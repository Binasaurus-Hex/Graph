format PE64
entry main
section '.text' code readable executable
print_float:
push rbp
mov rbp, rsp
sub rsp, 4 * 8
mov rcx, _float_format
mov rdx, [rbp + 2 * 8]
call [printf]
leave
ret
print_int:
push rbp
mov rbp, rsp
sub rsp, 4 * 8
mov rcx, _int_format
mov rdx, [rbp + 2 * 8]
call [printf]
leave
ret
sub rsp, 0
call main
add rsp, 0
xor rax, rax
call ExitProcess
main:
push rbp
mov rbp, rsp
sub rsp, 48
mov QWORD [rbp - 0], 3
mov QWORD [rbp - 8], 4
mov QWORD [rbp - 16], 9
mov rax, QWORD [rbp - 8]
mov rbx, QWORD [rbp - 16]
imul rax, rbx
mov QWORD [rbp - 24], rax
mov rax, QWORD [rbp - 0]
mov rbx, QWORD [rbp - 24]
add rax, rbx
mov QWORD [rbp - 32], rax
sub rsp, 8
push QWORD [rbp - 32]
call print_int
add rsp, 8
add rsp, 40
leave
ret
section '.data' data readable writeable
_float_format db 'the float is %f', 10,  0
_int_format db 'the int is %d', 10,  0
section '.idata' import data readable writeable
dd 0,0,0,RVA kernel32.dll_name,RVA kernel32.dll_table
dd 0,0,0,RVA msvcrt.dll_name,RVA msvcrt.dll_table
dd 0,0,0,0,0
kernel32.dll_table:
ExitProcess dq RVA _ExitProcess
dq 0
msvcrt.dll_table:
printf dq RVA _printf
dq 0
kernel32.dll_name db 'KERNEL32.DLL',0
msvcrt.dll_name db 'MSVCRT.DLL',0
_ExitProcess dw 0
db 'ExitProcess',0
_printf dw 0
db 'printf',0
