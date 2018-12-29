%~d0
cd %~p0

call variables.bat
SET RUNNABLE_JAR_DIRECTORY_2=%WINBOARD_INSTALLATION_PATH%\LeokomChessTest
SET RUN_JAR_PATH_2=%RUNNABLE_JAR_DIRECTORY_2%\Chess.jar

rem you may specify -Dblack=Simple
SET RUN_OPTIONS_2=
SET ENGINE_2=%JAVA_PATH% %RUN_OPTIONS_2% -jar %RUN_JAR_PATH_2%

SET MATCHES_COUNT=1

rem to turn on debug mode add -debug
rem it will create winboard debug log

rem -mg means match game
rem -testClaims disabled claims test in order to allow draw claim manually from the engine without adjudication
%WINBOARD_PATH% ^
-debug ^
-reuseFirst false ^
-mg %MATCHES_COUNT% ^
-fcp "%ENGINE%" ^
-fd "%RUNNABLE_JAR_DIRECTORY%" ^
-scp "%ENGINE_2%" ^
-sd "%RUNNABLE_JAR_DIRECTORY_2%" ^
-testClaims false
