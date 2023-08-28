package Bytecode;

import main.ExternalProcedures;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class VirtualMachine {

    public VirtualMachine(){
    }

    public void run(long[] program, int start){
        long[] stack = new long[10000];
        int stack_pointer = 0;
        int base_pointer = 0;
        int program_counter = start;

        InstructionSet[] instructions = InstructionSet.values();
        while (program_counter < program.length) {
            InstructionSet instruction = instructions[(int)program[program_counter++]];
            switch (instruction){
                case ALLOCATE -> {
                    long size = program[program_counter++];
                    stack_pointer += size;
                }
                case ASSIGN_LITERAL -> {
                    int to_memory = (int)program[program_counter++];
                    long value = program[program_counter++];
                    stack[base_pointer + to_memory] = value;
                }
                case ASSIGN_MEMORY -> {
                    int to_memory = (int)program[program_counter++];
                    int from_memory = (int)program[program_counter++];
                    long value = stack[base_pointer + from_memory];
                    stack[base_pointer + to_memory] = value;
                }
                case ADD, SUBTRACT, MULTIPLY, DIVIDE -> {
                    int storage_location = (int)program[program_counter++];
                    int mem_a = (int)program[program_counter++];
                    int mem_b = (int)program[program_counter++];
                    long a = stack[base_pointer + mem_a];
                    long b = stack[base_pointer + mem_b];
                    long result = switch (instruction){
                        case ADD -> a + b;
                        case SUBTRACT -> a - b;
                        case MULTIPLY -> a * b;
                        case DIVIDE -> a / b;
                        default -> 0;
                    };
                    stack[base_pointer + storage_location] = result;
                }

                case FLOAT_ADD, FLOAT_SUBTRACT, FLOAT_MULTIPLY, FLOAT_DIVIDE -> {
                    int storage_location = (int)program[program_counter++];
                    int mem_a = (int)program[program_counter++];
                    int mem_b = (int)program[program_counter++];
                    float a = Float.intBitsToFloat((int)stack[base_pointer + mem_a]);
                    float b = Float.intBitsToFloat((int)stack[base_pointer + mem_b]);
                    float result = switch (instruction){
                        case FLOAT_ADD -> a + b;
                        case FLOAT_SUBTRACT -> a - b;
                        case FLOAT_MULTIPLY -> a * b;
                        case FLOAT_DIVIDE -> a / b;
                        default -> 0;
                    };
                    stack[base_pointer + storage_location] = Float.floatToIntBits(result);
                }

                case PUSH_FRAME -> {
                    stack[stack_pointer++] = program_counter + 2;
                    base_pointer = stack_pointer;
                }
                case POP_FRAME -> {
                    stack_pointer = base_pointer;
                    program_counter = (int)stack[--stack_pointer];
                }

                case JUMP -> {
                    int location = (int)program[program_counter++];
                    program_counter = location;
                }

                case CALL_EXTERNAL -> {
                    int external_index = (int)program[program_counter++];
                    Method external_procedure = ExternalProcedures.class.getDeclaredMethods()[external_index];
                    Parameter[] parameters = external_procedure.getParameters();
                    Object[] args = new Object[external_procedure.getParameters().length];

                    int offset = stack_pointer;
                    for(int i = parameters.length - 1; i >= 0; i--){
                        Parameter parameter = parameters[i];
                        int size = 1; // assumed
                        offset -= size;
                        long value = stack[offset];
                        String type = parameter.getType().getName();
                        switch (type){
                            case "int"->{
                                args[i] = (int)value;
                            }
                            case "float"->{
                                args[i] = Float.intBitsToFloat((int)value);
                            }
                        }
                    }
                    try {
                        external_procedure.invoke(null, args);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
