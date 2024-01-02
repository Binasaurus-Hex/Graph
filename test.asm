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
    .format db 'float is : %f', 0xa, 0
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
+:
push rbp
mov rbp, rsp
sub rsp, 64
movsd xmm0, QWORD [rbp - 40]
movsd xmm1, QWORD [rbp - 48]
vaddsd xmm0, xmm1
movsd QWORD [rbp - 56], xmm0
movsd xmm0, QWORD [rbp - 88]
movsd xmm1, QWORD [rbp - 96]
vaddsd xmm0, xmm1
movsd QWORD [rbp - 104], xmm0
mov QWORD [rbp - 0], QWORD [rbp - -48]
mov QWORD [rbp - 64], QWORD [rbp - 16]
add rsp, 56
mov rsp, rbp
pop rbp
ret
add rsp, 56
mov rsp, rbp
pop rbp
ret
-:
push rbp
mov rbp, rsp
sub rsp, 64
movsd xmm0, QWORD [rbp - 40]
movsd xmm1, QWORD [rbp - 48]
vsubsd xmm0, xmm1
movsd QWORD [rbp - 56], xmm0
movsd xmm0, QWORD [rbp - 88]
movsd xmm1, QWORD [rbp - 96]
vsubsd xmm0, xmm1
movsd QWORD [rbp - 104], xmm0
mov QWORD [rbp - 0], QWORD [rbp - -48]
mov QWORD [rbp - 64], QWORD [rbp - 16]
add rsp, 56
mov rsp, rbp
pop rbp
ret
add rsp, 56
mov rsp, rbp
pop rbp
ret
*:
push rbp
mov rbp, rsp
sub rsp, 64
movsd xmm0, QWORD [rbp - 40]
movsd xmm1, QWORD [rbp - 48]
vmulsd xmm0, xmm1
movsd QWORD [rbp - 56], xmm0
movsd xmm0, QWORD [rbp - 88]
movsd xmm1, QWORD [rbp - 96]
vmulsd xmm0, xmm1
movsd QWORD [rbp - 104], xmm0
mov QWORD [rbp - 0], QWORD [rbp - -48]
mov QWORD [rbp - 64], QWORD [rbp - 16]
add rsp, 56
mov rsp, rbp
pop rbp
ret
add rsp, 56
mov rsp, rbp
pop rbp
ret
/:
push rbp
mov rbp, rsp
sub rsp, 64
movsd xmm0, QWORD [rbp - 40]
movsd xmm1, QWORD [rbp - 48]
vdivsd xmm0, xmm1
movsd QWORD [rbp - 56], xmm0
movsd xmm0, QWORD [rbp - 88]
movsd xmm1, QWORD [rbp - 96]
vdivsd xmm0, xmm1
movsd QWORD [rbp - 104], xmm0
mov QWORD [rbp - 0], QWORD [rbp - -48]
mov QWORD [rbp - 64], QWORD [rbp - 16]
add rsp, 56
mov rsp, rbp
pop rbp
ret
add rsp, 56
mov rsp, rbp
pop rbp
ret
*:
push rbp
mov rbp, rsp
sub rsp, 64
movsd xmm0, QWORD [rbp - 32]
movsd xmm1, QWORD [rbp - -24]
vmulsd xmm0, xmm1
movsd QWORD [rbp - 40], xmm0
movsd xmm0, QWORD [rbp - 64]
movsd xmm1, QWORD [rbp - -24]
vmulsd xmm0, xmm1
movsd QWORD [rbp - 72], xmm0
mov QWORD [rbp - 0], QWORD [rbp - -48]
mov QWORD [rbp - 64], QWORD [rbp - 16]
add rsp, 40
mov rsp, rbp
pop rbp
ret
add rsp, 40
mov rsp, rbp
pop rbp
ret
/:
push rbp
mov rbp, rsp
sub rsp, 64
movsd xmm0, QWORD [rbp - 32]
movsd xmm1, QWORD [rbp - -24]
vdivsd xmm0, xmm1
movsd QWORD [rbp - 40], xmm0
movsd xmm0, QWORD [rbp - 64]
movsd xmm1, QWORD [rbp - -24]
vdivsd xmm0, xmm1
movsd QWORD [rbp - 72], xmm0
mov QWORD [rbp - 0], QWORD [rbp - -48]
mov QWORD [rbp - 64], QWORD [rbp - 16]
add rsp, 40
mov rsp, rbp
pop rbp
ret
add rsp, 40
mov rsp, rbp
pop rbp
ret
length:
push rbp
mov rbp, rsp
sub rsp, 64
movsd xmm0, QWORD [rbp - 16]
movsd xmm1, QWORD [rbp - 24]
vmulsd xmm0, xmm1
movsd QWORD [rbp - 32], xmm0
movsd xmm0, QWORD [rbp - 56]
movsd xmm1, QWORD [rbp - 64]
vmulsd xmm0, xmm1
movsd QWORD [rbp - 72], xmm0
movsd xmm0, QWORD [rbp - 32]
movsd xmm1, QWORD [rbp - 72]
vaddsd xmm0, xmm1
movsd QWORD [rbp - 80], xmm0
sub rsp, 4
movsd xmm1, QWORD[rbp - 16]
call sqrt
pop QWORD [rbp - 96]
mov QWORD [rbp - 88], QWORD [rbp - -32]
add rsp, 48
mov rsp, rbp
pop rbp
ret
add rsp, 48
mov rsp, rbp
pop rbp
ret
normalized:
push rbp
mov rbp, rsp
sub rsp, 64
sub rsp, 4
movsd xmm1, QWORD[rbp - 16]
call length
add rsp, 4
pop QWORD [rbp - 16]
mov QWORD [rbp - 8], QWORD [rbp - 0]
movsd xmm0, QWORD [rbp - 48]
movsd xmm1, QWORD [rbp - 0]
vdivsd xmm0, xmm1
movsd QWORD [rbp - 56], xmm0
movsd xmm0, QWORD [rbp - 80]
movsd xmm1, QWORD [rbp - 0]
vdivsd xmm0, xmm1
movsd QWORD [rbp - 88], xmm0
mov QWORD [rbp - 16], QWORD [rbp - -40]
mov QWORD [rbp - 80], QWORD [rbp - 24]
add rsp, 48
mov rsp, rbp
pop rbp
ret
add rsp, 48
mov rsp, rbp
pop rbp
ret
print:
push rbp
mov rbp, rsp
sub rsp, 64
mov QWORD [rbp - 0], 50000
mov QWORD [rbp - 8], 2
movsd xmm1, QWORD[rbp - 16]
call print
movsd xmm1, QWORD[rbp - 16]
call print_float
mov QWORD [rbp - 32], 50002
mov QWORD [rbp - 40], 3
movsd xmm1, QWORD[rbp - 16]
call print
movsd xmm1, QWORD[rbp - 16]
call print_float
mov QWORD [rbp - 64], 50005
mov QWORD [rbp - 72], 2
movsd xmm1, QWORD[rbp - 16]
call print
add rsp, 40
mov rsp, rbp
pop rbp
ret
println:
push rbp
mov rbp, rsp
sub rsp, 32
movsd xmm1, QWORD[rbp - 16]
call print
add rsp, 4
mov QWORD [rbp - 0], 50000
mov QWORD [rbp - 8], 1
movsd xmm1, QWORD[rbp - 16]
call print
add rsp, 8
mov rsp, rbp
pop rbp
ret
timer_start:
push rbp
mov rbp, rsp
sub rsp, 64
mov QWORD [rbp - 32], 0
mov QWORD [rbp - 56], 0
mov QWORD [rbp - 0], QWORD [rbp - -48]
mov QWORD [rbp - 64], QWORD [rbp - 16]
mov QWORD [rbp - 128], QWORD [rbp - 80]
add rsp, 32
mov rsp, rbp
pop rbp
ret
add rsp, 32
mov rsp, rbp
pop rbp
ret
timer_update:
push rbp
mov rbp, rsp
sub rsp, 32
movsd xmm0, QWORD [rbp - 16]
movsd xmm1, QWORD [rbp - -24]
vaddsd xmm0, xmm1
movsd QWORD [rbp - 24], xmm0
add rsp, 16
mov rsp, rbp
pop rbp
ret
timer_complete:
push rbp
mov rbp, rsp
sub rsp, 64
movsd xmm0, QWORD [rbp - 24]
movsd xmm1, QWORD [rbp - 32]
 xmm0, xmm1
movsd QWORD [rbp - 40], xmm0
mov QWORD [rbp - 40], QWORD [rbp - 0]
mov rax, QWORD [rbp - 0]]
mov rbx, QWORD [rbp - 56]
 rax, rbx
mov QWORD [rbp - 80], 0
mov QWORD [rbp - 0], QWORD [rbp - -32]
add rsp, 44
mov rsp, rbp
pop rbp
ret
add rsp, 44
mov rsp, rbp
pop rbp
ret
set_color:
push rbp
mov rbp, rsp
sub rsp, 64
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
call extern_set_colour
add rsp, 32
mov rsp, rbp
pop rbp
ret
get_mouse_position:
push rbp
mov rbp, rsp
sub rsp, 32
sub rsp, 4
call get_mouse_x
pop QWORD [rbp - 32]
sub rsp, 4
call get_mouse_y
pop QWORD [rbp - 48]
mov QWORD [rbp - 0], QWORD [rbp - -32]
mov QWORD [rbp - 64], QWORD [rbp - 32]
add rsp, 24
mov rsp, rbp
pop rbp
ret
add rsp, 24
mov rsp, rbp
pop rbp
ret
accelerate:
push rbp
mov rbp, rsp
sub rsp, 32
sub rsp, 8
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
call +
add rsp, 8
pop QWORD [rbp - 32]
pop QWORD [rbp - 24]
add rsp, 16
mov rsp, rbp
pop rbp
ret
update_position:
push rbp
mov rbp, rsp
sub rsp, 128
sub rsp, 8
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
call -
add rsp, 8
pop QWORD [rbp - 48]
pop QWORD [rbp - 40]
mov QWORD [rbp - 32], QWORD [rbp - 0]
mov QWORD [rbp - 96], QWORD [rbp - 64]
movsd xmm0, QWORD [rbp - -24]
movsd xmm1, QWORD [rbp - -24]
vmulsd xmm0, xmm1
movsd QWORD [rbp - 88], xmm0
sub rsp, 8
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
call *
add rsp, 8
pop QWORD [rbp - 112]
pop QWORD [rbp - 104]
sub rsp, 8
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
call +
add rsp, 8
pop QWORD [rbp - 144]
pop QWORD [rbp - 136]
sub rsp, 8
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
call +
add rsp, 8
pop QWORD [rbp - 168]
pop QWORD [rbp - 160]
mov QWORD [rbp - 200], 0
mov QWORD [rbp - 216], 0
add rsp, 112
mov rsp, rbp
pop rbp
ret
constrain_to_circle:
push rbp
mov rbp, rsp
sub rsp, 128
sub rsp, 8
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
call -
add rsp, 8
pop QWORD [rbp - 48]
pop QWORD [rbp - 40]
mov QWORD [rbp - 32], QWORD [rbp - 0]
mov QWORD [rbp - 96], QWORD [rbp - 64]
sub rsp, 4
movsd xmm1, QWORD[rbp - 16]
call length
add rsp, 4
pop QWORD [rbp - 64]
movsd xmm0, QWORD [rbp - 80]
movsd xmm1, QWORD [rbp - 88]
vsubsd xmm0, xmm1
movsd QWORD [rbp - 96], xmm0
movsd xmm0, QWORD [rbp - 56]
movsd xmm1, QWORD [rbp - 96]
 xmm0, xmm1
movsd QWORD [rbp - 104], xmm0
sub rsp, 8
movsd xmm1, QWORD[rbp - 16]
call normalized
add rsp, 4
pop QWORD [rbp - 152]
pop QWORD [rbp - 144]
movsd xmm0, QWORD [rbp - 168]
movsd xmm1, QWORD [rbp - 176]
vsubsd xmm0, xmm1
movsd QWORD [rbp - 184], xmm0
sub rsp, 8
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
call *
add rsp, 8
pop QWORD [rbp - 216]
pop QWORD [rbp - 208]
sub rsp, 8
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
call +
add rsp, 8
pop QWORD [rbp - 240]
pop QWORD [rbp - 232]
add rsp, 120
mov rsp, rbp
pop rbp
ret
collide:
push rbp
mov rbp, rsp
sub rsp, 192
mov rax, QWORD [rbp - -32]]
mov rbx, QWORD [rbp - -24]
 rax, rbx
add rsp, 4
mov rsp, rbp
pop rbp
ret
sub rsp, 8
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
call -
add rsp, 8
pop QWORD [rbp - 56]
pop QWORD [rbp - 48]
mov QWORD [rbp - 40], QWORD [rbp - 8]
mov QWORD [rbp - 104], QWORD [rbp - 72]
sub rsp, 4
movsd xmm1, QWORD[rbp - 16]
call length
add rsp, 4
pop QWORD [rbp - 80]
mov QWORD [rbp - 72], QWORD [rbp - 56]
movsd xmm0, QWORD [rbp - 104]
movsd xmm1, QWORD [rbp - 112]
vaddsd xmm0, xmm1
movsd QWORD [rbp - 120], xmm0
mov QWORD [rbp - 120], QWORD [rbp - 80]
movsd xmm0, QWORD [rbp - 56]
movsd xmm1, QWORD [rbp - 80]
 xmm0, xmm1
movsd QWORD [rbp - 128], xmm0
sub rsp, 8
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
call /
add rsp, 8
pop QWORD [rbp - 176]
pop QWORD [rbp - 168]
mov QWORD [rbp - 160], QWORD [rbp - 136]
mov QWORD [rbp - 224], QWORD [rbp - 200]
movsd xmm0, QWORD [rbp - 80]
movsd xmm1, QWORD [rbp - 56]
vsubsd xmm0, xmm1
movsd QWORD [rbp - 184], xmm0
mov QWORD [rbp - 184], QWORD [rbp - 176]
mov QWORD [rbp - 208], 1056964608
movsd xmm0, QWORD [rbp - 208]
movsd xmm1, QWORD [rbp - 176]
vmulsd xmm0, xmm1
movsd QWORD [rbp - 216], xmm0
sub rsp, 8
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
call *
add rsp, 8
pop QWORD [rbp - 248]
pop QWORD [rbp - 240]
mov QWORD [rbp - 232], QWORD [rbp - 192]
mov QWORD [rbp - 296], QWORD [rbp - 256]
sub rsp, 8
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
call +
add rsp, 8
pop QWORD [rbp - 288]
pop QWORD [rbp - 280]
sub rsp, 8
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
call -
add rsp, 8
pop QWORD [rbp - 328]
pop QWORD [rbp - 320]
add rsp, 164
mov rsp, rbp
pop rbp
ret
get_balls:
push rbp
mov rbp, rsp
sub rsp, 3648
mov QWORD [rbp - 6728], 1
mov QWORD [rbp - 6728], QWORD [rbp - 6720]
mov QWORD [rbp - 6744], 0
mov QWORD [rbp - 6744], QWORD [rbp - 6736]
mov QWORD [rbp - 6760], 0
mov QWORD [rbp - 6760], QWORD [rbp - 6752]
mov QWORD [rbp - 6776], 1084227584
mov QWORD [rbp - 6776], QWORD [rbp - 6768]
mov QWORD [rbp - 6792], 0
mov QWORD [rbp - 6800], 120
mov QWORD [rbp - 6808], 1
mov QWORD [rbp - 6816], QWORD [rbp - 6784]
mov QWORD [rbp - 6832], 1092616192
mov QWORD [rbp - 6840], 10
mov rax, QWORD [rbp - 6752]]
mov rbx, QWORD [rbp - 6840]
 rax, rbx
mov QWORD [rbp - 6856], 1
mov rax, QWORD [rbp - 6736]]
mov rbx, QWORD [rbp - 6856]
add rax, rbx
mov QWORD [rbp - 6864], QWORD [rbp - 6736]
mov QWORD [rbp - 6872], 0
mov QWORD [rbp - 6872], QWORD [rbp - 6752]
mov QWORD [rbp - 6912], 1137180672
sub rsp, 4
movsd xmm1, QWORD[rbp - 16]
call _float
pop QWORD [rbp - 6928]
mov QWORD [rbp - 6936], 1073741824
movsd xmm0, QWORD [rbp - 6944]
movsd xmm1, QWORD [rbp - 6936]
vmulsd xmm0, xmm1
movsd QWORD [rbp - 6952], xmm0
movsd xmm0, QWORD [rbp - 6952]
movsd xmm1, QWORD [rbp - 6768]
vaddsd xmm0, xmm1
movsd QWORD [rbp - 6960], xmm0
movsd xmm0, QWORD [rbp - 6920]
movsd xmm1, QWORD [rbp - 6960]
vmulsd xmm0, xmm1
movsd QWORD [rbp - 6968], xmm0
movsd xmm0, QWORD [rbp - 6912]
movsd xmm1, QWORD [rbp - 6968]
vaddsd xmm0, xmm1
movsd QWORD [rbp - 6976], xmm0
mov QWORD [rbp - 6992], 1140457472
sub rsp, 4
movsd xmm1, QWORD[rbp - 16]
call _float
pop QWORD [rbp - 7008]
mov QWORD [rbp - 7016], 1073741824
movsd xmm0, QWORD [rbp - 7024]
movsd xmm1, QWORD [rbp - 7016]
vmulsd xmm0, xmm1
movsd QWORD [rbp - 7032], xmm0
movsd xmm0, QWORD [rbp - 7032]
movsd xmm1, QWORD [rbp - 6768]
vaddsd xmm0, xmm1
movsd QWORD [rbp - 7040], xmm0
movsd xmm0, QWORD [rbp - 7000]
movsd xmm1, QWORD [rbp - 7040]
vmulsd xmm0, xmm1
movsd QWORD [rbp - 7048], xmm0
movsd xmm0, QWORD [rbp - 6992]
movsd xmm1, QWORD [rbp - 7048]
vaddsd xmm0, xmm1
movsd QWORD [rbp - 7056], xmm0
mov QWORD [rbp - 7104], 953267991
mov QWORD [rbp - 7120], 953267991
mov QWORD [rbp - 7080], QWORD [rbp - 7064]
mov QWORD [rbp - 7144], QWORD [rbp - 7128]
sub rsp, 8
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
call +
add rsp, 8
pop QWORD [rbp - 7168]
pop QWORD [rbp - 7160]
mov QWORD [rbp - 7200], 0
mov QWORD [rbp - 7216], 1092416963
mov QWORD [rbp - 7224], 1
mov rax, QWORD [rbp - 6752]]
mov rbx, QWORD [rbp - 7224]
add rax, rbx
mov QWORD [rbp - 7232], QWORD [rbp - 6752]
mov QWORD [rbp - 7240], 1
mov rax, QWORD [rbp - 6720]]
mov rbx, QWORD [rbp - 7240]
add rax, rbx
mov QWORD [rbp - 7248], QWORD [rbp - 6720]
mov rax, QWORD [rbp - 6792]]
mov rbx, QWORD [rbp - 6808]
add rax, rbx
mov rax, QWORD [rbp - 6792]]
mov rbx, QWORD [rbp - 6800]
 rax, rbx
