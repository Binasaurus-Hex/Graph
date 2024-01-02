default rel
bits 64

segment .text
global main
extern _CRT_INIT
extern ExitProcess
extern printf
print_float:
  push    rbp
  mov    rbp, rsp
  sub    rsp, 32
segment .data
    .format db 'float : %f', 0xa, 0
segment .text
    lea    rcx, [.format]
    movss xmm1, xmm0
    cvtss2sd    xmm1, xmm1
    movq    rdx, xmm1
    call    printf
    leave
    ret
call main
add rsp, 0
xor rax, rax
call ExitProcess
main:
push rbp
mov rbp, rsp
sub rsp, 32
mov [rbp - 0], DWORD 1078774989
mov [rbp - 4], DWORD 1067030938
movsd xmm0, [rbp - 0]
movsd xmm1, [rbp - 4]
vaddsd xmm0, xmm1
movsd [rbp - 8], xmm0
push QWORD [rbp - 8]
call print_float
add rsp, 12
mov rsp, rbp
pop rbp
ret
