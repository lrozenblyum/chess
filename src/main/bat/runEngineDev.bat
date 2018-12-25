rem =======================================================
rem Runs Chess in development mode
rem skips tests, packages fast to correct target directory
rem Pre-requisites:
rem JAVA_HOME pointing to a valid Java installation
rem M3_HOME pointing to a valid Maven3 installation
rem =======================================================

rem ----
rem Technical remarks:
rem current solution is based on simple Maven and other bat-file usage
rem Other technical possibilities to implement the job is
rem a) call java directly from the bat file:
rem        * need to form correct classpath or duplicate it with maven's
rem b) call mvn exec:java
rem        * here I didn't find a way to make winboard use the mvn's console
rem ----

%~d0
cd %~p0

set INITIAL_DIRECTORY=%cd%

rem based on fact we're in src/main/bat and need to go to pom.xml location
cd ..\..\..
rem need to use call, otherwise maven takes the control over the batch
call %M3_HOME%\bin\mvn clean package -DskipTests=true

rem another option would be cd src/main/bat
cd %INITIAL_DIRECTORY%
call runEngine.bat