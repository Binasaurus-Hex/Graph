extrn printf: proc
extrn _CRT_INIT: proc
extrn ExitProcess: proc


.data
fstring db "value is : %f \n", 0
a dd 3.25


.code
main proc
  sub rbp, 28h

  call _CRT_INIT

  movss xmm1, [a]
  cvtss2sd xmm1, xmm1
  lea rcx, [fstring]
  mov rax, 1
  movq rdx, xmm1
  call printf

  mov ecx, eax     ; uExitCode = MessageBox(...)
  call ExitProcess
main endp

End