rem rebuild and deploy the project to local tomcat

IF NOT DEFINED CATALINA_HOME SET CATALINA_HOME=C:\apache-tomcat-7.0.41
IF NOT DEFINED HIPC_DATA_HOME SET HIPC_DATA_HOME=C:\data_collection\hipc_data
echo CATALINA_HOME is %CATALINA_HOME%
echo HIPC_DATA_HOME is %HIPC_DATA_HOME%

call mvn clean
if ERRORLEVEL 1 (
    echo something went wrong in cleaning
    exit /b 1
)
call mvn package
if ERRORLEVEL 1 (
    echo something went wrong
    exit /b 1
)
rmdir /s /q %CATALINA_HOME%\webapps\hipc-signature
copy .\web\target\hipc-signature.war %CATALINA_HOME%\webapps
%CATALINA_HOME%\bin\startup.bat