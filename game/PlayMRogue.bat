@echo off
IF EXIST "C:\Program Files\Java\jre6\bin\java.exe" (
"C:\Program Files\Java\jre6\bin\java.exe" -jar MRogue.jar
) ELSE IF EXIST "C:\Program Files\Java\jre7\bin\java.exe" (
"C:\Program Files\Java\jre7\bin\java.exe" -jar MRogue.jar
) ELSE (
@echo on
echo You don't appear to have Java installed, do that first!
)
@echo on
PAUSE