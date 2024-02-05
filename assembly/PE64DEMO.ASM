
; Example of 64-bit PE program

format PE64
entry start

section '.text' code readable executable

  start:
  push rbp
  mov rbp, rsp
	sub	rsp,8*6

  mov rcx, 1.0
  mov rax, 3.0
  mov QWORD[rbp - 8], 1078774989
  mov QWORD[rbp - 16], 1071040692
  movq xmm0, [rbp - 8]
  movq xmm1, [rbp - 16]

  addsd xmm0, xmm1
  movq [rsp + 4 * 8], xmm0

  call print_float

  mov rcx, 1.0
  mov rax, 3.0
  movq xmm0, rcx
  movq xmm1, rax

  mulsd xmm0, xmm1
  movq [rsp + 4 * 8], xmm0

  call print_float

  mov rcx, 1.0
  mov rax, 3.0
  movq xmm0, rcx
  movq xmm1, rax

  divsd xmm0, xmm1
  movq [rsp + 4 * 8], xmm0

  call print_float


  mov rcx, 1.0
  mov rax, 3.0
  movq xmm0, rcx
  movq xmm1, rax

  subsd xmm0, xmm1
  movq [rsp + 4 * 8], xmm0

  call print_float

	xor ecx, ecx
	call	[ExitProcess]

  print_float:
    push rbp
    mov rbp, rsp
    sub rsp, 4 * 8
    mov rcx, _float_format
    mov rdx, [rbp + 6 * 8]
    call [printf]
    leave
    ret


section '.data' data readable writeable

  _message db 'Hello World!',0
  _float_format db 'the float is %f', 10,  0

section '.idata' import data readable writeable

  dd 0,0,0,RVA kernel_name,RVA kernel_table
  dd 0,0,0,RVA user_name,RVA user_table
  dd 0,0,0,RVA msvcrt_name ,RVA msvcrt_table
  dd 0,0,0,0,0

  kernel_table:
    ExitProcess dq RVA _ExitProcess
    dq 0
  user_table:
    MessageBoxA dq RVA _MessageBoxA
    dq 0
  msvcrt_table:
     printf dq RVA _printf
     dq 0

  kernel_name db 'KERNEL32.DLL',0
  user_name db 'USER32.DLL',0
  msvcrt_name db 'MSVCRT.DLL', 0

  _ExitProcess dw 0
    db 'ExitProcess',0
  _MessageBoxA dw 0
    db 'MessageBoxA',0
  _printf dw 0
    db 'printf', 0
