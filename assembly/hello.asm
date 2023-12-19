GetStdHandle            proto               ; Writes I/O handles
WriteConsoleA           proto               ; Writes to Command Window
ReadConsoleA            proto               ; Reads character input from the console input buffer and removes it from the buffer
ExitProcess             proto               ; Returns CPU Control to Windows
SetConsoleTextAttribute proto               ; Sets colors for text and background
Console                 equ     -11         ; The standard output device code
                                            ; https://docs.microsoft.com/en-us/windows/console/getstdhandle





.code                                       ; Marks the start of Code section
main        proc                            ; Start of procedure named 'main'
            sub         rbp, 40             ; Reserve 40 bytes of Shadow Space


            ; Gets the STD output handle
            mov         rcx, Console        ; Dword nStdHandle:
            call        GetStdHandle        ; Returned handle is in RAX
            mov         stdout, rax         ; stores the handle for later use


            ; Sets the text & background colors
            mov         rcx, stdout                 ; handle hConsoleOutput as the 1st input
            mov         rdx, 9Fh                    ; wAttributes as the second input
            call        SetConsoleTextAttribute     ; Sets the attributes of characters written to the console screen buffer


            ; Welcome Message
            mov         rcx, stdout                 ; hConsoleOutput
            lea         rdx, welcomeMsg             ; *lpBuffer
            mov         r8, lengthof welcomeMsg     ; nNumberOfCharsToWrite
            lea         r9, nBytesWritten           ; lpNumberOfCharsWritten
            call        WriteConsoleA





            ; ReSets the text & background colors
            mov         rcx, stdout                 ; handle hConsoleOutput as the 1st input
            mov         rdx, 07h                    ; wAttributes as the second input
            call        SetConsoleTextAttribute     ; Sets the attributes of characters written to the console screen buffer


            ; Exit process
            mov         rcx, 0              ; Set the Exit Code to 0
            add         rbp, 40             ; Release 40 bytes of Shadow Space
            call        ExitProcess         ; Hand back the CPU control to Windows
main        endp                            ; End of procedure named 'main'



.data                                                   ; Marks the start of Data section
stdout              qword       ?                       ; Reserves space for handel to stdOut
nBytesWritten       qword       ?                       ; Reserved space for number of bytes written
welcomeMsg          byte        " Welcome ", 0Ah, 0Dh     ; First message written to the screen

                                end                     ; End of program