mov QWORD [rbp - 0], QWORD [rbp - -6736]
mov QWORD [rbp - 64], QWORD [rbp - -6672]
mov QWORD [rbp - 128], QWORD [rbp - -6608]
mov QWORD [rbp - 192], QWORD [rbp - -6544]
mov QWORD [rbp - 256], QWORD [rbp - -6480]
mov QWORD [rbp - 320], QWORD [rbp - -6416]
mov QWORD [rbp - 384], QWORD [rbp - -6352]
mov QWORD [rbp - 448], QWORD [rbp - -6288]
mov QWORD [rbp - 512], QWORD [rbp - -6224]
mov QWORD [rbp - 576], QWORD [rbp - -6160]
mov QWORD [rbp - 640], QWORD [rbp - -6096]
mov QWORD [rbp - 704], QWORD [rbp - -6032]
mov QWORD [rbp - 768], QWORD [rbp - -5968]
mov QWORD [rbp - 832], QWORD [rbp - -5904]
mov QWORD [rbp - 896], QWORD [rbp - -5840]
mov QWORD [rbp - 960], QWORD [rbp - -5776]
mov QWORD [rbp - 1024], QWORD [rbp - -5712]
mov QWORD [rbp - 1088], QWORD [rbp - -5648]
mov QWORD [rbp - 1152], QWORD [rbp - -5584]
mov QWORD [rbp - 1216], QWORD [rbp - -5520]
mov QWORD [rbp - 1280], QWORD [rbp - -5456]
mov QWORD [rbp - 1344], QWORD [rbp - -5392]
mov QWORD [rbp - 1408], QWORD [rbp - -5328]
mov QWORD [rbp - 1472], QWORD [rbp - -5264]
mov QWORD [rbp - 1536], QWORD [rbp - -5200]
mov QWORD [rbp - 1600], QWORD [rbp - -5136]
mov QWORD [rbp - 1664], QWORD [rbp - -5072]
mov QWORD [rbp - 1728], QWORD [rbp - -5008]
mov QWORD [rbp - 1792], QWORD [rbp - -4944]
mov QWORD [rbp - 1856], QWORD [rbp - -4880]
mov QWORD [rbp - 1920], QWORD [rbp - -4816]
mov QWORD [rbp - 1984], QWORD [rbp - -4752]
mov QWORD [rbp - 2048], QWORD [rbp - -4688]
mov QWORD [rbp - 2112], QWORD [rbp - -4624]
mov QWORD [rbp - 2176], QWORD [rbp - -4560]
mov QWORD [rbp - 2240], QWORD [rbp - -4496]
mov QWORD [rbp - 2304], QWORD [rbp - -4432]
mov QWORD [rbp - 2368], QWORD [rbp - -4368]
mov QWORD [rbp - 2432], QWORD [rbp - -4304]
mov QWORD [rbp - 2496], QWORD [rbp - -4240]
mov QWORD [rbp - 2560], QWORD [rbp - -4176]
mov QWORD [rbp - 2624], QWORD [rbp - -4112]
mov QWORD [rbp - 2688], QWORD [rbp - -4048]
mov QWORD [rbp - 2752], QWORD [rbp - -3984]
mov QWORD [rbp - 2816], QWORD [rbp - -3920]
mov QWORD [rbp - 2880], QWORD [rbp - -3856]
mov QWORD [rbp - 2944], QWORD [rbp - -3792]
mov QWORD [rbp - 3008], QWORD [rbp - -3728]
mov QWORD [rbp - 3072], QWORD [rbp - -3664]
mov QWORD [rbp - 3136], QWORD [rbp - -3600]
mov QWORD [rbp - 3200], QWORD [rbp - -3536]
mov QWORD [rbp - 3264], QWORD [rbp - -3472]
mov QWORD [rbp - 3328], QWORD [rbp - -3408]
mov QWORD [rbp - 3392], QWORD [rbp - -3344]
mov QWORD [rbp - 3456], QWORD [rbp - -3280]
mov QWORD [rbp - 3520], QWORD [rbp - -3216]
mov QWORD [rbp - 3584], QWORD [rbp - -3152]
mov QWORD [rbp - 3648], QWORD [rbp - -3088]
mov QWORD [rbp - 3712], QWORD [rbp - -3024]
mov QWORD [rbp - 3776], QWORD [rbp - -2960]
mov QWORD [rbp - 3840], QWORD [rbp - -2896]
mov QWORD [rbp - 3904], QWORD [rbp - -2832]
mov QWORD [rbp - 3968], QWORD [rbp - -2768]
mov QWORD [rbp - 4032], QWORD [rbp - -2704]
mov QWORD [rbp - 4096], QWORD [rbp - -2640]
mov QWORD [rbp - 4160], QWORD [rbp - -2576]
mov QWORD [rbp - 4224], QWORD [rbp - -2512]
mov QWORD [rbp - 4288], QWORD [rbp - -2448]
mov QWORD [rbp - 4352], QWORD [rbp - -2384]
mov QWORD [rbp - 4416], QWORD [rbp - -2320]
mov QWORD [rbp - 4480], QWORD [rbp - -2256]
mov QWORD [rbp - 4544], QWORD [rbp - -2192]
mov QWORD [rbp - 4608], QWORD [rbp - -2128]
mov QWORD [rbp - 4672], QWORD [rbp - -2064]
mov QWORD [rbp - 4736], QWORD [rbp - -2000]
mov QWORD [rbp - 4800], QWORD [rbp - -1936]
mov QWORD [rbp - 4864], QWORD [rbp - -1872]
mov QWORD [rbp - 4928], QWORD [rbp - -1808]
mov QWORD [rbp - 4992], QWORD [rbp - -1744]
mov QWORD [rbp - 5056], QWORD [rbp - -1680]
mov QWORD [rbp - 5120], QWORD [rbp - -1616]
mov QWORD [rbp - 5184], QWORD [rbp - -1552]
mov QWORD [rbp - 5248], QWORD [rbp - -1488]
mov QWORD [rbp - 5312], QWORD [rbp - -1424]
mov QWORD [rbp - 5376], QWORD [rbp - -1360]
mov QWORD [rbp - 5440], QWORD [rbp - -1296]
mov QWORD [rbp - 5504], QWORD [rbp - -1232]
mov QWORD [rbp - 5568], QWORD [rbp - -1168]
mov QWORD [rbp - 5632], QWORD [rbp - -1104]
mov QWORD [rbp - 5696], QWORD [rbp - -1040]
mov QWORD [rbp - 5760], QWORD [rbp - -976]
mov QWORD [rbp - 5824], QWORD [rbp - -912]
mov QWORD [rbp - 5888], QWORD [rbp - -848]
mov QWORD [rbp - 5952], QWORD [rbp - -784]
mov QWORD [rbp - 6016], QWORD [rbp - -720]
mov QWORD [rbp - 6080], QWORD [rbp - -656]
mov QWORD [rbp - 6144], QWORD [rbp - -592]
mov QWORD [rbp - 6208], QWORD [rbp - -528]
mov QWORD [rbp - 6272], QWORD [rbp - -464]
mov QWORD [rbp - 6336], QWORD [rbp - -400]
mov QWORD [rbp - 6400], QWORD [rbp - -336]
mov QWORD [rbp - 6464], QWORD [rbp - -272]
mov QWORD [rbp - 6528], QWORD [rbp - -208]
mov QWORD [rbp - 6592], QWORD [rbp - -144]
mov QWORD [rbp - 6656], QWORD [rbp - -80]
mov QWORD [rbp - 6720], QWORD [rbp - -16]
mov QWORD [rbp - 6784], QWORD [rbp - 48]
mov QWORD [rbp - 6848], QWORD [rbp - 112]
mov QWORD [rbp - 6912], QWORD [rbp - 176]
mov QWORD [rbp - 6976], QWORD [rbp - 240]
mov QWORD [rbp - 7040], QWORD [rbp - 304]
mov QWORD [rbp - 7104], QWORD [rbp - 368]
mov QWORD [rbp - 7168], QWORD [rbp - 432]
mov QWORD [rbp - 7232], QWORD [rbp - 496]
mov QWORD [rbp - 7296], QWORD [rbp - 560]
mov QWORD [rbp - 7360], QWORD [rbp - 624]
mov QWORD [rbp - 7424], QWORD [rbp - 688]
mov QWORD [rbp - 7488], QWORD [rbp - 752]
mov QWORD [rbp - 7552], QWORD [rbp - 816]
mov QWORD [rbp - 7616], QWORD [rbp - 880]
mov QWORD [rbp - 7680], QWORD [rbp - 944]
mov QWORD [rbp - 7744], QWORD [rbp - 1008]
mov QWORD [rbp - 7808], QWORD [rbp - 1072]
mov QWORD [rbp - 7872], QWORD [rbp - 1136]
mov QWORD [rbp - 7936], QWORD [rbp - 1200]
mov QWORD [rbp - 8000], QWORD [rbp - 1264]
mov QWORD [rbp - 8064], QWORD [rbp - 1328]
mov QWORD [rbp - 8128], QWORD [rbp - 1392]
mov QWORD [rbp - 8192], QWORD [rbp - 1456]
mov QWORD [rbp - 8256], QWORD [rbp - 1520]
mov QWORD [rbp - 8320], QWORD [rbp - 1584]
mov QWORD [rbp - 8384], QWORD [rbp - 1648]
mov QWORD [rbp - 8448], QWORD [rbp - 1712]
mov QWORD [rbp - 8512], QWORD [rbp - 1776]
mov QWORD [rbp - 8576], QWORD [rbp - 1840]
mov QWORD [rbp - 8640], QWORD [rbp - 1904]
mov QWORD [rbp - 8704], QWORD [rbp - 1968]
mov QWORD [rbp - 8768], QWORD [rbp - 2032]
mov QWORD [rbp - 8832], QWORD [rbp - 2096]
mov QWORD [rbp - 8896], QWORD [rbp - 2160]
mov QWORD [rbp - 8960], QWORD [rbp - 2224]
mov QWORD [rbp - 9024], QWORD [rbp - 2288]
mov QWORD [rbp - 9088], QWORD [rbp - 2352]
mov QWORD [rbp - 9152], QWORD [rbp - 2416]
mov QWORD [rbp - 9216], QWORD [rbp - 2480]
mov QWORD [rbp - 9280], QWORD [rbp - 2544]
mov QWORD [rbp - 9344], QWORD [rbp - 2608]
mov QWORD [rbp - 9408], QWORD [rbp - 2672]
mov QWORD [rbp - 9472], QWORD [rbp - 2736]
mov QWORD [rbp - 9536], QWORD [rbp - 2800]
mov QWORD [rbp - 9600], QWORD [rbp - 2864]
mov QWORD [rbp - 9664], QWORD [rbp - 2928]
mov QWORD [rbp - 9728], QWORD [rbp - 2992]
mov QWORD [rbp - 9792], QWORD [rbp - 3056]
mov QWORD [rbp - 9856], QWORD [rbp - 3120]
mov QWORD [rbp - 9920], QWORD [rbp - 3184]
mov QWORD [rbp - 9984], QWORD [rbp - 3248]
mov QWORD [rbp - 10048], QWORD [rbp - 3312]
mov QWORD [rbp - 10112], QWORD [rbp - 3376]
mov QWORD [rbp - 10176], QWORD [rbp - 3440]
mov QWORD [rbp - 10240], QWORD [rbp - 3504]
mov QWORD [rbp - 10304], QWORD [rbp - 3568]
mov QWORD [rbp - 10368], QWORD [rbp - 3632]
mov QWORD [rbp - 10432], QWORD [rbp - 3696]
mov QWORD [rbp - 10496], QWORD [rbp - 3760]
mov QWORD [rbp - 10560], QWORD [rbp - 3824]
mov QWORD [rbp - 10624], QWORD [rbp - 3888]
mov QWORD [rbp - 10688], QWORD [rbp - 3952]
mov QWORD [rbp - 10752], QWORD [rbp - 4016]
mov QWORD [rbp - 10816], QWORD [rbp - 4080]
mov QWORD [rbp - 10880], QWORD [rbp - 4144]
mov QWORD [rbp - 10944], QWORD [rbp - 4208]
mov QWORD [rbp - 11008], QWORD [rbp - 4272]
mov QWORD [rbp - 11072], QWORD [rbp - 4336]
mov QWORD [rbp - 11136], QWORD [rbp - 4400]
mov QWORD [rbp - 11200], QWORD [rbp - 4464]
mov QWORD [rbp - 11264], QWORD [rbp - 4528]
mov QWORD [rbp - 11328], QWORD [rbp - 4592]
mov QWORD [rbp - 11392], QWORD [rbp - 4656]
mov QWORD [rbp - 11456], QWORD [rbp - 4720]
mov QWORD [rbp - 11520], QWORD [rbp - 4784]
mov QWORD [rbp - 11584], QWORD [rbp - 4848]
mov QWORD [rbp - 11648], QWORD [rbp - 4912]
mov QWORD [rbp - 11712], QWORD [rbp - 4976]
mov QWORD [rbp - 11776], QWORD [rbp - 5040]
mov QWORD [rbp - 11840], QWORD [rbp - 5104]
mov QWORD [rbp - 11904], QWORD [rbp - 5168]
mov QWORD [rbp - 11968], QWORD [rbp - 5232]
mov QWORD [rbp - 12032], QWORD [rbp - 5296]
mov QWORD [rbp - 12096], QWORD [rbp - 5360]
mov QWORD [rbp - 12160], QWORD [rbp - 5424]
mov QWORD [rbp - 12224], QWORD [rbp - 5488]
mov QWORD [rbp - 12288], QWORD [rbp - 5552]
mov QWORD [rbp - 12352], QWORD [rbp - 5616]
mov QWORD [rbp - 12416], QWORD [rbp - 5680]
mov QWORD [rbp - 12480], QWORD [rbp - 5744]
mov QWORD [rbp - 12544], QWORD [rbp - 5808]
mov QWORD [rbp - 12608], QWORD [rbp - 5872]
mov QWORD [rbp - 12672], QWORD [rbp - 5936]
mov QWORD [rbp - 12736], QWORD [rbp - 6000]
mov QWORD [rbp - 12800], QWORD [rbp - 6064]
mov QWORD [rbp - 12864], QWORD [rbp - 6128]
mov QWORD [rbp - 12928], QWORD [rbp - 6192]
mov QWORD [rbp - 12992], QWORD [rbp - 6256]
mov QWORD [rbp - 13056], QWORD [rbp - 6320]
mov QWORD [rbp - 13120], QWORD [rbp - 6384]
mov QWORD [rbp - 13184], QWORD [rbp - 6448]
mov QWORD [rbp - 13248], QWORD [rbp - 6512]
mov QWORD [rbp - 13312], QWORD [rbp - 6576]
mov QWORD [rbp - 13376], QWORD [rbp - 6640]
mov QWORD [rbp - 13440], QWORD [rbp - 6704]
mov QWORD [rbp - 13504], QWORD [rbp - 6768]
mov QWORD [rbp - 13568], QWORD [rbp - 6832]
mov QWORD [rbp - 13632], QWORD [rbp - 6896]
mov QWORD [rbp - 13696], QWORD [rbp - 6960]
mov QWORD [rbp - 13760], QWORD [rbp - 7024]
mov QWORD [rbp - 13824], QWORD [rbp - 7088]
mov QWORD [rbp - 13888], QWORD [rbp - 7152]
mov QWORD [rbp - 13952], QWORD [rbp - 7216]
mov QWORD [rbp - 14016], QWORD [rbp - 7280]
mov QWORD [rbp - 14080], QWORD [rbp - 7344]
mov QWORD [rbp - 14144], QWORD [rbp - 7408]
mov QWORD [rbp - 14208], QWORD [rbp - 7472]
mov QWORD [rbp - 14272], QWORD [rbp - 7536]
mov QWORD [rbp - 14336], QWORD [rbp - 7600]
mov QWORD [rbp - 14400], QWORD [rbp - 7664]
mov QWORD [rbp - 14464], QWORD [rbp - 7728]
mov QWORD [rbp - 14528], QWORD [rbp - 7792]
mov QWORD [rbp - 14592], QWORD [rbp - 7856]
mov QWORD [rbp - 14656], QWORD [rbp - 7920]
mov QWORD [rbp - 14720], QWORD [rbp - 7984]
mov QWORD [rbp - 14784], QWORD [rbp - 8048]
mov QWORD [rbp - 14848], QWORD [rbp - 8112]
mov QWORD [rbp - 14912], QWORD [rbp - 8176]
mov QWORD [rbp - 14976], QWORD [rbp - 8240]
mov QWORD [rbp - 15040], QWORD [rbp - 8304]
mov QWORD [rbp - 15104], QWORD [rbp - 8368]
mov QWORD [rbp - 15168], QWORD [rbp - 8432]
mov QWORD [rbp - 15232], QWORD [rbp - 8496]
mov QWORD [rbp - 15296], QWORD [rbp - 8560]
mov QWORD [rbp - 15360], QWORD [rbp - 8624]
mov QWORD [rbp - 15424], QWORD [rbp - 8688]
mov QWORD [rbp - 15488], QWORD [rbp - 8752]
mov QWORD [rbp - 15552], QWORD [rbp - 8816]
mov QWORD [rbp - 15616], QWORD [rbp - 8880]
mov QWORD [rbp - 15680], QWORD [rbp - 8944]
mov QWORD [rbp - 15744], QWORD [rbp - 9008]
mov QWORD [rbp - 15808], QWORD [rbp - 9072]
mov QWORD [rbp - 15872], QWORD [rbp - 9136]
mov QWORD [rbp - 15936], QWORD [rbp - 9200]
mov QWORD [rbp - 16000], QWORD [rbp - 9264]
mov QWORD [rbp - 16064], QWORD [rbp - 9328]
mov QWORD [rbp - 16128], QWORD [rbp - 9392]
mov QWORD [rbp - 16192], QWORD [rbp - 9456]
mov QWORD [rbp - 16256], QWORD [rbp - 9520]
mov QWORD [rbp - 16320], QWORD [rbp - 9584]
mov QWORD [rbp - 16384], QWORD [rbp - 9648]
mov QWORD [rbp - 16448], QWORD [rbp - 9712]
mov QWORD [rbp - 16512], QWORD [rbp - 9776]
mov QWORD [rbp - 16576], QWORD [rbp - 9840]
mov QWORD [rbp - 16640], QWORD [rbp - 9904]
mov QWORD [rbp - 16704], QWORD [rbp - 9968]
mov QWORD [rbp - 16768], QWORD [rbp - 10032]
mov QWORD [rbp - 16832], QWORD [rbp - 10096]
mov QWORD [rbp - 16896], QWORD [rbp - 10160]
mov QWORD [rbp - 16960], QWORD [rbp - 10224]
mov QWORD [rbp - 17024], QWORD [rbp - 10288]
mov QWORD [rbp - 17088], QWORD [rbp - 10352]
mov QWORD [rbp - 17152], QWORD [rbp - 10416]
mov QWORD [rbp - 17216], QWORD [rbp - 10480]
mov QWORD [rbp - 17280], QWORD [rbp - 10544]
mov QWORD [rbp - 17344], QWORD [rbp - 10608]
mov QWORD [rbp - 17408], QWORD [rbp - 10672]
mov QWORD [rbp - 17472], QWORD [rbp - 10736]
mov QWORD [rbp - 17536], QWORD [rbp - 10800]
mov QWORD [rbp - 17600], QWORD [rbp - 10864]
mov QWORD [rbp - 17664], QWORD [rbp - 10928]
mov QWORD [rbp - 17728], QWORD [rbp - 10992]
mov QWORD [rbp - 17792], QWORD [rbp - 11056]
mov QWORD [rbp - 17856], QWORD [rbp - 11120]
mov QWORD [rbp - 17920], QWORD [rbp - 11184]
mov QWORD [rbp - 17984], QWORD [rbp - 11248]
mov QWORD [rbp - 18048], QWORD [rbp - 11312]
mov QWORD [rbp - 18112], QWORD [rbp - 11376]
mov QWORD [rbp - 18176], QWORD [rbp - 11440]
mov QWORD [rbp - 18240], QWORD [rbp - 11504]
mov QWORD [rbp - 18304], QWORD [rbp - 11568]
mov QWORD [rbp - 18368], QWORD [rbp - 11632]
mov QWORD [rbp - 18432], QWORD [rbp - 11696]
mov QWORD [rbp - 18496], QWORD [rbp - 11760]
mov QWORD [rbp - 18560], QWORD [rbp - 11824]
mov QWORD [rbp - 18624], QWORD [rbp - 11888]
mov QWORD [rbp - 18688], QWORD [rbp - 11952]
mov QWORD [rbp - 18752], QWORD [rbp - 12016]
mov QWORD [rbp - 18816], QWORD [rbp - 12080]
mov QWORD [rbp - 18880], QWORD [rbp - 12144]
mov QWORD [rbp - 18944], QWORD [rbp - 12208]
mov QWORD [rbp - 19008], QWORD [rbp - 12272]
mov QWORD [rbp - 19072], QWORD [rbp - 12336]
mov QWORD [rbp - 19136], QWORD [rbp - 12400]
mov QWORD [rbp - 19200], QWORD [rbp - 12464]
mov QWORD [rbp - 19264], QWORD [rbp - 12528]
mov QWORD [rbp - 19328], QWORD [rbp - 12592]
mov QWORD [rbp - 19392], QWORD [rbp - 12656]
mov QWORD [rbp - 19456], QWORD [rbp - 12720]
mov QWORD [rbp - 19520], QWORD [rbp - 12784]
mov QWORD [rbp - 19584], QWORD [rbp - 12848]
mov QWORD [rbp - 19648], QWORD [rbp - 12912]
mov QWORD [rbp - 19712], QWORD [rbp - 12976]
mov QWORD [rbp - 19776], QWORD [rbp - 13040]
mov QWORD [rbp - 19840], QWORD [rbp - 13104]
mov QWORD [rbp - 19904], QWORD [rbp - 13168]
mov QWORD [rbp - 19968], QWORD [rbp - 13232]
mov QWORD [rbp - 20032], QWORD [rbp - 13296]
mov QWORD [rbp - 20096], QWORD [rbp - 13360]
mov QWORD [rbp - 20160], QWORD [rbp - 13424]
mov QWORD [rbp - 20224], QWORD [rbp - 13488]
mov QWORD [rbp - 20288], QWORD [rbp - 13552]
mov QWORD [rbp - 20352], QWORD [rbp - 13616]
mov QWORD [rbp - 20416], QWORD [rbp - 13680]
mov QWORD [rbp - 20480], QWORD [rbp - 13744]
mov QWORD [rbp - 20544], QWORD [rbp - 13808]
mov QWORD [rbp - 20608], QWORD [rbp - 13872]
mov QWORD [rbp - 20672], QWORD [rbp - 13936]
mov QWORD [rbp - 20736], QWORD [rbp - 14000]
mov QWORD [rbp - 20800], QWORD [rbp - 14064]
mov QWORD [rbp - 20864], QWORD [rbp - 14128]
mov QWORD [rbp - 20928], QWORD [rbp - 14192]
mov QWORD [rbp - 20992], QWORD [rbp - 14256]
mov QWORD [rbp - 21056], QWORD [rbp - 14320]
mov QWORD [rbp - 21120], QWORD [rbp - 14384]
mov QWORD [rbp - 21184], QWORD [rbp - 14448]
mov QWORD [rbp - 21248], QWORD [rbp - 14512]
mov QWORD [rbp - 21312], QWORD [rbp - 14576]
mov QWORD [rbp - 21376], QWORD [rbp - 14640]
mov QWORD [rbp - 21440], QWORD [rbp - 14704]
mov QWORD [rbp - 21504], QWORD [rbp - 14768]
mov QWORD [rbp - 21568], QWORD [rbp - 14832]
mov QWORD [rbp - 21632], QWORD [rbp - 14896]
mov QWORD [rbp - 21696], QWORD [rbp - 14960]
mov QWORD [rbp - 21760], QWORD [rbp - 15024]
mov QWORD [rbp - 21824], QWORD [rbp - 15088]
mov QWORD [rbp - 21888], QWORD [rbp - 15152]
mov QWORD [rbp - 21952], QWORD [rbp - 15216]
mov QWORD [rbp - 22016], QWORD [rbp - 15280]
mov QWORD [rbp - 22080], QWORD [rbp - 15344]
mov QWORD [rbp - 22144], QWORD [rbp - 15408]
mov QWORD [rbp - 22208], QWORD [rbp - 15472]
mov QWORD [rbp - 22272], QWORD [rbp - 15536]
mov QWORD [rbp - 22336], QWORD [rbp - 15600]
mov QWORD [rbp - 22400], QWORD [rbp - 15664]
mov QWORD [rbp - 22464], QWORD [rbp - 15728]
mov QWORD [rbp - 22528], QWORD [rbp - 15792]
mov QWORD [rbp - 22592], QWORD [rbp - 15856]
mov QWORD [rbp - 22656], QWORD [rbp - 15920]
mov QWORD [rbp - 22720], QWORD [rbp - 15984]
mov QWORD [rbp - 22784], QWORD [rbp - 16048]
mov QWORD [rbp - 22848], QWORD [rbp - 16112]
mov QWORD [rbp - 22912], QWORD [rbp - 16176]
mov QWORD [rbp - 22976], QWORD [rbp - 16240]
mov QWORD [rbp - 23040], QWORD [rbp - 16304]
mov QWORD [rbp - 23104], QWORD [rbp - 16368]
mov QWORD [rbp - 23168], QWORD [rbp - 16432]
mov QWORD [rbp - 23232], QWORD [rbp - 16496]
mov QWORD [rbp - 23296], QWORD [rbp - 16560]
mov QWORD [rbp - 23360], QWORD [rbp - 16624]
mov QWORD [rbp - 23424], QWORD [rbp - 16688]
mov QWORD [rbp - 23488], QWORD [rbp - 16752]
mov QWORD [rbp - 23552], QWORD [rbp - 16816]
mov QWORD [rbp - 23616], QWORD [rbp - 16880]
mov QWORD [rbp - 23680], QWORD [rbp - 16944]
mov QWORD [rbp - 23744], QWORD [rbp - 17008]
mov QWORD [rbp - 23808], QWORD [rbp - 17072]
mov QWORD [rbp - 23872], QWORD [rbp - 17136]
mov QWORD [rbp - 23936], QWORD [rbp - 17200]
mov QWORD [rbp - 24000], QWORD [rbp - 17264]
mov QWORD [rbp - 24064], QWORD [rbp - 17328]
mov QWORD [rbp - 24128], QWORD [rbp - 17392]
mov QWORD [rbp - 24192], QWORD [rbp - 17456]
mov QWORD [rbp - 24256], QWORD [rbp - 17520]
mov QWORD [rbp - 24320], QWORD [rbp - 17584]
mov QWORD [rbp - 24384], QWORD [rbp - 17648]
mov QWORD [rbp - 24448], QWORD [rbp - 17712]
mov QWORD [rbp - 24512], QWORD [rbp - 17776]
mov QWORD [rbp - 24576], QWORD [rbp - 17840]
mov QWORD [rbp - 24640], QWORD [rbp - 17904]
mov QWORD [rbp - 24704], QWORD [rbp - 17968]
mov QWORD [rbp - 24768], QWORD [rbp - 18032]
mov QWORD [rbp - 24832], QWORD [rbp - 18096]
mov QWORD [rbp - 24896], QWORD [rbp - 18160]
mov QWORD [rbp - 24960], QWORD [rbp - 18224]
mov QWORD [rbp - 25024], QWORD [rbp - 18288]
mov QWORD [rbp - 25088], QWORD [rbp - 18352]
mov QWORD [rbp - 25152], QWORD [rbp - 18416]
mov QWORD [rbp - 25216], QWORD [rbp - 18480]
mov QWORD [rbp - 25280], QWORD [rbp - 18544]
mov QWORD [rbp - 25344], QWORD [rbp - 18608]
mov QWORD [rbp - 25408], QWORD [rbp - 18672]
mov QWORD [rbp - 25472], QWORD [rbp - 18736]
mov QWORD [rbp - 25536], QWORD [rbp - 18800]
mov QWORD [rbp - 25600], QWORD [rbp - 18864]
mov QWORD [rbp - 25664], QWORD [rbp - 18928]
mov QWORD [rbp - 25728], QWORD [rbp - 18992]
mov QWORD [rbp - 25792], QWORD [rbp - 19056]
mov QWORD [rbp - 25856], QWORD [rbp - 19120]
mov QWORD [rbp - 25920], QWORD [rbp - 19184]
mov QWORD [rbp - 25984], QWORD [rbp - 19248]
mov QWORD [rbp - 26048], QWORD [rbp - 19312]
mov QWORD [rbp - 26112], QWORD [rbp - 19376]
mov QWORD [rbp - 26176], QWORD [rbp - 19440]
mov QWORD [rbp - 26240], QWORD [rbp - 19504]
mov QWORD [rbp - 26304], QWORD [rbp - 19568]
mov QWORD [rbp - 26368], QWORD [rbp - 19632]
mov QWORD [rbp - 26432], QWORD [rbp - 19696]
mov QWORD [rbp - 26496], QWORD [rbp - 19760]
mov QWORD [rbp - 26560], QWORD [rbp - 19824]
mov QWORD [rbp - 26624], QWORD [rbp - 19888]
mov QWORD [rbp - 26688], QWORD [rbp - 19952]
mov QWORD [rbp - 26752], QWORD [rbp - 20016]
mov QWORD [rbp - 26816], QWORD [rbp - 20080]
mov QWORD [rbp - 26880], QWORD [rbp - 20144]
mov QWORD [rbp - 26944], QWORD [rbp - 20208]
mov QWORD [rbp - 27008], QWORD [rbp - 20272]
mov QWORD [rbp - 27072], QWORD [rbp - 20336]
mov QWORD [rbp - 27136], QWORD [rbp - 20400]
mov QWORD [rbp - 27200], QWORD [rbp - 20464]
mov QWORD [rbp - 27264], QWORD [rbp - 20528]
mov QWORD [rbp - 27328], QWORD [rbp - 20592]
mov QWORD [rbp - 27392], QWORD [rbp - 20656]
mov QWORD [rbp - 27456], QWORD [rbp - 20720]
mov QWORD [rbp - 27520], QWORD [rbp - 20784]
mov QWORD [rbp - 27584], QWORD [rbp - 20848]
mov QWORD [rbp - 27648], QWORD [rbp - 20912]
mov QWORD [rbp - 27712], QWORD [rbp - 20976]
mov QWORD [rbp - 27776], QWORD [rbp - 21040]
mov QWORD [rbp - 27840], QWORD [rbp - 21104]
mov QWORD [rbp - 27904], QWORD [rbp - 21168]
mov QWORD [rbp - 27968], QWORD [rbp - 21232]
mov QWORD [rbp - 28032], QWORD [rbp - 21296]
mov QWORD [rbp - 28096], QWORD [rbp - 21360]
mov QWORD [rbp - 28160], QWORD [rbp - 21424]
mov QWORD [rbp - 28224], QWORD [rbp - 21488]
mov QWORD [rbp - 28288], QWORD [rbp - 21552]
mov QWORD [rbp - 28352], QWORD [rbp - 21616]
mov QWORD [rbp - 28416], QWORD [rbp - 21680]
mov QWORD [rbp - 28480], QWORD [rbp - 21744]
mov QWORD [rbp - 28544], QWORD [rbp - 21808]
mov QWORD [rbp - 28608], QWORD [rbp - 21872]
mov QWORD [rbp - 28672], QWORD [rbp - 21936]
mov QWORD [rbp - 28736], QWORD [rbp - 22000]
mov QWORD [rbp - 28800], QWORD [rbp - 22064]
mov QWORD [rbp - 28864], QWORD [rbp - 22128]
mov QWORD [rbp - 28928], QWORD [rbp - 22192]
mov QWORD [rbp - 28992], QWORD [rbp - 22256]
mov QWORD [rbp - 29056], QWORD [rbp - 22320]
mov QWORD [rbp - 29120], QWORD [rbp - 22384]
mov QWORD [rbp - 29184], QWORD [rbp - 22448]
mov QWORD [rbp - 29248], QWORD [rbp - 22512]
mov QWORD [rbp - 29312], QWORD [rbp - 22576]
mov QWORD [rbp - 29376], QWORD [rbp - 22640]
mov QWORD [rbp - 29440], QWORD [rbp - 22704]
mov QWORD [rbp - 29504], QWORD [rbp - 22768]
mov QWORD [rbp - 29568], QWORD [rbp - 22832]
mov QWORD [rbp - 29632], QWORD [rbp - 22896]
mov QWORD [rbp - 29696], QWORD [rbp - 22960]
mov QWORD [rbp - 29760], QWORD [rbp - 23024]
mov QWORD [rbp - 29824], QWORD [rbp - 23088]
mov QWORD [rbp - 29888], QWORD [rbp - 23152]
mov QWORD [rbp - 29952], QWORD [rbp - 23216]
mov QWORD [rbp - 30016], QWORD [rbp - 23280]
mov QWORD [rbp - 30080], QWORD [rbp - 23344]
mov QWORD [rbp - 30144], QWORD [rbp - 23408]
mov QWORD [rbp - 30208], QWORD [rbp - 23472]
mov QWORD [rbp - 30272], QWORD [rbp - 23536]
mov QWORD [rbp - 30336], QWORD [rbp - 23600]
mov QWORD [rbp - 30400], QWORD [rbp - 23664]
mov QWORD [rbp - 30464], QWORD [rbp - 23728]
mov QWORD [rbp - 30528], QWORD [rbp - 23792]
mov QWORD [rbp - 30592], QWORD [rbp - 23856]
mov QWORD [rbp - 30656], QWORD [rbp - 23920]
mov QWORD [rbp - 30720], QWORD [rbp - 23984]
mov QWORD [rbp - 30784], QWORD [rbp - 24048]
mov QWORD [rbp - 30848], QWORD [rbp - 24112]
mov QWORD [rbp - 30912], QWORD [rbp - 24176]
mov QWORD [rbp - 30976], QWORD [rbp - 24240]
mov QWORD [rbp - 31040], QWORD [rbp - 24304]
mov QWORD [rbp - 31104], QWORD [rbp - 24368]
mov QWORD [rbp - 31168], QWORD [rbp - 24432]
mov QWORD [rbp - 31232], QWORD [rbp - 24496]
mov QWORD [rbp - 31296], QWORD [rbp - 24560]
mov QWORD [rbp - 31360], QWORD [rbp - 24624]
mov QWORD [rbp - 31424], QWORD [rbp - 24688]
mov QWORD [rbp - 31488], QWORD [rbp - 24752]
mov QWORD [rbp - 31552], QWORD [rbp - 24816]
mov QWORD [rbp - 31616], QWORD [rbp - 24880]
mov QWORD [rbp - 31680], QWORD [rbp - 24944]
mov QWORD [rbp - 31744], QWORD [rbp - 25008]
mov QWORD [rbp - 31808], QWORD [rbp - 25072]
mov QWORD [rbp - 31872], QWORD [rbp - 25136]
mov QWORD [rbp - 31936], QWORD [rbp - 25200]
mov QWORD [rbp - 32000], QWORD [rbp - 25264]
mov QWORD [rbp - 32064], QWORD [rbp - 25328]
mov QWORD [rbp - 32128], QWORD [rbp - 25392]
mov QWORD [rbp - 32192], QWORD [rbp - 25456]
mov QWORD [rbp - 32256], QWORD [rbp - 25520]
mov QWORD [rbp - 32320], QWORD [rbp - 25584]
mov QWORD [rbp - 32384], QWORD [rbp - 25648]
mov QWORD [rbp - 32448], QWORD [rbp - 25712]
mov QWORD [rbp - 32512], QWORD [rbp - 25776]
mov QWORD [rbp - 32576], QWORD [rbp - 25840]
mov QWORD [rbp - 32640], QWORD [rbp - 25904]
mov QWORD [rbp - 32704], QWORD [rbp - 25968]
mov QWORD [rbp - 32768], QWORD [rbp - 26032]
mov QWORD [rbp - 32832], QWORD [rbp - 26096]
mov QWORD [rbp - 32896], QWORD [rbp - 26160]
mov QWORD [rbp - 32960], QWORD [rbp - 26224]
mov QWORD [rbp - 33024], QWORD [rbp - 26288]
mov QWORD [rbp - 33088], QWORD [rbp - 26352]
mov QWORD [rbp - 33152], QWORD [rbp - 26416]
mov QWORD [rbp - 33216], QWORD [rbp - 26480]
mov QWORD [rbp - 33280], QWORD [rbp - 26544]
mov QWORD [rbp - 33344], QWORD [rbp - 26608]
mov QWORD [rbp - 33408], QWORD [rbp - 26672]
mov QWORD [rbp - 33472], QWORD [rbp - 26736]
mov QWORD [rbp - 33536], QWORD [rbp - 26800]
mov QWORD [rbp - 33600], QWORD [rbp - 26864]
mov QWORD [rbp - 33664], QWORD [rbp - 26928]
mov QWORD [rbp - 33728], QWORD [rbp - 26992]
mov QWORD [rbp - 33792], QWORD [rbp - 27056]
mov QWORD [rbp - 33856], QWORD [rbp - 27120]
mov QWORD [rbp - 33920], QWORD [rbp - 27184]
mov QWORD [rbp - 33984], QWORD [rbp - 27248]
mov QWORD [rbp - 34048], QWORD [rbp - 27312]
mov QWORD [rbp - 34112], QWORD [rbp - 27376]
mov QWORD [rbp - 34176], QWORD [rbp - 27440]
mov QWORD [rbp - 34240], QWORD [rbp - 27504]
mov QWORD [rbp - 34304], QWORD [rbp - 27568]
mov QWORD [rbp - 34368], QWORD [rbp - 27632]
mov QWORD [rbp - 34432], QWORD [rbp - 27696]
mov QWORD [rbp - 34496], QWORD [rbp - 27760]
mov QWORD [rbp - 34560], QWORD [rbp - 27824]
mov QWORD [rbp - 34624], QWORD [rbp - 27888]
mov QWORD [rbp - 34688], QWORD [rbp - 27952]
mov QWORD [rbp - 34752], QWORD [rbp - 28016]
mov QWORD [rbp - 34816], QWORD [rbp - 28080]
mov QWORD [rbp - 34880], QWORD [rbp - 28144]
mov QWORD [rbp - 34944], QWORD [rbp - 28208]
mov QWORD [rbp - 35008], QWORD [rbp - 28272]
mov QWORD [rbp - 35072], QWORD [rbp - 28336]
mov QWORD [rbp - 35136], QWORD [rbp - 28400]
mov QWORD [rbp - 35200], QWORD [rbp - 28464]
mov QWORD [rbp - 35264], QWORD [rbp - 28528]
mov QWORD [rbp - 35328], QWORD [rbp - 28592]
mov QWORD [rbp - 35392], QWORD [rbp - 28656]
mov QWORD [rbp - 35456], QWORD [rbp - 28720]
mov QWORD [rbp - 35520], QWORD [rbp - 28784]
mov QWORD [rbp - 35584], QWORD [rbp - 28848]
mov QWORD [rbp - 35648], QWORD [rbp - 28912]
mov QWORD [rbp - 35712], QWORD [rbp - 28976]
mov QWORD [rbp - 35776], QWORD [rbp - 29040]
mov QWORD [rbp - 35840], QWORD [rbp - 29104]
mov QWORD [rbp - 35904], QWORD [rbp - 29168]
mov QWORD [rbp - 35968], QWORD [rbp - 29232]
mov QWORD [rbp - 36032], QWORD [rbp - 29296]
mov QWORD [rbp - 36096], QWORD [rbp - 29360]
mov QWORD [rbp - 36160], QWORD [rbp - 29424]
mov QWORD [rbp - 36224], QWORD [rbp - 29488]
mov QWORD [rbp - 36288], QWORD [rbp - 29552]
mov QWORD [rbp - 36352], QWORD [rbp - 29616]
mov QWORD [rbp - 36416], QWORD [rbp - 29680]
mov QWORD [rbp - 36480], QWORD [rbp - 29744]
mov QWORD [rbp - 36544], QWORD [rbp - 29808]
mov QWORD [rbp - 36608], QWORD [rbp - 29872]
mov QWORD [rbp - 36672], QWORD [rbp - 29936]
mov QWORD [rbp - 36736], QWORD [rbp - 30000]
mov QWORD [rbp - 36800], QWORD [rbp - 30064]
mov QWORD [rbp - 36864], QWORD [rbp - 30128]
mov QWORD [rbp - 36928], QWORD [rbp - 30192]
mov QWORD [rbp - 36992], QWORD [rbp - 30256]
mov QWORD [rbp - 37056], QWORD [rbp - 30320]
mov QWORD [rbp - 37120], QWORD [rbp - 30384]
mov QWORD [rbp - 37184], QWORD [rbp - 30448]
mov QWORD [rbp - 37248], QWORD [rbp - 30512]
mov QWORD [rbp - 37312], QWORD [rbp - 30576]
mov QWORD [rbp - 37376], QWORD [rbp - 30640]
mov QWORD [rbp - 37440], QWORD [rbp - 30704]
mov QWORD [rbp - 37504], QWORD [rbp - 30768]
mov QWORD [rbp - 37568], QWORD [rbp - 30832]
mov QWORD [rbp - 37632], QWORD [rbp - 30896]
mov QWORD [rbp - 37696], QWORD [rbp - 30960]
mov QWORD [rbp - 37760], QWORD [rbp - 31024]
mov QWORD [rbp - 37824], QWORD [rbp - 31088]
mov QWORD [rbp - 37888], QWORD [rbp - 31152]
mov QWORD [rbp - 37952], QWORD [rbp - 31216]
mov QWORD [rbp - 38016], QWORD [rbp - 31280]
mov QWORD [rbp - 38080], QWORD [rbp - 31344]
mov QWORD [rbp - 38144], QWORD [rbp - 31408]
mov QWORD [rbp - 38208], QWORD [rbp - 31472]
mov QWORD [rbp - 38272], QWORD [rbp - 31536]
mov QWORD [rbp - 38336], QWORD [rbp - 31600]
mov QWORD [rbp - 38400], QWORD [rbp - 31664]
mov QWORD [rbp - 38464], QWORD [rbp - 31728]
mov QWORD [rbp - 38528], QWORD [rbp - 31792]
mov QWORD [rbp - 38592], QWORD [rbp - 31856]
mov QWORD [rbp - 38656], QWORD [rbp - 31920]
mov QWORD [rbp - 38720], QWORD [rbp - 31984]
mov QWORD [rbp - 38784], QWORD [rbp - 32048]
mov QWORD [rbp - 38848], QWORD [rbp - 32112]
mov QWORD [rbp - 38912], QWORD [rbp - 32176]
mov QWORD [rbp - 38976], QWORD [rbp - 32240]
mov QWORD [rbp - 39040], QWORD [rbp - 32304]
mov QWORD [rbp - 39104], QWORD [rbp - 32368]
mov QWORD [rbp - 39168], QWORD [rbp - 32432]
mov QWORD [rbp - 39232], QWORD [rbp - 32496]
mov QWORD [rbp - 39296], QWORD [rbp - 32560]
mov QWORD [rbp - 39360], QWORD [rbp - 32624]
mov QWORD [rbp - 39424], QWORD [rbp - 32688]
mov QWORD [rbp - 39488], QWORD [rbp - 32752]
mov QWORD [rbp - 39552], QWORD [rbp - 32816]
mov QWORD [rbp - 39616], QWORD [rbp - 32880]
mov QWORD [rbp - 39680], QWORD [rbp - 32944]
mov QWORD [rbp - 39744], QWORD [rbp - 33008]
mov QWORD [rbp - 39808], QWORD [rbp - 33072]
mov QWORD [rbp - 39872], QWORD [rbp - 33136]
mov QWORD [rbp - 39936], QWORD [rbp - 33200]
mov QWORD [rbp - 40000], QWORD [rbp - 33264]
mov QWORD [rbp - 40064], QWORD [rbp - 33328]
mov QWORD [rbp - 40128], QWORD [rbp - 33392]
mov QWORD [rbp - 40192], QWORD [rbp - 33456]
mov QWORD [rbp - 40256], QWORD [rbp - 33520]
mov QWORD [rbp - 40320], QWORD [rbp - 33584]
mov QWORD [rbp - 40384], QWORD [rbp - 33648]
mov QWORD [rbp - 40448], QWORD [rbp - 33712]
mov QWORD [rbp - 40512], QWORD [rbp - 33776]
mov QWORD [rbp - 40576], QWORD [rbp - 33840]
mov QWORD [rbp - 40640], QWORD [rbp - 33904]
mov QWORD [rbp - 40704], QWORD [rbp - 33968]
mov QWORD [rbp - 40768], QWORD [rbp - 34032]
mov QWORD [rbp - 40832], QWORD [rbp - 34096]
mov QWORD [rbp - 40896], QWORD [rbp - 34160]
mov QWORD [rbp - 40960], QWORD [rbp - 34224]
mov QWORD [rbp - 41024], QWORD [rbp - 34288]
mov QWORD [rbp - 41088], QWORD [rbp - 34352]
mov QWORD [rbp - 41152], QWORD [rbp - 34416]
mov QWORD [rbp - 41216], QWORD [rbp - 34480]
mov QWORD [rbp - 41280], QWORD [rbp - 34544]
mov QWORD [rbp - 41344], QWORD [rbp - 34608]
mov QWORD [rbp - 41408], QWORD [rbp - 34672]
mov QWORD [rbp - 41472], QWORD [rbp - 34736]
mov QWORD [rbp - 41536], QWORD [rbp - 34800]
mov QWORD [rbp - 41600], QWORD [rbp - 34864]
mov QWORD [rbp - 41664], QWORD [rbp - 34928]
mov QWORD [rbp - 41728], QWORD [rbp - 34992]
mov QWORD [rbp - 41792], QWORD [rbp - 35056]
mov QWORD [rbp - 41856], QWORD [rbp - 35120]
mov QWORD [rbp - 41920], QWORD [rbp - 35184]
mov QWORD [rbp - 41984], QWORD [rbp - 35248]
mov QWORD [rbp - 42048], QWORD [rbp - 35312]
mov QWORD [rbp - 42112], QWORD [rbp - 35376]
mov QWORD [rbp - 42176], QWORD [rbp - 35440]
mov QWORD [rbp - 42240], QWORD [rbp - 35504]
mov QWORD [rbp - 42304], QWORD [rbp - 35568]
mov QWORD [rbp - 42368], QWORD [rbp - 35632]
mov QWORD [rbp - 42432], QWORD [rbp - 35696]
mov QWORD [rbp - 42496], QWORD [rbp - 35760]
mov QWORD [rbp - 42560], QWORD [rbp - 35824]
mov QWORD [rbp - 42624], QWORD [rbp - 35888]
mov QWORD [rbp - 42688], QWORD [rbp - 35952]
mov QWORD [rbp - 42752], QWORD [rbp - 36016]
mov QWORD [rbp - 42816], QWORD [rbp - 36080]
mov QWORD [rbp - 42880], QWORD [rbp - 36144]
mov QWORD [rbp - 42944], QWORD [rbp - 36208]
mov QWORD [rbp - 43008], QWORD [rbp - 36272]
mov QWORD [rbp - 43072], QWORD [rbp - 36336]
mov QWORD [rbp - 43136], QWORD [rbp - 36400]
mov QWORD [rbp - 43200], QWORD [rbp - 36464]
mov QWORD [rbp - 43264], QWORD [rbp - 36528]
mov QWORD [rbp - 43328], QWORD [rbp - 36592]
mov QWORD [rbp - 43392], QWORD [rbp - 36656]
mov QWORD [rbp - 43456], QWORD [rbp - 36720]
mov QWORD [rbp - 43520], QWORD [rbp - 36784]
mov QWORD [rbp - 43584], QWORD [rbp - 36848]
mov QWORD [rbp - 43648], QWORD [rbp - 36912]
mov QWORD [rbp - 43712], QWORD [rbp - 36976]
mov QWORD [rbp - 43776], QWORD [rbp - 37040]
mov QWORD [rbp - 43840], QWORD [rbp - 37104]
mov QWORD [rbp - 43904], QWORD [rbp - 37168]
mov QWORD [rbp - 43968], QWORD [rbp - 37232]
mov QWORD [rbp - 44032], QWORD [rbp - 37296]
mov QWORD [rbp - 44096], QWORD [rbp - 37360]
mov QWORD [rbp - 44160], QWORD [rbp - 37424]
mov QWORD [rbp - 44224], QWORD [rbp - 37488]
mov QWORD [rbp - 44288], QWORD [rbp - 37552]
mov QWORD [rbp - 44352], QWORD [rbp - 37616]
mov QWORD [rbp - 44416], QWORD [rbp - 37680]
mov QWORD [rbp - 44480], QWORD [rbp - 37744]
mov QWORD [rbp - 44544], QWORD [rbp - 37808]
mov QWORD [rbp - 44608], QWORD [rbp - 37872]
mov QWORD [rbp - 44672], QWORD [rbp - 37936]
mov QWORD [rbp - 44736], QWORD [rbp - 38000]
mov QWORD [rbp - 44800], QWORD [rbp - 38064]
mov QWORD [rbp - 44864], QWORD [rbp - 38128]
mov QWORD [rbp - 44928], QWORD [rbp - 38192]
mov QWORD [rbp - 44992], QWORD [rbp - 38256]
mov QWORD [rbp - 45056], QWORD [rbp - 38320]
mov QWORD [rbp - 45120], QWORD [rbp - 38384]
mov QWORD [rbp - 45184], QWORD [rbp - 38448]
mov QWORD [rbp - 45248], QWORD [rbp - 38512]
mov QWORD [rbp - 45312], QWORD [rbp - 38576]
mov QWORD [rbp - 45376], QWORD [rbp - 38640]
mov QWORD [rbp - 45440], QWORD [rbp - 38704]
mov QWORD [rbp - 45504], QWORD [rbp - 38768]
mov QWORD [rbp - 45568], QWORD [rbp - 38832]
mov QWORD [rbp - 45632], QWORD [rbp - 38896]
mov QWORD [rbp - 45696], QWORD [rbp - 38960]
mov QWORD [rbp - 45760], QWORD [rbp - 39024]
mov QWORD [rbp - 45824], QWORD [rbp - 39088]
mov QWORD [rbp - 45888], QWORD [rbp - 39152]
mov QWORD [rbp - 45952], QWORD [rbp - 39216]
mov QWORD [rbp - 46016], QWORD [rbp - 39280]
mov QWORD [rbp - 46080], QWORD [rbp - 39344]
mov QWORD [rbp - 46144], QWORD [rbp - 39408]
mov QWORD [rbp - 46208], QWORD [rbp - 39472]
mov QWORD [rbp - 46272], QWORD [rbp - 39536]
mov QWORD [rbp - 46336], QWORD [rbp - 39600]
mov QWORD [rbp - 46400], QWORD [rbp - 39664]
mov QWORD [rbp - 46464], QWORD [rbp - 39728]
mov QWORD [rbp - 46528], QWORD [rbp - 39792]
mov QWORD [rbp - 46592], QWORD [rbp - 39856]
mov QWORD [rbp - 46656], QWORD [rbp - 39920]
mov QWORD [rbp - 46720], QWORD [rbp - 39984]
mov QWORD [rbp - 46784], QWORD [rbp - 40048]
mov QWORD [rbp - 46848], QWORD [rbp - 40112]
mov QWORD [rbp - 46912], QWORD [rbp - 40176]
mov QWORD [rbp - 46976], QWORD [rbp - 40240]
mov QWORD [rbp - 47040], QWORD [rbp - 40304]
mov QWORD [rbp - 47104], QWORD [rbp - 40368]
mov QWORD [rbp - 47168], QWORD [rbp - 40432]
mov QWORD [rbp - 47232], QWORD [rbp - 40496]
mov QWORD [rbp - 47296], QWORD [rbp - 40560]
mov QWORD [rbp - 47360], QWORD [rbp - 40624]
mov QWORD [rbp - 47424], QWORD [rbp - 40688]
mov QWORD [rbp - 47488], QWORD [rbp - 40752]
mov QWORD [rbp - 47552], QWORD [rbp - 40816]
mov QWORD [rbp - 47616], QWORD [rbp - 40880]
mov QWORD [rbp - 47680], QWORD [rbp - 40944]
mov QWORD [rbp - 47744], QWORD [rbp - 41008]
mov QWORD [rbp - 47808], QWORD [rbp - 41072]
mov QWORD [rbp - 47872], QWORD [rbp - 41136]
mov QWORD [rbp - 47936], QWORD [rbp - 41200]
mov QWORD [rbp - 48000], QWORD [rbp - 41264]
mov QWORD [rbp - 48064], QWORD [rbp - 41328]
mov QWORD [rbp - 48128], QWORD [rbp - 41392]
mov QWORD [rbp - 48192], QWORD [rbp - 41456]
mov QWORD [rbp - 48256], QWORD [rbp - 41520]
mov QWORD [rbp - 48320], QWORD [rbp - 41584]
mov QWORD [rbp - 48384], QWORD [rbp - 41648]
mov QWORD [rbp - 48448], QWORD [rbp - 41712]
mov QWORD [rbp - 48512], QWORD [rbp - 41776]
mov QWORD [rbp - 48576], QWORD [rbp - 41840]
mov QWORD [rbp - 48640], QWORD [rbp - 41904]
mov QWORD [rbp - 48704], QWORD [rbp - 41968]
mov QWORD [rbp - 48768], QWORD [rbp - 42032]
mov QWORD [rbp - 48832], QWORD [rbp - 42096]
mov QWORD [rbp - 48896], QWORD [rbp - 42160]
mov QWORD [rbp - 48960], QWORD [rbp - 42224]
mov QWORD [rbp - 49024], QWORD [rbp - 42288]
mov QWORD [rbp - 49088], QWORD [rbp - 42352]
mov QWORD [rbp - 49152], QWORD [rbp - 42416]
mov QWORD [rbp - 49216], QWORD [rbp - 42480]
mov QWORD [rbp - 49280], QWORD [rbp - 42544]
mov QWORD [rbp - 49344], QWORD [rbp - 42608]
mov QWORD [rbp - 49408], QWORD [rbp - 42672]
mov QWORD [rbp - 49472], QWORD [rbp - 42736]
mov QWORD [rbp - 49536], QWORD [rbp - 42800]
mov QWORD [rbp - 49600], QWORD [rbp - 42864]
mov QWORD [rbp - 49664], QWORD [rbp - 42928]
mov QWORD [rbp - 49728], QWORD [rbp - 42992]
mov QWORD [rbp - 49792], QWORD [rbp - 43056]
mov QWORD [rbp - 49856], QWORD [rbp - 43120]
mov QWORD [rbp - 49920], QWORD [rbp - 43184]
mov QWORD [rbp - 49984], QWORD [rbp - 43248]
mov QWORD [rbp - 50048], QWORD [rbp - 43312]
mov QWORD [rbp - 50112], QWORD [rbp - 43376]
mov QWORD [rbp - 50176], QWORD [rbp - 43440]
mov QWORD [rbp - 50240], QWORD [rbp - 43504]
mov QWORD [rbp - 50304], QWORD [rbp - 43568]
mov QWORD [rbp - 50368], QWORD [rbp - 43632]
mov QWORD [rbp - 50432], QWORD [rbp - 43696]
mov QWORD [rbp - 50496], QWORD [rbp - 43760]
mov QWORD [rbp - 50560], QWORD [rbp - 43824]
mov QWORD [rbp - 50624], QWORD [rbp - 43888]
mov QWORD [rbp - 50688], QWORD [rbp - 43952]
mov QWORD [rbp - 50752], QWORD [rbp - 44016]
mov QWORD [rbp - 50816], QWORD [rbp - 44080]
mov QWORD [rbp - 50880], QWORD [rbp - 44144]
mov QWORD [rbp - 50944], QWORD [rbp - 44208]
mov QWORD [rbp - 51008], QWORD [rbp - 44272]
mov QWORD [rbp - 51072], QWORD [rbp - 44336]
mov QWORD [rbp - 51136], QWORD [rbp - 44400]
mov QWORD [rbp - 51200], QWORD [rbp - 44464]
mov QWORD [rbp - 51264], QWORD [rbp - 44528]
mov QWORD [rbp - 51328], QWORD [rbp - 44592]
mov QWORD [rbp - 51392], QWORD [rbp - 44656]
mov QWORD [rbp - 51456], QWORD [rbp - 44720]
mov QWORD [rbp - 51520], QWORD [rbp - 44784]
mov QWORD [rbp - 51584], QWORD [rbp - 44848]
mov QWORD [rbp - 51648], QWORD [rbp - 44912]
mov QWORD [rbp - 51712], QWORD [rbp - 44976]
mov QWORD [rbp - 51776], QWORD [rbp - 45040]
mov QWORD [rbp - 51840], QWORD [rbp - 45104]
mov QWORD [rbp - 51904], QWORD [rbp - 45168]
mov QWORD [rbp - 51968], QWORD [rbp - 45232]
mov QWORD [rbp - 52032], QWORD [rbp - 45296]
mov QWORD [rbp - 52096], QWORD [rbp - 45360]
mov QWORD [rbp - 52160], QWORD [rbp - 45424]
mov QWORD [rbp - 52224], QWORD [rbp - 45488]
mov QWORD [rbp - 52288], QWORD [rbp - 45552]
mov QWORD [rbp - 52352], QWORD [rbp - 45616]
mov QWORD [rbp - 52416], QWORD [rbp - 45680]
mov QWORD [rbp - 52480], QWORD [rbp - 45744]
mov QWORD [rbp - 52544], QWORD [rbp - 45808]
mov QWORD [rbp - 52608], QWORD [rbp - 45872]
mov QWORD [rbp - 52672], QWORD [rbp - 45936]
mov QWORD [rbp - 52736], QWORD [rbp - 46000]
mov QWORD [rbp - 52800], QWORD [rbp - 46064]
mov QWORD [rbp - 52864], QWORD [rbp - 46128]
mov QWORD [rbp - 52928], QWORD [rbp - 46192]
mov QWORD [rbp - 52992], QWORD [rbp - 46256]
mov QWORD [rbp - 53056], QWORD [rbp - 46320]
mov QWORD [rbp - 53120], QWORD [rbp - 46384]
mov QWORD [rbp - 53184], QWORD [rbp - 46448]
mov QWORD [rbp - 53248], QWORD [rbp - 46512]
mov QWORD [rbp - 53312], QWORD [rbp - 46576]
mov QWORD [rbp - 53376], QWORD [rbp - 46640]
mov QWORD [rbp - 53440], QWORD [rbp - 46704]
mov QWORD [rbp - 53504], QWORD [rbp - 46768]
mov QWORD [rbp - 53568], QWORD [rbp - 46832]
mov QWORD [rbp - 53632], QWORD [rbp - 46896]
mov QWORD [rbp - 53696], QWORD [rbp - 46960]
add rsp, 3632
mov rsp, rbp
pop rbp
ret
add rsp, 3632
mov rsp, rbp
pop rbp
ret
compute_pi:
push rbp
mov rbp, rsp
sub rsp, 96
mov QWORD [rbp - 8], 1065353216
mov QWORD [rbp - 8], QWORD [rbp - 0]
mov QWORD [rbp - 24], 1065353216
mov QWORD [rbp - 24], QWORD [rbp - 16]
mov QWORD [rbp - 40], 0
mov QWORD [rbp - 40], QWORD [rbp - 32]
mov QWORD [rbp - 56], 0
mov QWORD [rbp - 56], QWORD [rbp - 48]
mov QWORD [rbp - 64], 1065353216
movsd xmm0, QWORD [rbp - 16]
movsd xmm1, QWORD [rbp - 0]
vmulsd xmm0, xmm1
movsd QWORD [rbp - 72], xmm0
movsd xmm0, QWORD [rbp - 64]
movsd xmm1, QWORD [rbp - 72]
vdivsd xmm0, xmm1
movsd QWORD [rbp - 80], xmm0
movsd xmm0, QWORD [rbp - 32]
movsd xmm1, QWORD [rbp - 80]
vaddsd xmm0, xmm1
movsd QWORD [rbp - 88], xmm0
mov QWORD [rbp - 88], QWORD [rbp - 32]
mov QWORD [rbp - 96], 1073741824
movsd xmm0, QWORD [rbp - 0]
movsd xmm1, QWORD [rbp - 96]
vaddsd xmm0, xmm1
movsd QWORD [rbp - 104], xmm0
mov QWORD [rbp - 104], QWORD [rbp - 0]
mov QWORD [rbp - 112], 0
movsd xmm0, QWORD [rbp - 112]
movsd xmm1, QWORD [rbp - 16]
vsubsd xmm0, xmm1
movsd QWORD [rbp - 120], xmm0
mov QWORD [rbp - 120], QWORD [rbp - 16]
mov QWORD [rbp - 128], 1
mov rax, QWORD [rbp - 48]]
mov rbx, QWORD [rbp - 128]
add rax, rbx
mov QWORD [rbp - 136], QWORD [rbp - 48]
mov QWORD [rbp - 144], 10000
mov rax, QWORD [rbp - 48]]
mov rbx, QWORD [rbp - 144]
 rax, rbx
