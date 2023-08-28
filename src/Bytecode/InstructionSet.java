package Bytecode;

public enum InstructionSet {
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE,

    FLOAT_ADD,
    FLOAT_SUBTRACT,
    FLOAT_MULTIPLY,
    FLOAT_DIVIDE,

    LESS_THAN,
    JUMP_IF,

    JUMP,
    RETURN,
    PUSH_FRAME,
    POP_FRAME,
    CALL_EXTERNAL,
    PRINT,
    ALLOCATE,
    DEALLOCATE,
    ASSIGN_LITERAL,
    ASSIGN_MEMORY;

    public long code(){
        return (long)ordinal();
    }
}
