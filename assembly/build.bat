@echo off
ml64 main.asm /link /subsystem:console /defaultlib:kernel32.lib /defaultlib:user32.lib /defaultlib:legacy_stdio_definitions.lib /defaultlib:msvcrt.lib
main.exe
echo exit code : %errorlevel%