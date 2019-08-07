@rem =======================================================
@rem Runs 2-engine match between Chess deployed to <WinboardRoot>/LeokomChess and <WinboardRoot>/LeokomChessTest
@rem Pre-requisites:
@rem variables.bat contains correct configuration
@rem =======================================================

%~d0
cd %~p0

call variables.bat
SET RUNNABLE_JAR_DIRECTORY_2=%WINBOARD_INSTALLATION_PATH%\LeokomChessTest
SET RUN_JAR_PATH_2=%RUNNABLE_JAR_DIRECTORY_2%\Chess.jar

@rem you may specify -Dblack.engine=brain.simple or brain.denormalized for Chess 0.5+
@rem you may specify -Dblack.engine=Simple for Chess 0.4
@rem you may specify -Dblack=Simple for Chess <= 0.3
@rem for LegalPlayer you may specify -Dblack.depth=2 (if the second instance is Chess >= 0.4)
SET RUN_OPTIONS_2=
SET ENGINE_2=%JAVA_PATH% %RUN_OPTIONS_2% -jar %RUN_JAR_PATH_2%

SET MATCHES_COUNT=1

@rem to turn on debug mode add -debug
@rem it will create winboard debug log

@rem -mg means match game
@rem -testClaims disabled claims test in order to allow draw claim manually from the engine without adjudication
%WINBOARD_PATH% ^
-debug ^
-reuseFirst false ^
-mg %MATCHES_COUNT% ^
-fcp "%ENGINE%" ^
-fd "%RUNNABLE_JAR_DIRECTORY%" ^
-scp "%ENGINE_2%" ^
-sd "%RUNNABLE_JAR_DIRECTORY_2%" ^
-testClaims false