mov QWORD [rbp - 160], 1082130432
movsd xmm0, QWORD [rbp - 32]
movsd xmm1, QWORD [rbp - 160]
vmulsd xmm0, xmm1
movsd QWORD [rbp - 168], xmm0
mov QWORD [rbp - 168], QWORD [rbp - -24]
add rsp, 88
mov rsp, rbp
pop rbp
ret
add rsp, 88
mov rsp, rbp
pop rbp
ret
main:
call _CRT_INIT
push rbp
mov rbp, rsp
sub rsp, 7456
mov QWORD [rbp - 8], 1500
mov QWORD [rbp - 8], QWORD [rbp - 0]
mov QWORD [rbp - 24], 1000
mov QWORD [rbp - 24], QWORD [rbp - 16]
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
call open_window
mov QWORD [rbp - 104], 0
mov QWORD [rbp - 120], 0
mov QWORD [rbp - 136], 0
mov QWORD [rbp - 152], 1065353216
mov QWORD [rbp - 64], QWORD [rbp - 32]
mov QWORD [rbp - 128], QWORD [rbp - 96]
mov QWORD [rbp - 192], QWORD [rbp - 160]
mov QWORD [rbp - 256], QWORD [rbp - 224]
mov QWORD [rbp - 232], 1065353216
mov QWORD [rbp - 248], 1065353216
mov QWORD [rbp - 264], 1065353216
mov QWORD [rbp - 280], 1065353216
mov QWORD [rbp - 192], QWORD [rbp - 160]
mov QWORD [rbp - 256], QWORD [rbp - 224]
mov QWORD [rbp - 320], QWORD [rbp - 288]
mov QWORD [rbp - 384], QWORD [rbp - 352]
mov QWORD [rbp - 328], 0
mov QWORD [rbp - 344], 1092416963
mov QWORD [rbp - 352], 1128792064
movsd xmm0, QWORD [rbp - 344]
movsd xmm1, QWORD [rbp - 352]
vmulsd xmm0, xmm1
movsd QWORD [rbp - 360], xmm0
mov QWORD [rbp - 304], QWORD [rbp - 288]
mov QWORD [rbp - 368], QWORD [rbp - 352]
mov QWORD [rbp - 7088], QWORD [rbp - 368]
mov QWORD [rbp - 7152], QWORD [rbp - 432]
mov QWORD [rbp - 7216], QWORD [rbp - 496]
mov QWORD [rbp - 7280], QWORD [rbp - 560]
mov QWORD [rbp - 7344], QWORD [rbp - 624]
mov QWORD [rbp - 7408], QWORD [rbp - 688]
mov QWORD [rbp - 7472], QWORD [rbp - 752]
mov QWORD [rbp - 7536], QWORD [rbp - 816]
mov QWORD [rbp - 7600], QWORD [rbp - 880]
mov QWORD [rbp - 7664], QWORD [rbp - 944]
mov QWORD [rbp - 7728], QWORD [rbp - 1008]
mov QWORD [rbp - 7792], QWORD [rbp - 1072]
mov QWORD [rbp - 7856], QWORD [rbp - 1136]
mov QWORD [rbp - 7920], QWORD [rbp - 1200]
mov QWORD [rbp - 7984], QWORD [rbp - 1264]
mov QWORD [rbp - 8048], QWORD [rbp - 1328]
mov QWORD [rbp - 8112], QWORD [rbp - 1392]
mov QWORD [rbp - 8176], QWORD [rbp - 1456]
mov QWORD [rbp - 8240], QWORD [rbp - 1520]
mov QWORD [rbp - 8304], QWORD [rbp - 1584]
mov QWORD [rbp - 8368], QWORD [rbp - 1648]
mov QWORD [rbp - 8432], QWORD [rbp - 1712]
mov QWORD [rbp - 8496], QWORD [rbp - 1776]
mov QWORD [rbp - 8560], QWORD [rbp - 1840]
mov QWORD [rbp - 8624], QWORD [rbp - 1904]
mov QWORD [rbp - 8688], QWORD [rbp - 1968]
mov QWORD [rbp - 8752], QWORD [rbp - 2032]
mov QWORD [rbp - 8816], QWORD [rbp - 2096]
mov QWORD [rbp - 8880], QWORD [rbp - 2160]
mov QWORD [rbp - 8944], QWORD [rbp - 2224]
mov QWORD [rbp - 9008], QWORD [rbp - 2288]
mov QWORD [rbp - 9072], QWORD [rbp - 2352]
mov QWORD [rbp - 9136], QWORD [rbp - 2416]
mov QWORD [rbp - 9200], QWORD [rbp - 2480]
mov QWORD [rbp - 9264], QWORD [rbp - 2544]
mov QWORD [rbp - 9328], QWORD [rbp - 2608]
mov QWORD [rbp - 9392], QWORD [rbp - 2672]
mov QWORD [rbp - 9456], QWORD [rbp - 2736]
mov QWORD [rbp - 9520], QWORD [rbp - 2800]
mov QWORD [rbp - 9584], QWORD [rbp - 2864]
mov QWORD [rbp - 9648], QWORD [rbp - 2928]
mov QWORD [rbp - 9712], QWORD [rbp - 2992]
mov QWORD [rbp - 9776], QWORD [rbp - 3056]
mov QWORD [rbp - 9840], QWORD [rbp - 3120]
mov QWORD [rbp - 9904], QWORD [rbp - 3184]
mov QWORD [rbp - 9968], QWORD [rbp - 3248]
mov QWORD [rbp - 10032], QWORD [rbp - 3312]
mov QWORD [rbp - 10096], QWORD [rbp - 3376]
mov QWORD [rbp - 10160], QWORD [rbp - 3440]
mov QWORD [rbp - 10224], QWORD [rbp - 3504]
mov QWORD [rbp - 10288], QWORD [rbp - 3568]
mov QWORD [rbp - 10352], QWORD [rbp - 3632]
mov QWORD [rbp - 10416], QWORD [rbp - 3696]
mov QWORD [rbp - 10480], QWORD [rbp - 3760]
mov QWORD [rbp - 10544], QWORD [rbp - 3824]
mov QWORD [rbp - 10608], QWORD [rbp - 3888]
mov QWORD [rbp - 10672], QWORD [rbp - 3952]
mov QWORD [rbp - 10736], QWORD [rbp - 4016]
mov QWORD [rbp - 10800], QWORD [rbp - 4080]
mov QWORD [rbp - 10864], QWORD [rbp - 4144]
mov QWORD [rbp - 10928], QWORD [rbp - 4208]
mov QWORD [rbp - 10992], QWORD [rbp - 4272]
mov QWORD [rbp - 11056], QWORD [rbp - 4336]
mov QWORD [rbp - 11120], QWORD [rbp - 4400]
mov QWORD [rbp - 11184], QWORD [rbp - 4464]
mov QWORD [rbp - 11248], QWORD [rbp - 4528]
mov QWORD [rbp - 11312], QWORD [rbp - 4592]
mov QWORD [rbp - 11376], QWORD [rbp - 4656]
mov QWORD [rbp - 11440], QWORD [rbp - 4720]
mov QWORD [rbp - 11504], QWORD [rbp - 4784]
mov QWORD [rbp - 11568], QWORD [rbp - 4848]
mov QWORD [rbp - 11632], QWORD [rbp - 4912]
mov QWORD [rbp - 11696], QWORD [rbp - 4976]
mov QWORD [rbp - 11760], QWORD [rbp - 5040]
mov QWORD [rbp - 11824], QWORD [rbp - 5104]
mov QWORD [rbp - 11888], QWORD [rbp - 5168]
mov QWORD [rbp - 11952], QWORD [rbp - 5232]
mov QWORD [rbp - 12016], QWORD [rbp - 5296]
mov QWORD [rbp - 12080], QWORD [rbp - 5360]
mov QWORD [rbp - 12144], QWORD [rbp - 5424]
mov QWORD [rbp - 12208], QWORD [rbp - 5488]
mov QWORD [rbp - 12272], QWORD [rbp - 5552]
mov QWORD [rbp - 12336], QWORD [rbp - 5616]
mov QWORD [rbp - 12400], QWORD [rbp - 5680]
mov QWORD [rbp - 12464], QWORD [rbp - 5744]
mov QWORD [rbp - 12528], QWORD [rbp - 5808]
mov QWORD [rbp - 12592], QWORD [rbp - 5872]
mov QWORD [rbp - 12656], QWORD [rbp - 5936]
mov QWORD [rbp - 12720], QWORD [rbp - 6000]
mov QWORD [rbp - 12784], QWORD [rbp - 6064]
mov QWORD [rbp - 12848], QWORD [rbp - 6128]
mov QWORD [rbp - 12912], QWORD [rbp - 6192]
mov QWORD [rbp - 12976], QWORD [rbp - 6256]
mov QWORD [rbp - 13040], QWORD [rbp - 6320]
mov QWORD [rbp - 13104], QWORD [rbp - 6384]
mov QWORD [rbp - 13168], QWORD [rbp - 6448]
mov QWORD [rbp - 13232], QWORD [rbp - 6512]
mov QWORD [rbp - 13296], QWORD [rbp - 6576]
mov QWORD [rbp - 13360], QWORD [rbp - 6640]
mov QWORD [rbp - 13424], QWORD [rbp - 6704]
mov QWORD [rbp - 13488], QWORD [rbp - 6768]
mov QWORD [rbp - 13552], QWORD [rbp - 6832]
mov QWORD [rbp - 13616], QWORD [rbp - 6896]
mov QWORD [rbp - 13680], QWORD [rbp - 6960]
mov QWORD [rbp - 13744], QWORD [rbp - 7024]
mov QWORD [rbp - 13808], QWORD [rbp - 7088]
mov QWORD [rbp - 13872], QWORD [rbp - 7152]
mov QWORD [rbp - 13936], QWORD [rbp - 7216]
mov QWORD [rbp - 14000], QWORD [rbp - 7280]
mov QWORD [rbp - 14064], QWORD [rbp - 7344]
mov QWORD [rbp - 14128], QWORD [rbp - 7408]
mov QWORD [rbp - 14192], QWORD [rbp - 7472]
mov QWORD [rbp - 14256], QWORD [rbp - 7536]
mov QWORD [rbp - 14320], QWORD [rbp - 7600]
mov QWORD [rbp - 14384], QWORD [rbp - 7664]
mov QWORD [rbp - 14448], QWORD [rbp - 7728]
mov QWORD [rbp - 14512], QWORD [rbp - 7792]
mov QWORD [rbp - 14576], QWORD [rbp - 7856]
mov QWORD [rbp - 14640], QWORD [rbp - 7920]
mov QWORD [rbp - 14704], QWORD [rbp - 7984]
mov QWORD [rbp - 14768], QWORD [rbp - 8048]
mov QWORD [rbp - 14832], QWORD [rbp - 8112]
mov QWORD [rbp - 14896], QWORD [rbp - 8176]
mov QWORD [rbp - 14960], QWORD [rbp - 8240]
mov QWORD [rbp - 15024], QWORD [rbp - 8304]
mov QWORD [rbp - 15088], QWORD [rbp - 8368]
mov QWORD [rbp - 15152], QWORD [rbp - 8432]
mov QWORD [rbp - 15216], QWORD [rbp - 8496]
mov QWORD [rbp - 15280], QWORD [rbp - 8560]
mov QWORD [rbp - 15344], QWORD [rbp - 8624]
mov QWORD [rbp - 15408], QWORD [rbp - 8688]
mov QWORD [rbp - 15472], QWORD [rbp - 8752]
mov QWORD [rbp - 15536], QWORD [rbp - 8816]
mov QWORD [rbp - 15600], QWORD [rbp - 8880]
mov QWORD [rbp - 15664], QWORD [rbp - 8944]
mov QWORD [rbp - 15728], QWORD [rbp - 9008]
mov QWORD [rbp - 15792], QWORD [rbp - 9072]
mov QWORD [rbp - 15856], QWORD [rbp - 9136]
mov QWORD [rbp - 15920], QWORD [rbp - 9200]
mov QWORD [rbp - 15984], QWORD [rbp - 9264]
mov QWORD [rbp - 16048], QWORD [rbp - 9328]
mov QWORD [rbp - 16112], QWORD [rbp - 9392]
mov QWORD [rbp - 16176], QWORD [rbp - 9456]
mov QWORD [rbp - 16240], QWORD [rbp - 9520]
mov QWORD [rbp - 16304], QWORD [rbp - 9584]
mov QWORD [rbp - 16368], QWORD [rbp - 9648]
mov QWORD [rbp - 16432], QWORD [rbp - 9712]
mov QWORD [rbp - 16496], QWORD [rbp - 9776]
mov QWORD [rbp - 16560], QWORD [rbp - 9840]
mov QWORD [rbp - 16624], QWORD [rbp - 9904]
mov QWORD [rbp - 16688], QWORD [rbp - 9968]
mov QWORD [rbp - 16752], QWORD [rbp - 10032]
mov QWORD [rbp - 16816], QWORD [rbp - 10096]
mov QWORD [rbp - 16880], QWORD [rbp - 10160]
mov QWORD [rbp - 16944], QWORD [rbp - 10224]
mov QWORD [rbp - 17008], QWORD [rbp - 10288]
mov QWORD [rbp - 17072], QWORD [rbp - 10352]
mov QWORD [rbp - 17136], QWORD [rbp - 10416]
mov QWORD [rbp - 17200], QWORD [rbp - 10480]
mov QWORD [rbp - 17264], QWORD [rbp - 10544]
mov QWORD [rbp - 17328], QWORD [rbp - 10608]
mov QWORD [rbp - 17392], QWORD [rbp - 10672]
mov QWORD [rbp - 17456], QWORD [rbp - 10736]
mov QWORD [rbp - 17520], QWORD [rbp - 10800]
mov QWORD [rbp - 17584], QWORD [rbp - 10864]
mov QWORD [rbp - 17648], QWORD [rbp - 10928]
mov QWORD [rbp - 17712], QWORD [rbp - 10992]
mov QWORD [rbp - 17776], QWORD [rbp - 11056]
mov QWORD [rbp - 17840], QWORD [rbp - 11120]
mov QWORD [rbp - 17904], QWORD [rbp - 11184]
mov QWORD [rbp - 17968], QWORD [rbp - 11248]
mov QWORD [rbp - 18032], QWORD [rbp - 11312]
mov QWORD [rbp - 18096], QWORD [rbp - 11376]
mov QWORD [rbp - 18160], QWORD [rbp - 11440]
mov QWORD [rbp - 18224], QWORD [rbp - 11504]
mov QWORD [rbp - 18288], QWORD [rbp - 11568]
mov QWORD [rbp - 18352], QWORD [rbp - 11632]
mov QWORD [rbp - 18416], QWORD [rbp - 11696]
mov QWORD [rbp - 18480], QWORD [rbp - 11760]
mov QWORD [rbp - 18544], QWORD [rbp - 11824]
mov QWORD [rbp - 18608], QWORD [rbp - 11888]
mov QWORD [rbp - 18672], QWORD [rbp - 11952]
mov QWORD [rbp - 18736], QWORD [rbp - 12016]
mov QWORD [rbp - 18800], QWORD [rbp - 12080]
mov QWORD [rbp - 18864], QWORD [rbp - 12144]
mov QWORD [rbp - 18928], QWORD [rbp - 12208]
mov QWORD [rbp - 18992], QWORD [rbp - 12272]
mov QWORD [rbp - 19056], QWORD [rbp - 12336]
mov QWORD [rbp - 19120], QWORD [rbp - 12400]
mov QWORD [rbp - 19184], QWORD [rbp - 12464]
mov QWORD [rbp - 19248], QWORD [rbp - 12528]
mov QWORD [rbp - 19312], QWORD [rbp - 12592]
mov QWORD [rbp - 19376], QWORD [rbp - 12656]
mov QWORD [rbp - 19440], QWORD [rbp - 12720]
mov QWORD [rbp - 19504], QWORD [rbp - 12784]
mov QWORD [rbp - 19568], QWORD [rbp - 12848]
mov QWORD [rbp - 19632], QWORD [rbp - 12912]
mov QWORD [rbp - 19696], QWORD [rbp - 12976]
mov QWORD [rbp - 19760], QWORD [rbp - 13040]
mov QWORD [rbp - 19824], QWORD [rbp - 13104]
mov QWORD [rbp - 19888], QWORD [rbp - 13168]
mov QWORD [rbp - 19952], QWORD [rbp - 13232]
mov QWORD [rbp - 20016], QWORD [rbp - 13296]
mov QWORD [rbp - 20080], QWORD [rbp - 13360]
mov QWORD [rbp - 20144], QWORD [rbp - 13424]
mov QWORD [rbp - 20208], QWORD [rbp - 13488]
mov QWORD [rbp - 20272], QWORD [rbp - 13552]
mov QWORD [rbp - 20336], QWORD [rbp - 13616]
mov QWORD [rbp - 20400], QWORD [rbp - 13680]
mov QWORD [rbp - 20464], QWORD [rbp - 13744]
mov QWORD [rbp - 20528], QWORD [rbp - 13808]
mov QWORD [rbp - 20592], QWORD [rbp - 13872]
mov QWORD [rbp - 20656], QWORD [rbp - 13936]
mov QWORD [rbp - 20720], QWORD [rbp - 14000]
mov QWORD [rbp - 20784], QWORD [rbp - 14064]
mov QWORD [rbp - 20848], QWORD [rbp - 14128]
mov QWORD [rbp - 20912], QWORD [rbp - 14192]
mov QWORD [rbp - 20976], QWORD [rbp - 14256]
mov QWORD [rbp - 21040], QWORD [rbp - 14320]
mov QWORD [rbp - 21104], QWORD [rbp - 14384]
mov QWORD [rbp - 21168], QWORD [rbp - 14448]
mov QWORD [rbp - 21232], QWORD [rbp - 14512]
mov QWORD [rbp - 21296], QWORD [rbp - 14576]
mov QWORD [rbp - 21360], QWORD [rbp - 14640]
mov QWORD [rbp - 21424], QWORD [rbp - 14704]
mov QWORD [rbp - 21488], QWORD [rbp - 14768]
mov QWORD [rbp - 21552], QWORD [rbp - 14832]
mov QWORD [rbp - 21616], QWORD [rbp - 14896]
mov QWORD [rbp - 21680], QWORD [rbp - 14960]
mov QWORD [rbp - 21744], QWORD [rbp - 15024]
mov QWORD [rbp - 21808], QWORD [rbp - 15088]
mov QWORD [rbp - 21872], QWORD [rbp - 15152]
mov QWORD [rbp - 21936], QWORD [rbp - 15216]
mov QWORD [rbp - 22000], QWORD [rbp - 15280]
mov QWORD [rbp - 22064], QWORD [rbp - 15344]
mov QWORD [rbp - 22128], QWORD [rbp - 15408]
mov QWORD [rbp - 22192], QWORD [rbp - 15472]
mov QWORD [rbp - 22256], QWORD [rbp - 15536]
mov QWORD [rbp - 22320], QWORD [rbp - 15600]
mov QWORD [rbp - 22384], QWORD [rbp - 15664]
mov QWORD [rbp - 22448], QWORD [rbp - 15728]
mov QWORD [rbp - 22512], QWORD [rbp - 15792]
mov QWORD [rbp - 22576], QWORD [rbp - 15856]
mov QWORD [rbp - 22640], QWORD [rbp - 15920]
mov QWORD [rbp - 22704], QWORD [rbp - 15984]
mov QWORD [rbp - 22768], QWORD [rbp - 16048]
mov QWORD [rbp - 22832], QWORD [rbp - 16112]
mov QWORD [rbp - 22896], QWORD [rbp - 16176]
mov QWORD [rbp - 22960], QWORD [rbp - 16240]
mov QWORD [rbp - 23024], QWORD [rbp - 16304]
mov QWORD [rbp - 23088], QWORD [rbp - 16368]
mov QWORD [rbp - 23152], QWORD [rbp - 16432]
mov QWORD [rbp - 23216], QWORD [rbp - 16496]
mov QWORD [rbp - 23280], QWORD [rbp - 16560]
mov QWORD [rbp - 23344], QWORD [rbp - 16624]
mov QWORD [rbp - 23408], QWORD [rbp - 16688]
mov QWORD [rbp - 23472], QWORD [rbp - 16752]
mov QWORD [rbp - 23536], QWORD [rbp - 16816]
mov QWORD [rbp - 23600], QWORD [rbp - 16880]
mov QWORD [rbp - 23664], QWORD [rbp - 16944]
mov QWORD [rbp - 23728], QWORD [rbp - 17008]
mov QWORD [rbp - 23792], QWORD [rbp - 17072]
mov QWORD [rbp - 23856], QWORD [rbp - 17136]
mov QWORD [rbp - 23920], QWORD [rbp - 17200]
mov QWORD [rbp - 23984], QWORD [rbp - 17264]
mov QWORD [rbp - 24048], QWORD [rbp - 17328]
mov QWORD [rbp - 24112], QWORD [rbp - 17392]
mov QWORD [rbp - 24176], QWORD [rbp - 17456]
mov QWORD [rbp - 24240], QWORD [rbp - 17520]
mov QWORD [rbp - 24304], QWORD [rbp - 17584]
mov QWORD [rbp - 24368], QWORD [rbp - 17648]
mov QWORD [rbp - 24432], QWORD [rbp - 17712]
mov QWORD [rbp - 24496], QWORD [rbp - 17776]
mov QWORD [rbp - 24560], QWORD [rbp - 17840]
mov QWORD [rbp - 24624], QWORD [rbp - 17904]
mov QWORD [rbp - 24688], QWORD [rbp - 17968]
mov QWORD [rbp - 24752], QWORD [rbp - 18032]
mov QWORD [rbp - 24816], QWORD [rbp - 18096]
mov QWORD [rbp - 24880], QWORD [rbp - 18160]
mov QWORD [rbp - 24944], QWORD [rbp - 18224]
mov QWORD [rbp - 25008], QWORD [rbp - 18288]
mov QWORD [rbp - 25072], QWORD [rbp - 18352]
mov QWORD [rbp - 25136], QWORD [rbp - 18416]
mov QWORD [rbp - 25200], QWORD [rbp - 18480]
mov QWORD [rbp - 25264], QWORD [rbp - 18544]
mov QWORD [rbp - 25328], QWORD [rbp - 18608]
mov QWORD [rbp - 25392], QWORD [rbp - 18672]
mov QWORD [rbp - 25456], QWORD [rbp - 18736]
mov QWORD [rbp - 25520], QWORD [rbp - 18800]
mov QWORD [rbp - 25584], QWORD [rbp - 18864]
mov QWORD [rbp - 25648], QWORD [rbp - 18928]
mov QWORD [rbp - 25712], QWORD [rbp - 18992]
mov QWORD [rbp - 25776], QWORD [rbp - 19056]
mov QWORD [rbp - 25840], QWORD [rbp - 19120]
mov QWORD [rbp - 25904], QWORD [rbp - 19184]
mov QWORD [rbp - 25968], QWORD [rbp - 19248]
mov QWORD [rbp - 26032], QWORD [rbp - 19312]
mov QWORD [rbp - 26096], QWORD [rbp - 19376]
mov QWORD [rbp - 26160], QWORD [rbp - 19440]
mov QWORD [rbp - 26224], QWORD [rbp - 19504]
mov QWORD [rbp - 26288], QWORD [rbp - 19568]
mov QWORD [rbp - 26352], QWORD [rbp - 19632]
mov QWORD [rbp - 26416], QWORD [rbp - 19696]
mov QWORD [rbp - 26480], QWORD [rbp - 19760]
mov QWORD [rbp - 26544], QWORD [rbp - 19824]
mov QWORD [rbp - 26608], QWORD [rbp - 19888]
mov QWORD [rbp - 26672], QWORD [rbp - 19952]
mov QWORD [rbp - 26736], QWORD [rbp - 20016]
mov QWORD [rbp - 26800], QWORD [rbp - 20080]
mov QWORD [rbp - 26864], QWORD [rbp - 20144]
mov QWORD [rbp - 26928], QWORD [rbp - 20208]
mov QWORD [rbp - 26992], QWORD [rbp - 20272]
mov QWORD [rbp - 27056], QWORD [rbp - 20336]
mov QWORD [rbp - 27120], QWORD [rbp - 20400]
mov QWORD [rbp - 27184], QWORD [rbp - 20464]
mov QWORD [rbp - 27248], QWORD [rbp - 20528]
mov QWORD [rbp - 27312], QWORD [rbp - 20592]
mov QWORD [rbp - 27376], QWORD [rbp - 20656]
mov QWORD [rbp - 27440], QWORD [rbp - 20720]
mov QWORD [rbp - 27504], QWORD [rbp - 20784]
mov QWORD [rbp - 27568], QWORD [rbp - 20848]
mov QWORD [rbp - 27632], QWORD [rbp - 20912]
mov QWORD [rbp - 27696], QWORD [rbp - 20976]
mov QWORD [rbp - 27760], QWORD [rbp - 21040]
mov QWORD [rbp - 27824], QWORD [rbp - 21104]
mov QWORD [rbp - 27888], QWORD [rbp - 21168]
mov QWORD [rbp - 27952], QWORD [rbp - 21232]
mov QWORD [rbp - 28016], QWORD [rbp - 21296]
mov QWORD [rbp - 28080], QWORD [rbp - 21360]
mov QWORD [rbp - 28144], QWORD [rbp - 21424]
mov QWORD [rbp - 28208], QWORD [rbp - 21488]
mov QWORD [rbp - 28272], QWORD [rbp - 21552]
mov QWORD [rbp - 28336], QWORD [rbp - 21616]
mov QWORD [rbp - 28400], QWORD [rbp - 21680]
mov QWORD [rbp - 28464], QWORD [rbp - 21744]
mov QWORD [rbp - 28528], QWORD [rbp - 21808]
mov QWORD [rbp - 28592], QWORD [rbp - 21872]
mov QWORD [rbp - 28656], QWORD [rbp - 21936]
mov QWORD [rbp - 28720], QWORD [rbp - 22000]
mov QWORD [rbp - 28784], QWORD [rbp - 22064]
mov QWORD [rbp - 28848], QWORD [rbp - 22128]
mov QWORD [rbp - 28912], QWORD [rbp - 22192]
mov QWORD [rbp - 28976], QWORD [rbp - 22256]
mov QWORD [rbp - 29040], QWORD [rbp - 22320]
mov QWORD [rbp - 29104], QWORD [rbp - 22384]
mov QWORD [rbp - 29168], QWORD [rbp - 22448]
mov QWORD [rbp - 29232], QWORD [rbp - 22512]
mov QWORD [rbp - 29296], QWORD [rbp - 22576]
mov QWORD [rbp - 29360], QWORD [rbp - 22640]
mov QWORD [rbp - 29424], QWORD [rbp - 22704]
mov QWORD [rbp - 29488], QWORD [rbp - 22768]
mov QWORD [rbp - 29552], QWORD [rbp - 22832]
mov QWORD [rbp - 29616], QWORD [rbp - 22896]
mov QWORD [rbp - 29680], QWORD [rbp - 22960]
mov QWORD [rbp - 29744], QWORD [rbp - 23024]
mov QWORD [rbp - 29808], QWORD [rbp - 23088]
mov QWORD [rbp - 29872], QWORD [rbp - 23152]
mov QWORD [rbp - 29936], QWORD [rbp - 23216]
mov QWORD [rbp - 30000], QWORD [rbp - 23280]
mov QWORD [rbp - 30064], QWORD [rbp - 23344]
mov QWORD [rbp - 30128], QWORD [rbp - 23408]
mov QWORD [rbp - 30192], QWORD [rbp - 23472]
mov QWORD [rbp - 30256], QWORD [rbp - 23536]
mov QWORD [rbp - 30320], QWORD [rbp - 23600]
mov QWORD [rbp - 30384], QWORD [rbp - 23664]
mov QWORD [rbp - 30448], QWORD [rbp - 23728]
mov QWORD [rbp - 30512], QWORD [rbp - 23792]
mov QWORD [rbp - 30576], QWORD [rbp - 23856]
mov QWORD [rbp - 30640], QWORD [rbp - 23920]
mov QWORD [rbp - 30704], QWORD [rbp - 23984]
mov QWORD [rbp - 30768], QWORD [rbp - 24048]
mov QWORD [rbp - 30832], QWORD [rbp - 24112]
mov QWORD [rbp - 30896], QWORD [rbp - 24176]
mov QWORD [rbp - 30960], QWORD [rbp - 24240]
mov QWORD [rbp - 31024], QWORD [rbp - 24304]
mov QWORD [rbp - 31088], QWORD [rbp - 24368]
mov QWORD [rbp - 31152], QWORD [rbp - 24432]
mov QWORD [rbp - 31216], QWORD [rbp - 24496]
mov QWORD [rbp - 31280], QWORD [rbp - 24560]
mov QWORD [rbp - 31344], QWORD [rbp - 24624]
mov QWORD [rbp - 31408], QWORD [rbp - 24688]
mov QWORD [rbp - 31472], QWORD [rbp - 24752]
mov QWORD [rbp - 31536], QWORD [rbp - 24816]
mov QWORD [rbp - 31600], QWORD [rbp - 24880]
mov QWORD [rbp - 31664], QWORD [rbp - 24944]
mov QWORD [rbp - 31728], QWORD [rbp - 25008]
mov QWORD [rbp - 31792], QWORD [rbp - 25072]
mov QWORD [rbp - 31856], QWORD [rbp - 25136]
mov QWORD [rbp - 31920], QWORD [rbp - 25200]
mov QWORD [rbp - 31984], QWORD [rbp - 25264]
mov QWORD [rbp - 32048], QWORD [rbp - 25328]
mov QWORD [rbp - 32112], QWORD [rbp - 25392]
mov QWORD [rbp - 32176], QWORD [rbp - 25456]
mov QWORD [rbp - 32240], QWORD [rbp - 25520]
mov QWORD [rbp - 32304], QWORD [rbp - 25584]
mov QWORD [rbp - 32368], QWORD [rbp - 25648]
mov QWORD [rbp - 32432], QWORD [rbp - 25712]
mov QWORD [rbp - 32496], QWORD [rbp - 25776]
mov QWORD [rbp - 32560], QWORD [rbp - 25840]
mov QWORD [rbp - 32624], QWORD [rbp - 25904]
mov QWORD [rbp - 32688], QWORD [rbp - 25968]
mov QWORD [rbp - 32752], QWORD [rbp - 26032]
mov QWORD [rbp - 32816], QWORD [rbp - 26096]
mov QWORD [rbp - 32880], QWORD [rbp - 26160]
mov QWORD [rbp - 32944], QWORD [rbp - 26224]
mov QWORD [rbp - 33008], QWORD [rbp - 26288]
mov QWORD [rbp - 33072], QWORD [rbp - 26352]
mov QWORD [rbp - 33136], QWORD [rbp - 26416]
mov QWORD [rbp - 33200], QWORD [rbp - 26480]
mov QWORD [rbp - 33264], QWORD [rbp - 26544]
mov QWORD [rbp - 33328], QWORD [rbp - 26608]
mov QWORD [rbp - 33392], QWORD [rbp - 26672]
mov QWORD [rbp - 33456], QWORD [rbp - 26736]
mov QWORD [rbp - 33520], QWORD [rbp - 26800]
mov QWORD [rbp - 33584], QWORD [rbp - 26864]
mov QWORD [rbp - 33648], QWORD [rbp - 26928]
mov QWORD [rbp - 33712], QWORD [rbp - 26992]
mov QWORD [rbp - 33776], QWORD [rbp - 27056]
mov QWORD [rbp - 33840], QWORD [rbp - 27120]
mov QWORD [rbp - 33904], QWORD [rbp - 27184]
mov QWORD [rbp - 33968], QWORD [rbp - 27248]
mov QWORD [rbp - 34032], QWORD [rbp - 27312]
mov QWORD [rbp - 34096], QWORD [rbp - 27376]
mov QWORD [rbp - 34160], QWORD [rbp - 27440]
mov QWORD [rbp - 34224], QWORD [rbp - 27504]
mov QWORD [rbp - 34288], QWORD [rbp - 27568]
mov QWORD [rbp - 34352], QWORD [rbp - 27632]
mov QWORD [rbp - 34416], QWORD [rbp - 27696]
mov QWORD [rbp - 34480], QWORD [rbp - 27760]
mov QWORD [rbp - 34544], QWORD [rbp - 27824]
mov QWORD [rbp - 34608], QWORD [rbp - 27888]
mov QWORD [rbp - 34672], QWORD [rbp - 27952]
mov QWORD [rbp - 34736], QWORD [rbp - 28016]
mov QWORD [rbp - 34800], QWORD [rbp - 28080]
mov QWORD [rbp - 34864], QWORD [rbp - 28144]
mov QWORD [rbp - 34928], QWORD [rbp - 28208]
mov QWORD [rbp - 34992], QWORD [rbp - 28272]
mov QWORD [rbp - 35056], QWORD [rbp - 28336]
mov QWORD [rbp - 35120], QWORD [rbp - 28400]
mov QWORD [rbp - 35184], QWORD [rbp - 28464]
mov QWORD [rbp - 35248], QWORD [rbp - 28528]
mov QWORD [rbp - 35312], QWORD [rbp - 28592]
mov QWORD [rbp - 35376], QWORD [rbp - 28656]
mov QWORD [rbp - 35440], QWORD [rbp - 28720]
mov QWORD [rbp - 35504], QWORD [rbp - 28784]
mov QWORD [rbp - 35568], QWORD [rbp - 28848]
mov QWORD [rbp - 35632], QWORD [rbp - 28912]
mov QWORD [rbp - 35696], QWORD [rbp - 28976]
mov QWORD [rbp - 35760], QWORD [rbp - 29040]
mov QWORD [rbp - 35824], QWORD [rbp - 29104]
mov QWORD [rbp - 35888], QWORD [rbp - 29168]
mov QWORD [rbp - 35952], QWORD [rbp - 29232]
mov QWORD [rbp - 36016], QWORD [rbp - 29296]
mov QWORD [rbp - 36080], QWORD [rbp - 29360]
mov QWORD [rbp - 36144], QWORD [rbp - 29424]
mov QWORD [rbp - 36208], QWORD [rbp - 29488]
mov QWORD [rbp - 36272], QWORD [rbp - 29552]
mov QWORD [rbp - 36336], QWORD [rbp - 29616]
mov QWORD [rbp - 36400], QWORD [rbp - 29680]
mov QWORD [rbp - 36464], QWORD [rbp - 29744]
mov QWORD [rbp - 36528], QWORD [rbp - 29808]
mov QWORD [rbp - 36592], QWORD [rbp - 29872]
mov QWORD [rbp - 36656], QWORD [rbp - 29936]
mov QWORD [rbp - 36720], QWORD [rbp - 30000]
mov QWORD [rbp - 36784], QWORD [rbp - 30064]
mov QWORD [rbp - 36848], QWORD [rbp - 30128]
mov QWORD [rbp - 36912], QWORD [rbp - 30192]
mov QWORD [rbp - 36976], QWORD [rbp - 30256]
mov QWORD [rbp - 37040], QWORD [rbp - 30320]
mov QWORD [rbp - 37104], QWORD [rbp - 30384]
mov QWORD [rbp - 37168], QWORD [rbp - 30448]
mov QWORD [rbp - 37232], QWORD [rbp - 30512]
mov QWORD [rbp - 37296], QWORD [rbp - 30576]
mov QWORD [rbp - 37360], QWORD [rbp - 30640]
mov QWORD [rbp - 37424], QWORD [rbp - 30704]
mov QWORD [rbp - 37488], QWORD [rbp - 30768]
mov QWORD [rbp - 37552], QWORD [rbp - 30832]
mov QWORD [rbp - 37616], QWORD [rbp - 30896]
mov QWORD [rbp - 37680], QWORD [rbp - 30960]
mov QWORD [rbp - 37744], QWORD [rbp - 31024]
mov QWORD [rbp - 37808], QWORD [rbp - 31088]
mov QWORD [rbp - 37872], QWORD [rbp - 31152]
mov QWORD [rbp - 37936], QWORD [rbp - 31216]
mov QWORD [rbp - 38000], QWORD [rbp - 31280]
mov QWORD [rbp - 38064], QWORD [rbp - 31344]
mov QWORD [rbp - 38128], QWORD [rbp - 31408]
mov QWORD [rbp - 38192], QWORD [rbp - 31472]
mov QWORD [rbp - 38256], QWORD [rbp - 31536]
mov QWORD [rbp - 38320], QWORD [rbp - 31600]
mov QWORD [rbp - 38384], QWORD [rbp - 31664]
mov QWORD [rbp - 38448], QWORD [rbp - 31728]
mov QWORD [rbp - 38512], QWORD [rbp - 31792]
mov QWORD [rbp - 38576], QWORD [rbp - 31856]
mov QWORD [rbp - 38640], QWORD [rbp - 31920]
mov QWORD [rbp - 38704], QWORD [rbp - 31984]
mov QWORD [rbp - 38768], QWORD [rbp - 32048]
mov QWORD [rbp - 38832], QWORD [rbp - 32112]
mov QWORD [rbp - 38896], QWORD [rbp - 32176]
mov QWORD [rbp - 38960], QWORD [rbp - 32240]
mov QWORD [rbp - 39024], QWORD [rbp - 32304]
mov QWORD [rbp - 39088], QWORD [rbp - 32368]
mov QWORD [rbp - 39152], QWORD [rbp - 32432]
mov QWORD [rbp - 39216], QWORD [rbp - 32496]
mov QWORD [rbp - 39280], QWORD [rbp - 32560]
mov QWORD [rbp - 39344], QWORD [rbp - 32624]
mov QWORD [rbp - 39408], QWORD [rbp - 32688]
mov QWORD [rbp - 39472], QWORD [rbp - 32752]
mov QWORD [rbp - 39536], QWORD [rbp - 32816]
mov QWORD [rbp - 39600], QWORD [rbp - 32880]
mov QWORD [rbp - 39664], QWORD [rbp - 32944]
mov QWORD [rbp - 39728], QWORD [rbp - 33008]
mov QWORD [rbp - 39792], QWORD [rbp - 33072]
mov QWORD [rbp - 39856], QWORD [rbp - 33136]
mov QWORD [rbp - 39920], QWORD [rbp - 33200]
mov QWORD [rbp - 39984], QWORD [rbp - 33264]
mov QWORD [rbp - 40048], QWORD [rbp - 33328]
mov QWORD [rbp - 40112], QWORD [rbp - 33392]
mov QWORD [rbp - 40176], QWORD [rbp - 33456]
mov QWORD [rbp - 40240], QWORD [rbp - 33520]
mov QWORD [rbp - 40304], QWORD [rbp - 33584]
mov QWORD [rbp - 40368], QWORD [rbp - 33648]
mov QWORD [rbp - 40432], QWORD [rbp - 33712]
mov QWORD [rbp - 40496], QWORD [rbp - 33776]
mov QWORD [rbp - 40560], QWORD [rbp - 33840]
mov QWORD [rbp - 40624], QWORD [rbp - 33904]
mov QWORD [rbp - 40688], QWORD [rbp - 33968]
mov QWORD [rbp - 40752], QWORD [rbp - 34032]
mov QWORD [rbp - 40816], QWORD [rbp - 34096]
mov QWORD [rbp - 40880], QWORD [rbp - 34160]
mov QWORD [rbp - 40944], QWORD [rbp - 34224]
mov QWORD [rbp - 41008], QWORD [rbp - 34288]
mov QWORD [rbp - 41072], QWORD [rbp - 34352]
mov QWORD [rbp - 41136], QWORD [rbp - 34416]
mov QWORD [rbp - 41200], QWORD [rbp - 34480]
mov QWORD [rbp - 41264], QWORD [rbp - 34544]
mov QWORD [rbp - 41328], QWORD [rbp - 34608]
mov QWORD [rbp - 41392], QWORD [rbp - 34672]
mov QWORD [rbp - 41456], QWORD [rbp - 34736]
mov QWORD [rbp - 41520], QWORD [rbp - 34800]
mov QWORD [rbp - 41584], QWORD [rbp - 34864]
mov QWORD [rbp - 41648], QWORD [rbp - 34928]
mov QWORD [rbp - 41712], QWORD [rbp - 34992]
mov QWORD [rbp - 41776], QWORD [rbp - 35056]
mov QWORD [rbp - 41840], QWORD [rbp - 35120]
mov QWORD [rbp - 41904], QWORD [rbp - 35184]
mov QWORD [rbp - 41968], QWORD [rbp - 35248]
mov QWORD [rbp - 42032], QWORD [rbp - 35312]
mov QWORD [rbp - 42096], QWORD [rbp - 35376]
mov QWORD [rbp - 42160], QWORD [rbp - 35440]
mov QWORD [rbp - 42224], QWORD [rbp - 35504]
mov QWORD [rbp - 42288], QWORD [rbp - 35568]
mov QWORD [rbp - 42352], QWORD [rbp - 35632]
mov QWORD [rbp - 42416], QWORD [rbp - 35696]
mov QWORD [rbp - 42480], QWORD [rbp - 35760]
mov QWORD [rbp - 42544], QWORD [rbp - 35824]
mov QWORD [rbp - 42608], QWORD [rbp - 35888]
mov QWORD [rbp - 42672], QWORD [rbp - 35952]
mov QWORD [rbp - 42736], QWORD [rbp - 36016]
mov QWORD [rbp - 42800], QWORD [rbp - 36080]
mov QWORD [rbp - 42864], QWORD [rbp - 36144]
mov QWORD [rbp - 42928], QWORD [rbp - 36208]
mov QWORD [rbp - 42992], QWORD [rbp - 36272]
mov QWORD [rbp - 43056], QWORD [rbp - 36336]
mov QWORD [rbp - 43120], QWORD [rbp - 36400]
mov QWORD [rbp - 43184], QWORD [rbp - 36464]
mov QWORD [rbp - 43248], QWORD [rbp - 36528]
mov QWORD [rbp - 43312], QWORD [rbp - 36592]
mov QWORD [rbp - 43376], QWORD [rbp - 36656]
mov QWORD [rbp - 43440], QWORD [rbp - 36720]
mov QWORD [rbp - 43504], QWORD [rbp - 36784]
mov QWORD [rbp - 43568], QWORD [rbp - 36848]
mov QWORD [rbp - 43632], QWORD [rbp - 36912]
mov QWORD [rbp - 43696], QWORD [rbp - 36976]
mov QWORD [rbp - 43760], QWORD [rbp - 37040]
mov QWORD [rbp - 43824], QWORD [rbp - 37104]
mov QWORD [rbp - 43888], QWORD [rbp - 37168]
mov QWORD [rbp - 43952], QWORD [rbp - 37232]
mov QWORD [rbp - 44016], QWORD [rbp - 37296]
mov QWORD [rbp - 44080], QWORD [rbp - 37360]
mov QWORD [rbp - 44144], QWORD [rbp - 37424]
mov QWORD [rbp - 44208], QWORD [rbp - 37488]
mov QWORD [rbp - 44272], QWORD [rbp - 37552]
mov QWORD [rbp - 44336], QWORD [rbp - 37616]
mov QWORD [rbp - 44400], QWORD [rbp - 37680]
mov QWORD [rbp - 44464], QWORD [rbp - 37744]
mov QWORD [rbp - 44528], QWORD [rbp - 37808]
mov QWORD [rbp - 44592], QWORD [rbp - 37872]
mov QWORD [rbp - 44656], QWORD [rbp - 37936]
mov QWORD [rbp - 44720], QWORD [rbp - 38000]
mov QWORD [rbp - 44784], QWORD [rbp - 38064]
mov QWORD [rbp - 44848], QWORD [rbp - 38128]
mov QWORD [rbp - 44912], QWORD [rbp - 38192]
mov QWORD [rbp - 44976], QWORD [rbp - 38256]
mov QWORD [rbp - 45040], QWORD [rbp - 38320]
mov QWORD [rbp - 45104], QWORD [rbp - 38384]
mov QWORD [rbp - 45168], QWORD [rbp - 38448]
mov QWORD [rbp - 45232], QWORD [rbp - 38512]
mov QWORD [rbp - 45296], QWORD [rbp - 38576]
mov QWORD [rbp - 45360], QWORD [rbp - 38640]
mov QWORD [rbp - 45424], QWORD [rbp - 38704]
mov QWORD [rbp - 45488], QWORD [rbp - 38768]
mov QWORD [rbp - 45552], QWORD [rbp - 38832]
mov QWORD [rbp - 45616], QWORD [rbp - 38896]
mov QWORD [rbp - 45680], QWORD [rbp - 38960]
mov QWORD [rbp - 45744], QWORD [rbp - 39024]
mov QWORD [rbp - 45808], QWORD [rbp - 39088]
mov QWORD [rbp - 45872], QWORD [rbp - 39152]
mov QWORD [rbp - 45936], QWORD [rbp - 39216]
mov QWORD [rbp - 46000], QWORD [rbp - 39280]
mov QWORD [rbp - 46064], QWORD [rbp - 39344]
mov QWORD [rbp - 46128], QWORD [rbp - 39408]
mov QWORD [rbp - 46192], QWORD [rbp - 39472]
mov QWORD [rbp - 46256], QWORD [rbp - 39536]
mov QWORD [rbp - 46320], QWORD [rbp - 39600]
mov QWORD [rbp - 46384], QWORD [rbp - 39664]
mov QWORD [rbp - 46448], QWORD [rbp - 39728]
mov QWORD [rbp - 46512], QWORD [rbp - 39792]
mov QWORD [rbp - 46576], QWORD [rbp - 39856]
mov QWORD [rbp - 46640], QWORD [rbp - 39920]
mov QWORD [rbp - 46704], QWORD [rbp - 39984]
mov QWORD [rbp - 46768], QWORD [rbp - 40048]
mov QWORD [rbp - 46832], QWORD [rbp - 40112]
mov QWORD [rbp - 46896], QWORD [rbp - 40176]
mov QWORD [rbp - 46960], QWORD [rbp - 40240]
mov QWORD [rbp - 47024], QWORD [rbp - 40304]
mov QWORD [rbp - 47088], QWORD [rbp - 40368]
mov QWORD [rbp - 47152], QWORD [rbp - 40432]
mov QWORD [rbp - 47216], QWORD [rbp - 40496]
mov QWORD [rbp - 47280], QWORD [rbp - 40560]
mov QWORD [rbp - 47344], QWORD [rbp - 40624]
mov QWORD [rbp - 47408], QWORD [rbp - 40688]
mov QWORD [rbp - 47472], QWORD [rbp - 40752]
mov QWORD [rbp - 47536], QWORD [rbp - 40816]
mov QWORD [rbp - 47600], QWORD [rbp - 40880]
mov QWORD [rbp - 47664], QWORD [rbp - 40944]
mov QWORD [rbp - 47728], QWORD [rbp - 41008]
mov QWORD [rbp - 47792], QWORD [rbp - 41072]
mov QWORD [rbp - 47856], QWORD [rbp - 41136]
mov QWORD [rbp - 47920], QWORD [rbp - 41200]
mov QWORD [rbp - 47984], QWORD [rbp - 41264]
mov QWORD [rbp - 48048], QWORD [rbp - 41328]
mov QWORD [rbp - 48112], QWORD [rbp - 41392]
mov QWORD [rbp - 48176], QWORD [rbp - 41456]
mov QWORD [rbp - 48240], QWORD [rbp - 41520]
mov QWORD [rbp - 48304], QWORD [rbp - 41584]
mov QWORD [rbp - 48368], QWORD [rbp - 41648]
mov QWORD [rbp - 48432], QWORD [rbp - 41712]
mov QWORD [rbp - 48496], QWORD [rbp - 41776]
mov QWORD [rbp - 48560], QWORD [rbp - 41840]
mov QWORD [rbp - 48624], QWORD [rbp - 41904]
mov QWORD [rbp - 48688], QWORD [rbp - 41968]
mov QWORD [rbp - 48752], QWORD [rbp - 42032]
mov QWORD [rbp - 48816], QWORD [rbp - 42096]
mov QWORD [rbp - 48880], QWORD [rbp - 42160]
mov QWORD [rbp - 48944], QWORD [rbp - 42224]
mov QWORD [rbp - 49008], QWORD [rbp - 42288]
mov QWORD [rbp - 49072], QWORD [rbp - 42352]
mov QWORD [rbp - 49136], QWORD [rbp - 42416]
mov QWORD [rbp - 49200], QWORD [rbp - 42480]
mov QWORD [rbp - 49264], QWORD [rbp - 42544]
mov QWORD [rbp - 49328], QWORD [rbp - 42608]
mov QWORD [rbp - 49392], QWORD [rbp - 42672]
mov QWORD [rbp - 49456], QWORD [rbp - 42736]
mov QWORD [rbp - 49520], QWORD [rbp - 42800]
mov QWORD [rbp - 49584], QWORD [rbp - 42864]
mov QWORD [rbp - 49648], QWORD [rbp - 42928]
mov QWORD [rbp - 49712], QWORD [rbp - 42992]
mov QWORD [rbp - 49776], QWORD [rbp - 43056]
mov QWORD [rbp - 49840], QWORD [rbp - 43120]
mov QWORD [rbp - 49904], QWORD [rbp - 43184]
mov QWORD [rbp - 49968], QWORD [rbp - 43248]
mov QWORD [rbp - 50032], QWORD [rbp - 43312]
mov QWORD [rbp - 50096], QWORD [rbp - 43376]
mov QWORD [rbp - 50160], QWORD [rbp - 43440]
mov QWORD [rbp - 50224], QWORD [rbp - 43504]
mov QWORD [rbp - 50288], QWORD [rbp - 43568]
mov QWORD [rbp - 50352], QWORD [rbp - 43632]
mov QWORD [rbp - 50416], QWORD [rbp - 43696]
mov QWORD [rbp - 50480], QWORD [rbp - 43760]
mov QWORD [rbp - 50544], QWORD [rbp - 43824]
mov QWORD [rbp - 50608], QWORD [rbp - 43888]
mov QWORD [rbp - 50672], QWORD [rbp - 43952]
mov QWORD [rbp - 50736], QWORD [rbp - 44016]
mov QWORD [rbp - 50800], QWORD [rbp - 44080]
mov QWORD [rbp - 50864], QWORD [rbp - 44144]
mov QWORD [rbp - 50928], QWORD [rbp - 44208]
mov QWORD [rbp - 50992], QWORD [rbp - 44272]
mov QWORD [rbp - 51056], QWORD [rbp - 44336]
mov QWORD [rbp - 51120], QWORD [rbp - 44400]
mov QWORD [rbp - 51184], QWORD [rbp - 44464]
mov QWORD [rbp - 51248], QWORD [rbp - 44528]
mov QWORD [rbp - 51312], QWORD [rbp - 44592]
mov QWORD [rbp - 51376], QWORD [rbp - 44656]
mov QWORD [rbp - 51440], QWORD [rbp - 44720]
mov QWORD [rbp - 51504], QWORD [rbp - 44784]
mov QWORD [rbp - 51568], QWORD [rbp - 44848]
mov QWORD [rbp - 51632], QWORD [rbp - 44912]
mov QWORD [rbp - 51696], QWORD [rbp - 44976]
mov QWORD [rbp - 51760], QWORD [rbp - 45040]
mov QWORD [rbp - 51824], QWORD [rbp - 45104]
mov QWORD [rbp - 51888], QWORD [rbp - 45168]
mov QWORD [rbp - 51952], QWORD [rbp - 45232]
mov QWORD [rbp - 52016], QWORD [rbp - 45296]
mov QWORD [rbp - 52080], QWORD [rbp - 45360]
mov QWORD [rbp - 52144], QWORD [rbp - 45424]
mov QWORD [rbp - 52208], QWORD [rbp - 45488]
mov QWORD [rbp - 52272], QWORD [rbp - 45552]
mov QWORD [rbp - 52336], QWORD [rbp - 45616]
mov QWORD [rbp - 52400], QWORD [rbp - 45680]
mov QWORD [rbp - 52464], QWORD [rbp - 45744]
mov QWORD [rbp - 52528], QWORD [rbp - 45808]
mov QWORD [rbp - 52592], QWORD [rbp - 45872]
mov QWORD [rbp - 52656], QWORD [rbp - 45936]
mov QWORD [rbp - 52720], QWORD [rbp - 46000]
mov QWORD [rbp - 52784], QWORD [rbp - 46064]
mov QWORD [rbp - 52848], QWORD [rbp - 46128]
mov QWORD [rbp - 52912], QWORD [rbp - 46192]
mov QWORD [rbp - 52976], QWORD [rbp - 46256]
mov QWORD [rbp - 53040], QWORD [rbp - 46320]
mov QWORD [rbp - 53104], QWORD [rbp - 46384]
mov QWORD [rbp - 53168], QWORD [rbp - 46448]
mov QWORD [rbp - 53232], QWORD [rbp - 46512]
mov QWORD [rbp - 53296], QWORD [rbp - 46576]
mov QWORD [rbp - 53360], QWORD [rbp - 46640]
mov QWORD [rbp - 53424], QWORD [rbp - 46704]
mov QWORD [rbp - 53488], QWORD [rbp - 46768]
mov QWORD [rbp - 53552], QWORD [rbp - 46832]
mov QWORD [rbp - 53616], QWORD [rbp - 46896]
mov QWORD [rbp - 53680], QWORD [rbp - 46960]
mov QWORD [rbp - 53744], QWORD [rbp - 47024]
mov QWORD [rbp - 53808], QWORD [rbp - 47088]
mov QWORD [rbp - 53872], QWORD [rbp - 47152]
mov QWORD [rbp - 53936], QWORD [rbp - 47216]
mov QWORD [rbp - 54000], QWORD [rbp - 47280]
mov QWORD [rbp - 54064], QWORD [rbp - 47344]
mov QWORD [rbp - 54128], QWORD [rbp - 47408]
mov QWORD [rbp - 54192], QWORD [rbp - 47472]
mov QWORD [rbp - 54256], QWORD [rbp - 47536]
mov QWORD [rbp - 54320], QWORD [rbp - 47600]
mov QWORD [rbp - 54384], QWORD [rbp - 47664]
mov QWORD [rbp - 54448], QWORD [rbp - 47728]
mov QWORD [rbp - 54512], QWORD [rbp - 47792]
mov QWORD [rbp - 54576], QWORD [rbp - 47856]
mov QWORD [rbp - 54640], QWORD [rbp - 47920]
mov QWORD [rbp - 54704], QWORD [rbp - 47984]
mov QWORD [rbp - 54768], QWORD [rbp - 48048]
mov QWORD [rbp - 54832], QWORD [rbp - 48112]
mov QWORD [rbp - 54896], QWORD [rbp - 48176]
mov QWORD [rbp - 54960], QWORD [rbp - 48240]
mov QWORD [rbp - 55024], QWORD [rbp - 48304]
mov QWORD [rbp - 55088], QWORD [rbp - 48368]
mov QWORD [rbp - 55152], QWORD [rbp - 48432]
mov QWORD [rbp - 55216], QWORD [rbp - 48496]
mov QWORD [rbp - 55280], QWORD [rbp - 48560]
mov QWORD [rbp - 55344], QWORD [rbp - 48624]
mov QWORD [rbp - 55408], QWORD [rbp - 48688]
mov QWORD [rbp - 55472], QWORD [rbp - 48752]
mov QWORD [rbp - 55536], QWORD [rbp - 48816]
mov QWORD [rbp - 55600], QWORD [rbp - 48880]
mov QWORD [rbp - 55664], QWORD [rbp - 48944]
mov QWORD [rbp - 55728], QWORD [rbp - 49008]
mov QWORD [rbp - 55792], QWORD [rbp - 49072]
mov QWORD [rbp - 55856], QWORD [rbp - 49136]
mov QWORD [rbp - 55920], QWORD [rbp - 49200]
mov QWORD [rbp - 55984], QWORD [rbp - 49264]
mov QWORD [rbp - 56048], QWORD [rbp - 49328]
mov QWORD [rbp - 56112], QWORD [rbp - 49392]
mov QWORD [rbp - 56176], QWORD [rbp - 49456]
mov QWORD [rbp - 56240], QWORD [rbp - 49520]
mov QWORD [rbp - 56304], QWORD [rbp - 49584]
mov QWORD [rbp - 56368], QWORD [rbp - 49648]
mov QWORD [rbp - 56432], QWORD [rbp - 49712]
mov QWORD [rbp - 56496], QWORD [rbp - 49776]
mov QWORD [rbp - 56560], QWORD [rbp - 49840]
mov QWORD [rbp - 56624], QWORD [rbp - 49904]
mov QWORD [rbp - 56688], QWORD [rbp - 49968]
mov QWORD [rbp - 56752], QWORD [rbp - 50032]
mov QWORD [rbp - 56816], QWORD [rbp - 50096]
mov QWORD [rbp - 56880], QWORD [rbp - 50160]
mov QWORD [rbp - 56944], QWORD [rbp - 50224]
mov QWORD [rbp - 57008], QWORD [rbp - 50288]
mov QWORD [rbp - 57072], QWORD [rbp - 50352]
mov QWORD [rbp - 57136], QWORD [rbp - 50416]
mov QWORD [rbp - 57200], QWORD [rbp - 50480]
mov QWORD [rbp - 57264], QWORD [rbp - 50544]
mov QWORD [rbp - 57328], QWORD [rbp - 50608]
mov QWORD [rbp - 57392], QWORD [rbp - 50672]
mov QWORD [rbp - 57456], QWORD [rbp - 50736]
mov QWORD [rbp - 57520], QWORD [rbp - 50800]
mov QWORD [rbp - 57584], QWORD [rbp - 50864]
mov QWORD [rbp - 57648], QWORD [rbp - 50928]
mov QWORD [rbp - 57712], QWORD [rbp - 50992]
mov QWORD [rbp - 57776], QWORD [rbp - 51056]
mov QWORD [rbp - 57840], QWORD [rbp - 51120]
mov QWORD [rbp - 57904], QWORD [rbp - 51184]
mov QWORD [rbp - 57968], QWORD [rbp - 51248]
mov QWORD [rbp - 58032], QWORD [rbp - 51312]
mov QWORD [rbp - 58096], QWORD [rbp - 51376]
mov QWORD [rbp - 58160], QWORD [rbp - 51440]
mov QWORD [rbp - 58224], QWORD [rbp - 51504]
mov QWORD [rbp - 58288], QWORD [rbp - 51568]
mov QWORD [rbp - 58352], QWORD [rbp - 51632]
mov QWORD [rbp - 58416], QWORD [rbp - 51696]
mov QWORD [rbp - 58480], QWORD [rbp - 51760]
mov QWORD [rbp - 58544], QWORD [rbp - 51824]
mov QWORD [rbp - 58608], QWORD [rbp - 51888]
mov QWORD [rbp - 58672], QWORD [rbp - 51952]
mov QWORD [rbp - 58736], QWORD [rbp - 52016]
mov QWORD [rbp - 58800], QWORD [rbp - 52080]
mov QWORD [rbp - 58864], QWORD [rbp - 52144]
mov QWORD [rbp - 58928], QWORD [rbp - 52208]
mov QWORD [rbp - 58992], QWORD [rbp - 52272]
mov QWORD [rbp - 59056], QWORD [rbp - 52336]
mov QWORD [rbp - 59120], QWORD [rbp - 52400]
mov QWORD [rbp - 59184], QWORD [rbp - 52464]
mov QWORD [rbp - 59248], QWORD [rbp - 52528]
mov QWORD [rbp - 59312], QWORD [rbp - 52592]
mov QWORD [rbp - 59376], QWORD [rbp - 52656]
mov QWORD [rbp - 59440], QWORD [rbp - 52720]
mov QWORD [rbp - 59504], QWORD [rbp - 52784]
mov QWORD [rbp - 59568], QWORD [rbp - 52848]
mov QWORD [rbp - 59632], QWORD [rbp - 52912]
mov QWORD [rbp - 59696], QWORD [rbp - 52976]
mov QWORD [rbp - 59760], QWORD [rbp - 53040]
mov QWORD [rbp - 59824], QWORD [rbp - 53104]
mov QWORD [rbp - 59888], QWORD [rbp - 53168]
mov QWORD [rbp - 59952], QWORD [rbp - 53232]
mov QWORD [rbp - 60016], QWORD [rbp - 53296]
mov QWORD [rbp - 60080], QWORD [rbp - 53360]
mov QWORD [rbp - 60144], QWORD [rbp - 53424]
mov QWORD [rbp - 60208], QWORD [rbp - 53488]
mov QWORD [rbp - 60272], QWORD [rbp - 53552]
mov QWORD [rbp - 60336], QWORD [rbp - 53616]
mov QWORD [rbp - 60400], QWORD [rbp - 53680]
mov QWORD [rbp - 60464], QWORD [rbp - 53744]
mov QWORD [rbp - 60528], QWORD [rbp - 53808]
mov QWORD [rbp - 60592], QWORD [rbp - 53872]
mov QWORD [rbp - 60656], QWORD [rbp - 53936]
mov QWORD [rbp - 60720], QWORD [rbp - 54000]
mov QWORD [rbp - 60784], QWORD [rbp - 54064]
mov QWORD [rbp - 13888], 1140457472
mov QWORD [rbp - 13904], 1140457472
mov QWORD [rbp - 13920], 1137180672
mov QWORD [rbp - 13832], QWORD [rbp - 13808]
mov QWORD [rbp - 13896], QWORD [rbp - 13872]
mov QWORD [rbp - 13960], QWORD [rbp - 13936]
sub rsp, 4
call time_seconds
pop QWORD [rbp - 13944]
mov QWORD [rbp - 13936], QWORD [rbp - 13928]
mov QWORD [rbp - 13952], 0
mov QWORD [rbp - 13952], QWORD [rbp - 13944]
mov QWORD [rbp - 13968], 0
mov QWORD [rbp - 13968], QWORD [rbp - 13960]
mov QWORD [rbp - 14000], 1065353216
sub rsp, 12
movsd xmm1, QWORD[rbp - 16]
call timer_start
add rsp, 4
pop QWORD [rbp - 14032]
pop QWORD [rbp - 14024]
pop QWORD [rbp - 14016]
mov QWORD [rbp - 14008], QWORD [rbp - 13976]
mov QWORD [rbp - 14072], QWORD [rbp - 14040]
mov QWORD [rbp - 14136], QWORD [rbp - 14104]
mov QWORD [rbp - 14040], 1
mov QWORD [rbp - 14056], 1
mov QWORD [rbp - 14056], QWORD [rbp - 14048]
mov QWORD [rbp - 14064], 1
mov rax, QWORD [rbp - 13960]]
mov rbx, QWORD [rbp - 14064]
add rax, rbx
mov QWORD [rbp - 14072], QWORD [rbp - 13960]
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
call timer_update
add rsp, 8
sub rsp, 4
movsd xmm1, QWORD[rbp - 16]
call timer_complete
add rsp, 4
pop QWORD [rbp - 14104]
movsd xmm1, QWORD[rbp - 16]
call print_int
mov QWORD [rbp - 14104], 50000
mov QWORD [rbp - 14112], 1
movsd xmm1, QWORD[rbp - 16]
call print
mov QWORD [rbp - 14120], 0
mov QWORD [rbp - 14120], QWORD [rbp - 13960]
sub rsp, 4
call mouse_down
pop QWORD [rbp - 14136]
mov QWORD [rbp - 14136], 0
mov QWORD [rbp - 14136], QWORD [rbp - 14048]
call frame_begin
movsd xmm1, QWORD[rbp - 16]
call set_color
add rsp, 4
mov QWORD [rbp - 14152], 0
mov QWORD [rbp - 14160], 0
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
call fill_rect
movsd xmm1, QWORD[rbp - 16]
call set_color
add rsp, 4
sub rsp, 4
movsd xmm1, QWORD[rbp - 16]
call _int
pop QWORD [rbp - 14208]
sub rsp, 4
movsd xmm1, QWORD[rbp - 16]
call _int
pop QWORD [rbp - 14240]
sub rsp, 4
movsd xmm1, QWORD[rbp - 16]
call _int
pop QWORD [rbp - 14264]
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
call fill_circle
movsd xmm1, QWORD[rbp - 16]
call set_color
add rsp, 4
sub rsp, 8
call get_mouse_position
add rsp, 0
pop QWORD [rbp - 14304]
pop QWORD [rbp - 14296]
mov QWORD [rbp - 14288], QWORD [rbp - 14272]
mov QWORD [rbp - 14352], QWORD [rbp - 14336]
sub rsp, 4
movsd xmm1, QWORD[rbp - 16]
call _float
pop QWORD [rbp - 14360]
sub rsp, 4
movsd xmm1, QWORD[rbp - 16]
call _float
pop QWORD [rbp - 14392]
mov QWORD [rbp - 14400], 0
mov QWORD [rbp - 14400], QWORD [rbp - 14392]
mov QWORD [rbp - 14408], 0
movsd xmm0, QWORD [rbp - 14048]
movsd xmm1, QWORD [rbp - 14408]
 xmm0, xmm1
