SET WINBOARD_PATH=E:\Games\WinBoard-4.6.2\WinBoard\winboard.exe
rem this directory must be synchronous with jar destination
rem I use the Winboard installation sub-dir for simplicity
SET RUNNABLE_JAR_DIRECTORY=E:\Games\WinBoard-4.6.2\LeokomChess
SET RUN_JAR_PATH=%RUNNABLE_JAR_DIRECTORY%\Chess.jar

rem to turn on debug mode add -debug
rem it will create winboard debug log
%WINBOARD_PATH% -debug -cp -fcp "D:\Java\jdk1.7.0_45\bin\java.exe -jar %RUN_JAR_PATH%" -fd "%RUNNABLE_JAR_DIRECTORY%"
rem for debugging purposes add -debug to the line above