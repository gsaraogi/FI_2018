#!/bin/bash
if echo "exit;" | sqlplus SYSTEM/Oracle_1@//localhost:1521/pdborcl 2>&1 | grep -q "Connected to"
then echo "connected OK"
exit 0
else echo "connection FAIL"
exit 1
fi