movsd QWORD [rbp - 14416], xmm0
mov QWORD [rbp - 14432], 0
mov QWORD [rbp - 14440], 120
mov QWORD [rbp - 14448], 1
mov QWORD [rbp - 14456], QWORD [rbp - 14424]
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
call accelerate
add rsp, 8
mov rax, QWORD [rbp - 14432]]
mov rbx, QWORD [rbp - 14448]
add rax, rbx
mov rax, QWORD [rbp - 14432]]
mov rbx, QWORD [rbp - 14440]
 rax, rbx
mov QWORD [rbp - 14488], 0
mov QWORD [rbp - 14496], 120
mov QWORD [rbp - 14504], 1
mov QWORD [rbp - 14512], QWORD [rbp - 14480]
mov QWORD [rbp - 14520], 1073741824
movsd xmm0, QWORD [rbp - 13944]
movsd xmm1, QWORD [rbp - 14520]
vdivsd xmm0, xmm1
movsd QWORD [rbp - 14528], xmm0
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
call update_position
add rsp, 8
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
call constrain_to_circle
add rsp, 8
mov rax, QWORD [rbp - 14488]]
mov rbx, QWORD [rbp - 14504]
add rax, rbx
mov rax, QWORD [rbp - 14488]]
mov rbx, QWORD [rbp - 14496]
 rax, rbx
