#! /bin/sh

# This script starts the contact matcher server. 
# sushma, 14/11/2012

set -x

IMPORT_EXPORT_PROCESS=ImportExportRunner

MAIN_PROCESS=`ps -ef | grep $IMPORT_EXPORT_PROCESS | grep -v grep | tr -s " " " "  | cut -f2 -d" "`

if [ x"$MAIN_PROCESS" != x ]
then
	echo Import Export Runner is already running.  Please stop it first.
	exit 1
fi

cd `dirname $0`
CLASSPATH=import-export.jar:lib/*:conf
nohup /usr/java/jdk1.6.0_27/bin/java -Xms1g -Xmx1g -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -XX:+CMSIncrementalPacing -XX:CMSIncrementalDutyCycleMin=10 -XX:CMSIncrementalDutyCycle=50 -XX:ParallelGCThreads=8 -classpath $CLASSPATH com.teaminology.hp.ImportExportRunner &

echo Import Export Runner started ...