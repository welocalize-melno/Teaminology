#! /bin/sh
set -x

IMPORT_EXPORT_PROCESS=ImportExportRunner

ps -ef | grep $IMPORT_EXPORT_PROCESS | grep -v grep

MAIN_PROCESS=`ps -ef | grep $IMPORT_EXPORT_PROCESS | grep -v grep | tr -s " " " "  | head -1 | cut -f2 -d" "`

if [ x"$MAIN_PROCESS" = x ]
then
	echo Import Export Runner is not running
	exit 0
else
	kill $MAIN_PROCESS 2> /dev/null 
	echo Import Export Runner stopped
fi

ps -ef | grep $IMPORT_EXPORT_PROCESS
