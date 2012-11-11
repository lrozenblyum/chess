SET WINBOARD_PATH=E:\Games\WinBoard-4.6.2\WinBoard\winboard.exe
rem this directory must be synchronous with jar destination
rem I use the Winboard installation sub-dir for simplicity
SET RUNNABLE_JAR_DIRECTORY=E:\Games\WinBoard-4.6.2\LeokomChess
SET RUN_JAR_PATH=%RUNNABLE_JAR_DIRECTORY%\Chess.jar


%WINBOARD_PATH% -cp -fcp "java -jar %RUN_JAR_PATH%" -fd "%RUNNABLE_JAR_DIRECTORY%"
rem for debugging purposes add -debug to the line above