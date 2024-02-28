#pragma once

enum InstructionSet {

    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE,
    LESS_THAN,
    EQUALS,
    GREATER_THAN,

    AND,
    OR,

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
    JUMP,
    CALL_PROCEDURE,
    RETURN,
    PROGRAM_EXIT,
    CALL_EXTERNAL,
    ALLOCATE,
    DEALLOCATE,
    ASSIGN_LITERAL,
    ASSIGN_RAW_DATA,
    MEMSET,
    ASSIGN_MEMORY,
    ASSIGN_POP,
    ASSIGN_ADDRESS,
    ASSIGN_DEREFERENCE,

    ASSIGN_VAR_FROM_LOCATION,
    ASSIGN_VAR_TO_LOCATION,
    ASSIGN_TO_LOCATION_FROM_LOCATION,
    STRUCT_PTR_LOCATION,
    STRUCT_LOCATION,
    ARRAY_LOCATION,
    ARRAY_PTR_LOCATION,
    PUSH_MEMORY
};