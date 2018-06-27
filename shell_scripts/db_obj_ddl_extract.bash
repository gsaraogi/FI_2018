#!/bin/bash
export script_name=`basename $0 .bash`
export bin_dir="/C/Users/gsaraogi/Desktop"

if [[ -f $bin_dir/prop.cfg ]];
then
. $bin_dir/prop.cfg
else
echo "Config file sql_config.cfg not found. Exiting..."
exit 1
fi

if [[ ! -f $bin_dir/obj_lst.csv ]];
then
echo "object list not found. Exiting..."
exit 2
fi

for line in `cat $bin_dir/obj_lst.csv`
do
obj_type=`echo $line | awk -F',' '{print $1;}'`
obj_name=`echo $line | awk -F',' '{print $2;}'`
obj_own=`echo $line | awk -F',' '{print $3;}'`
$ORA_HOME/sqlplus -s $DB_USR/$DB_PWD@$DB_URL <<_EOF >> sqlplus_log 2>&1
BEGIN
    dbms_metadata.set_transform_param(dbms_metadata.session_transform,'STORAGE',false);
    dbms_metadata.set_transform_param(dbms_metadata.session_transform,'TABLESPACE',false);
    dbms_metadata.set_transform_param(dbms_metadata.session_transform,'SEGMENT_ATTRIBUTES',false);
    dbms_metadata.set_transform_param(dbms_metadata.session_transform,'REF_CONSTRAINTS',false);
END;
/
SET trimspool ON
SET heading off
SET linesize 300
SET echo off
SET pages 999
SET long 90000
set termout off
set feedback off
SET longchunksize 90000
column txt format a121 word_wrapped
Col object_type format a10000
Col object_name format a10000
Col owner format a10000
 
spool db_obj_ddl.sql append

SELECT DBMS_METADATA.GET_DDL('$obj_type','$obj_name','$obj_own')
     FROM ALL_OBJECTS u where u.object_type = '$obj_type' AND u.object_name = '$obj_name' AND owner = '$obj_own';
spool off;
exit;
_EOF
done
