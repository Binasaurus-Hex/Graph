package Bytecode;

import SyntaxNodes.ProcedureDeclaration;
import main.BytecodeGenerator;
import main.Utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssemblyGenerator {

    static void movsd(StringBuilder builder, String register, int memory_location){
        builder.append("movsd ");
        builder.append(register);
        builder.append(", ");
        memory(builder, memory_location);
        builder.append("\n");
    }

    static void memory(StringBuilder builder, int memory_location){
        if(memory_location < 0){
            System.out.printf("");
        }
        builder.append("[rbp - ");
        builder.append(memory_location * 4);
        builder.append("]");
    }
    static void movsd(StringBuilder builder, int memory_location, String register){
        builder.append("movsd ");
        memory(builder, memory_location);
        builder.append(", ");
        builder.append(register);
        builder.append("\n");
    }

    static void float_op(StringBuilder builder, String operation, String register_a, String register_b){
        builder.append(operation);
        builder.append(" ");
        builder.append(register_a);
        builder.append(", ");
        builder.append(register_b);
        builder.append("\n");
    }

    static Method[] external_procedures = Utils.get_external_procedures();

    public static String assembly(int[] program, Map<ProcedureDeclaration, Integer> labels){
        StringBuilder builder = new StringBuilder();

        InstructionSet[] instructions = InstructionSet.values();
        int index = 0;
        while(index < program.length) {

            for(Map.Entry<ProcedureDeclaration, Integer> label : labels.entrySet()){
                if(label.getValue() == index){
                    System.out.println();
                    System.out.println(label.getKey().name);
                }
            }
            System.out.print("\t");
            InstructionSet instruction = instructions[program[index++]];
            System.out.print(instruction.name());

            switch (instruction) {
                case ALLOCATE -> {
                    int size = program[index++];
                    builder.append("sub rsp, ");
                    builder.append(size * 4);
                    builder.append("\n");
                }
                case DEALLOCATE -> {
                    int size = program[index++];
                    builder.append("add rsp, ");
                    builder.append(size * 4);
                    builder.append("\n");
                }
                case ASSIGN_LITERAL -> {
                    int memory_address = program[index++];
                    int value = program[index++];
                    builder.append("mov ");
                    memory(builder, memory_address);
                    builder.append(", ");
                    builder.append(value);
                    builder.append("\n");
                }
                case ASSIGN_RAW_DATA -> {
                    int to_memory = program[index++];
                    int size = program[index++];
                    System.out.printf("\n");
                    for(int i = 0; i < size; i++){
                        int a = program[index++];
                        System.out.printf("mov [rbp - %d], %d\n", to_memory + i, a);
                    }
                    // memcpy
                }
                case MEMSET -> {
                    int to_memory = program[index++];
                    int size = program[index++];
                    for(int i = 0; i < size; i++){
                        int a = program[index++];
                    }
                    // memset
                }

                case ASSIGN_MEMORY -> {
                    int to_memory = program[index++];
                    int from_memory = program[index++];
                    int size = program[index++];
                    for(int i = 0; i < size; i++){
                        builder.append("mov ");
                        memory(builder, from_memory + i * 4);
                        builder.append(", ");
                        memory(builder, to_memory + i * 4);
                        builder.append("\n");
                    }
                }

                case ASSIGN_VAR_FROM_LOCATION -> {
                    int to_memory = program[index++];
                    int from_location = program[index++];
                    int size = program[index++];
                }

                case ASSIGN_VAR_TO_LOCATION -> {
                    int to_location = program[index++];
                    int from_memory = program[index++];
                    int size = program[index++];
                }

                case ASSIGN_TO_LOCATION_FROM_LOCATION -> {
                    int to_location = program[index++];
                    int from_location = program[index++];
                    int size = program[index++];
                }

                case PUSH_MEMORY -> {
                    int memory_address = program[index++];
                    int size = program[index++];
                }

                case ASSIGN_POP -> {
                    int memory_address = program[index++];
                    int size = program[index++];
                }

                case ASSIGN_ADDRESS -> {
                    int memory_address = program[index++];
                    int variable = program[index++];
                }

                case ASSIGN_DEREFERENCE -> {
                    int memory_address = program[index++];
                    int pointer = program[index++];
                }

                case STRUCT_LOCATION -> {
                    int memory_address = program[index++];
                    int struct = program[index++];
                    int field = program[index++];
                }

                case STRUCT_PTR_LOCATION -> {
                    int memory_address = program[index++];
                    int struct_ptr = program[index++];
                    int field = program[index++];
                }

                case ARRAY_LOCATION -> {
                    int memory_address = program[index++];
                    int array = program[index++];
                    int i = program[index++];
                    int size = program[index++];
                }

                case ARRAY_PTR_LOCATION -> {
                    int memory_address = program[index++];
                    int array_ptr = program[index++];
                    int i = program[index++];
                    int size = program[index++];
                }

                case ADD, SUBTRACT, MULTIPLY, DIVIDE, LESS_THAN, GREATER_THAN, EQUALS, AND, OR -> {
                    int storage_location = program[index++];
                    int mem_a = (int) program[index++];
                    int mem_b = (int) program[index++];
                    builder.append("mov rax, ");
                    memory(builder, mem_a);
                    builder.append("]\n");
                    builder.append("mov rbx, ");
                    memory(builder, mem_b);
                    builder.append("\n");

                    String operation = switch (instruction){
                        case ADD -> "add";
                        case SUBTRACT -> "sub";
                        case MULTIPLY -> "imul";
                        case DIVIDE -> "idiv";
                        default -> "";
                    };

                    builder.append(operation);
                    builder.append(" rax, rbx\n");
                }

                case FLOAT_ADD, FLOAT_SUBTRACT, FLOAT_MULTIPLY, FLOAT_DIVIDE, FLOAT_LESS_THAN, FLOAT_GREATER_THAN, FLOAT_EQUALS -> {
                    int storage_location = program[index++];
                    int mem_a = program[index++];
                    int mem_b = program[index++];
                    movsd(builder, "xmm0", mem_a);
                    movsd(builder, "xmm1", mem_b);
                    String operation = switch (instruction){
                        case FLOAT_ADD -> "vaddsd";
                        case FLOAT_SUBTRACT -> "vsubsd";
                        case FLOAT_MULTIPLY -> "vmulsd";
                        case FLOAT_DIVIDE -> "vdivsd";
                        default -> "";
                    };
                    float_op(builder, operation, "xmm0", "xmm1");
                    movsd(builder, storage_location, "xmm0");
                }

                case RETURN -> {
                    builder.append("mov rsp, rbp\n");
                    builder.append("pop rbp\n");
                    builder.append("ret\n");
                }


                case PROGRAM_EXIT -> {
                    builder.append("ret\n");
                }

                case PROCEDURE_HEADER -> {
                    builder.append("push rbp\n");
                    builder.append("mov rbp, rsp\n");
                }

                case CALL_PROCEDURE -> {
                    int procedure_location = program[index++];
                    for(Map.Entry<ProcedureDeclaration, Integer> label : labels.entrySet()){
                        if(label.getValue() == procedure_location){
                            System.out.print(" " + label.getKey().name);
                        }
                    }
                }

                case CALL_EXTERNAL -> {
                    int external_location = program[index++];
                    System.out.print(" " + external_procedures[external_location].getName());

                }

                case JUMP -> {
                    int jump_location = program[index++];
                }

                case JUMP_IF -> {
                    int location = program[index++];
                    int boolean_address = program[index++];
                }
                case JUMP_IF_NOT -> {
                    int location = program[index++];
                    int boolean_address = program[index++];
                }
            }

            System.out.println();
        }

        return builder.toString();
    }
}
