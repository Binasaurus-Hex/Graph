@echo off
nasm -f win64 float_hello.asm -o float_hello.obj 
link float_hello.obj /subsystem:console /defaultlib:legacy_stdio_definitions.lib /defaultlib:msvcrt.lib /entry:main
float_hello.exe