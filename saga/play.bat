@echo off
IF EXIST "C:\Program Files\Java\jre7\bin\java.exe" (
"C:\Program Files\Java\jre7\bin\java.exe" -jar game.jar debug=release
) ELSE IF EXIST "C:\Program Files\Java\jre6\bin\java.exe" (
"C:\Program Files\Java\jre6\bin\java.exe" -jar game.jar debug=release
) ELSE IF EXIST "C:\Program Files (x86)\Java\jre6\bin\java.exe" (
"C:\Program Files (x86)\Java\jre6\bin\java.exe" -jar game.jar debug=release
) ELSE IF EXIST "C:\Program Files (x86)\Java\jre7\bin\java.exe" (
"C:\Program Files (x86)\Java\jre7\bin\java.exe" -jar game.jar debug=release
) ELSE IF EXIST "C:\Program Files (x86)\Java\jre7\bin\java.exe" (
"C:\Program Files\Java\jre1.8.0_25\bin\java.exe" -jar game.jar debug=release
) ELSE IF EXIST "C:\Program Files (x86)\Java\jre7\bin\java.exe" (
"C:\Program Files\Java\jdk1.8.0_25\bin\java.exe" -jar game.jar debug=release
) ELSE (
@echo on
echo You don't appear to have Java installed, do that first!
)
PAUSE