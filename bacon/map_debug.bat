@echo off
Set /P map= Enter filename of map: 
Set /P x=start x: 
Set /P y= start y: 

@echo off
IF EXIST "C:\Program Files\Java\jre7\bin\java.exe" (
"C:\Program Files\Java\jre7\bin\java.exe" -jar game.jar party=party_playerTest debug=develop map=%map% x=%x% y=%y% encounters=off
) ELSE IF EXIST "C:\Program Files\Java\jre6\bin\java.exe" (
"C:\Program Files\Java\jre6\bin\java.exe" -jar game.jar party=party_playerTest debug=develop map=%map% x=%x% y=%y% encounters=off
) ELSE IF EXIST "C:\Program Files (x86)\Java\jre6\bin\java.exe" (
"C:\Program Files (x86)\Java\jre6\bin\java.exe" -jar game.jar party=party_playerTest debug=develop map=%map% x=%x% y=%y% encounters=off
) ELSE IF EXIST "C:\Program Files (x86)\Java\jre7\bin\java.exe" (
"C:\Program Files (x86)\Java\jre7\bin\java.exe" -jar game.jar party=party_playerTest debug=develop map=%map% x=%x% y=%y% encounters=off
) ELSE (
@echo on
java -jar game.jar map=%map% x=%x% y=%y%
)
PAUSE