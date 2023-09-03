package Bytecode;

public enum InstructionSet {
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE,
    LESS_THAN,
    EQUALS,
    GREATER_THAN,

    FLOAT_ADD,
    FLOAT_SUBTRACT,
    FLOAT_MULTIPLY,
    FLOAT_DIVIDE,
    FLOAT_LESS_THAN,
    FLOAT_EQUALS,
    FLOAT_GREATER_THAN,

    JUMP_IF,
    JUMP_IF_NOT,

    PROCEDURE_HEADER,
    SET_BASE_PTR,


    JUMP,
    CALL_PROCEDURE,
    RETURN,
    CALL_EXTERNAL,
    PRINT,
    ALLOCATE,
    DEALLOCATE,
    ASSIGN_LITERAL,
    ASSIGN_MEMORY,
    ASSIGN_POP,
    ASSIGN_ADDRESS,
    ASSIGN_DEREFERENCE,
    ASSIGN_ARRAY_INDEX,

    PUSH_MEMORY;

    public long code(){
        return (long)ordinal();
    }
}