mov QWORD [rbp - 14560], 0
mov QWORD [rbp - 14568], 120
mov QWORD [rbp - 14576], 1
mov QWORD [rbp - 14584], QWORD [rbp - 14552]
mov QWORD [rbp - 14600], 0
mov QWORD [rbp - 14608], 120
mov QWORD [rbp - 14616], 1
mov QWORD [rbp - 14624], QWORD [rbp - 14592]
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
call collide
add rsp, 8
mov rax, QWORD [rbp - 14600]]
mov rbx, QWORD [rbp - 14616]
add rax, rbx
mov rax, QWORD [rbp - 14600]]
mov rbx, QWORD [rbp - 14608]
 rax, rbx
mov rax, QWORD [rbp - 14560]]
mov rbx, QWORD [rbp - 14576]
add rax, rbx
mov rax, QWORD [rbp - 14560]]
mov rbx, QWORD [rbp - 14568]
 rax, rbx
mov QWORD [rbp - 14648], 1
mov rax, QWORD [rbp - 14392]]
mov rbx, QWORD [rbp - 14648]
add rax, rbx
mov QWORD [rbp - 14656], QWORD [rbp - 14392]
mov QWORD [rbp - 14664], 2
mov rax, QWORD [rbp - 14392]]
mov rbx, QWORD [rbp - 14664]
 rax, rbx
