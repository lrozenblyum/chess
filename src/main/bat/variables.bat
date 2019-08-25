@rem =======================================================
@rem Tunes common settings:
@rem * Winboard location
@rem * Java executable location
@rem * All derivatives and default engine settings 
@rem =======================================================

@rem the variables should be tuned per target environment
SET WINBOARD_INSTALLATION_PATH=E:\Games\WinBoard-4.8.0
SET JAVA_PATH=Q:\Program Files\Java\jdk1.8.0_162\bin\java.exe

@rem UI that we use to run our Chess with
SET WINBOARD_PATH=%WINBOARD_INSTALLATION_PATH%\WinBoard\winboard.exe
@rem I use the Winboard installation as a Chess deployment target
@rem it should be equal to 'project.deployDirectory' property in pom.xml
SET BRAND_NAME=LeokomChess
SET RUNNABLE_JAR_DIRECTORY=%WINBOARD_INSTALLATION_PATH%\%BRAND_NAME%
SET RUN_JAR_PATH=%RUNNABLE_JAR_DIRECTORY%\%BRAND_NAME%.jar
@rem you may pass -Dblack.engine=brain.simple or brain.denormalized or brain.random to choose a different engine for blacks
@rem for brain.normalized you may specify -Dblack.depth (1 or 2)
SET RUN_OPTIONS=-Dblack.depth=2

SET ENGINE=%JAVA_PATH% %RUN_OPTIONS% -jar %RUN_JAR_PATH%