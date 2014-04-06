@echo off
IF EXIST "C:\Program Files\Java\jre7\bin\java.exe" (
"C:\Program Files\Java\jre7\bin\java.exe" -jar game.jar alpha_save.sav
) ELSE IF EXIST "C:\Program Files\Java\jre6\bin\java.exe" (
"C:\Program Files\Java\jre6\bin\java.exe" -jar game.jar alpha_save.sav
) ELSE IF EXIST "C:\Program Files (x86)\Java\jre6\bin\java.exe" (
"C:\Program Files (x86)\Java\jre6\bin\java.exe" -jar game.jar alpha_save.sav
) ELSE IF EXIST "C:\Program Files (x86)\Java\jre7\bin\java.exe" (
"C:\Program Files (x86)\Java\jre7\bin\java.exe" -jar game.jar alpha_save.sav
) ELSE (
PAUSE
@echo on
echo You don't appear to have Java installed, do that first!
)