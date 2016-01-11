@echo off
echo Deleting all IntelliJ IDE files from the project. You will have to import project again after that.
echo Press Ctrl-C to Cancel.
pause
cd source-project
rmdir /S /Q .idea
del /S *.iml
cd ..