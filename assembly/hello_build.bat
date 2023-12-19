@echo off
ml64 hello.asm /link /subsystem:console /defaultlib:kernel32.lib /defaultlib:user32.lib /entry:main
hello.exe
echo exit code : %errorlevel%