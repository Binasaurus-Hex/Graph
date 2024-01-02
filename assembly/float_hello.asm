default rel
bits 64

segment .bss
    c resq 2

segment .data
    a dd 3.25
    b dd -10.53
    x dd 3.5, 8.2, 10.5, 1.3
    y dq 4.4, 5.5, 6.6, 7.7
    z dd 1.2, 2.3, 3.4, 4.56, 5.53, 2.34, 2.12, 8.01

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

print_int:
  push    rbp
  mov    rbp, rsp
  sub    rsp, 32
segment .data
.format db '%d', 0
segment .text
    mov rdx, rcx
    lea rcx, [.format]
    call printf
    leave
    ret

main:
    call _CRT_INIT                 ; Needed since our entry point is not _DllMainCRTStartup. See https://msdn.microsoft.com/en-us/library/708by912.aspx

    push    rbp
    mov    rbp, rsp
    sub    rsp, 32

    mov rbx, 0
loop_start:
    mov rcx, rbx
    inc rbx
    cmp rbx, 100000000
    jle loop_start
    call print_int

    mov QWORD [rbp - 4], 102990

    movsd    xmm0, [a]             ; Load a
    movsd    xmm1, [b]             ; Load b
    vaddss    xmm2, xmm0, xmm1      ; xmm2 = xmm1 + xmm2
    movsd [a], xmm2

    movsd    xmm0, [a]
    call print_float


    ; Now let's try moving packed data around
    movups    xmm0, [x]         ; Move 4 floats to xmm0
    vmovups    ymm0, [z]        ; Move 8 floats to ymm0
    vmovupd    ymm1, [y]        ; Move 4 doubles to ymm1
    movupd    [a], xmm0         ; Move 2 doubles to a

    addps    xmm0, xmm0         ; Packed add
    vaddpd    ymm1, ymm1

    vmovupd    ymm2, ymm1

    vsubpd    ymm3, ymm2, ymm1  ; ymm2 - ymm1, store in ymm3

    ; Unordered comparison of floating point single scalar
    ucomiss    xmm0, xmm1
    jbe    less_or_equal

less_or_equal:
    movss    xmm3, [a]

    xor eax, eax                ; return 0
    call ExitProcess