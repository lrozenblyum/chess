call variables.bat
SET RUNNABLE_JAR_DIRECTORY_2=E:\Games\WinBoard-4.7.3\LeokomChessTest
SET RUN_JAR_PATH_2=%RUNNABLE_JAR_DIRECTORY_2%\Chess.jar

rem for Winboard integrations we simply don't care about color of the opponent
rem the goal is just to specify WHAT is the opponent
rem specifying 'unlogically' -Dblack=Legal since -Dwhite=Legal is unsupported now
SET RUN_OPTIONS_2=-Dblack=Legal

rem to turn on debug mode add -debug
rem it will create winboard debug log

rem -mg means match game
%WINBOARD_PATH% -debug  -reuseFirst false -mg 1 -fcp "%JAVA_PATH% %RUN_OPTIONS% -jar %RUN_JAR_PATH%" -fd "%RUNNABLE_JAR_DIRECTORY%" -scp "%JAVA_PATH% %RUN_OPTIONS_2% -jar %RUN_JAR_PATH_2%" -sd "%RUNNABLE_JAR_DIRECTORY_2%"

