rem load the submission data. this should be done after loading the background data

IF NOT DEFINED HIPC_DATA_HOME SET HIPC_DATA_HOME=C:\data_collection\hipc_data

echo start loading controlled vocabulary %time%
java -ea -jar admin\target\dashboard-admin.jar -cv
echo start loading submission data %time%
java -ea -jar admin\target\dashboard-admin.jar -o

echo start indexing %time%
java -jar admin\target\dashboard-admin.jar -i
rem there is no meaningful scoring for now
rem echo start subject ranking %time%
rem java -jar admin\target\dashboard-admin.jar -r

echo end time %time%
