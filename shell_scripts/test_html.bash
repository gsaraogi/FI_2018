#!/bin/bash
TITLE="DB objects deployment report"
RIGHT_NOW=$(date +"%x %r %Z")
TIME_STAMP="DB objects deployment report as on $RIGHT_NOW"
echo "
<HTML>
    <HEAD>
        <TITLE>
        $TITLE
                </TITLE>
                </HEAD>
                <BODY>
                <P><H1><strong><u>$TIME_STAMP</u><strong></H1></P>
                </BODY>
" >> /C/Users/gsaraogi/Desktop/report.html
echo "
        <table border=\"1\" cellpadding=\"1\" cellspacing=\"1\">
        <tbody>
        <tr style=\"background-color: #A4A4A4\">
        <th>SQL File</th><th>Object Name</th><th>Error</th><th>
                </tr>" >> /C/Users/gsaraogi/Desktop/report.html 
#echo "<table>" > report.html 
while read INPUT 
do 
echo "<tr><td>${INPUT//,/</td><td>}</td></tr>" >> /C/Users/gsaraogi/Desktop/report.html 
done < /C/Users/gsaraogi/Desktop/report1.txt
echo "</table>" >> /C/Users/gsaraogi/Desktop/report.html

echo "          </tbody>
                </table> " >> /C/Users/gsaraogi/Desktop/report.html
				
echo "<BODY><br><br>Thanks and Regards<br>
Deployment Team<br><br>
<b><i>Disclaimer: Please do not reply to this system generated email. Kindly contact run team for concerns.</i></b></BODY>" >> /C/Users/gsaraogi/Desktop/report.html