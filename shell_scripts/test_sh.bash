#!/bin/bash
export script_name=`basename $0 .bash`
export bin_dir="/C/Users/gsaraogi/Desktop"
export sql_script=$1

if [[ $# -ne 1 ]];
then
echo "wrong number of arguments passed. Exiting..."
exit 1
fi

if [[ -f $bin_dir/sql_wrapper.cfg ]];
then
. $bin_dir/sql_wrapper.cfg
else
echo "Config file sql_wrapper.cfg not found. Exiting..."
exit 1
fi

$ORA_HOME/sqlplus -s $DB_USR/$DB_PWD@$DB_URL <<_EOF > $log_dir/sqlplus_log 2>&1
@'$sql_script'
exit;
_EOF


if [[ -s $log_dir/sqlplus_log ]];
then
echo "ERROR: $script_name completed with errors. Please check report file for details." >> $log_dir/$script_name.log
grep -q "ORA" $log_dir/sqlplus_log && awk '/TABLE/{nr[NR]; nr[NR+3]}; NR in nr' $log_dir/sqlplus_log > $log_dir/report_file_error
awk '/INSERT/{nr[NR]; nr[NR+3]}; NR in nr' $log_dir/sqlplus_log  >> $log_dir/report_file_error
awk '/UPDATE/{nr[NR]; nr[NR+3]}; NR in nr' $log_dir/sqlplus_log  >> $log_dir/report_file_error
awk '/DELETE/{nr[NR]; nr[NR+3]}; NR in nr' $log_dir/sqlplus_log  >> $log_dir/report_file_error
awk '/MERGE/{nr[NR]; nr[NR+3]}; NR in nr' $log_dir/sqlplus_log  >> $log_dir/report_file_error
awk '{for (I=1;I<=NF;I++) if ($I == "TABLE") {print $(I+1)};}' $log_dir/report_file_error > $log_dir/tbl_lst
awk '{for (I=1;I<=NF;I++) if ($I == "UPDATE") {print $(I+1)};}' $log_dir/report_file_error >> $log_dir/tbl_lst
awk '{for (I=1;I<=NF;I++) if ($I == "INTO") {print $(I+1)};}' $log_dir/report_file_error >> $log_dir/tbl_lst
awk '{for (I=1;I<=NF;I++) if ($I == "FROM") {print $(I+1)};}' $log_dir/report_file_error >> $log_dir/tbl_lst
awk '/:/{nr[NR]; nr[NR]}; NR in nr' $log_dir/report_file_error > $log_dir/err_lst
paste -d',' $log_dir/tbl_lst $log_dir/err_lst > $log_dir/report.txt
awk -v sql_file=`basename $sql_script` $'{print sql_file","$0;}' $log_dir/report.txt > $log_dir/report1.txt
if [[ -s $log_dir/report1.txt ]];
then
bash $log_dir/test_html.bash
fi
exit 1
else
echo "SUCCESS: $script_name completed successfully." >> $log_dir/$script_name.log
exit 0
fi

