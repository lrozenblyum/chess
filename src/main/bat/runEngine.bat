@rem =======================================================
@rem Runs Chess deployed to <WinboardRoot>/LeokomChess
@rem Pre-requisites:
@rem variables.bat contains correct configuration
@rem =======================================================

@rem support Eclipse which doesn't set up working directory to this file's directory
@rem however the working directory can be configured if we create an external tools configuration for this file
@rem it also will then redirect the output to Eclipse console
%~d0
cd %~p0

call variables.bat

@rem to turn on debug mode add -debug
@rem it will create winboard debug log

%WINBOARD_PATH% ^
-debug ^
-cp ^
-reuseFirst false ^
-fcp "%ENGINE%" ^
-fd "%RUNNABLE_JAR_DIRECTORY%"