mov QWORD [rbp - 14688], 0
mov QWORD [rbp - 14696], 120
mov QWORD [rbp - 14704], 1
mov QWORD [rbp - 14712], QWORD [rbp - 14680]
sub rsp, 4
movsd xmm1, QWORD[rbp - 16]
call _int
pop QWORD [rbp - 14752]
sub rsp, 4
movsd xmm1, QWORD[rbp - 16]
call _int
pop QWORD [rbp - 14784]
sub rsp, 4
movsd xmm1, QWORD[rbp - 16]
call _int
pop QWORD [rbp - 14808]
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
movsd xmm1, QWORD[rbp - 16]
call fill_circle
mov rax, QWORD [rbp - 14688]]
mov rbx, QWORD [rbp - 14704]
add rax, rbx
mov rax, QWORD [rbp - 14688]]
mov rbx, QWORD [rbp - 14696]
 rax, rbx
call draw
sub rsp, 4
call time_seconds
pop QWORD [rbp - 14832]
mov QWORD [rbp - 14824], QWORD [rbp - 14816]
movsd xmm0, QWORD [rbp - 14816]
movsd xmm1, QWORD [rbp - 13928]
vsubsd xmm0, xmm1
movsd QWORD [rbp - 14832], xmm0
mov QWORD [rbp - 14832], QWORD [rbp - 13944]
mov QWORD [rbp - 14816], QWORD [rbp - 13928]
mov QWORD [rbp - 14840], 1
add rsp, 7424
mov rsp, rbp
pop rbp
ret
