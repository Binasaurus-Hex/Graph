package Bytecode;

import jdk.jshell.EvalException;
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

                case DEALLOCATE -> {
                    long size = program[program_counter++];
                    stack_pointer -= size;
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

                case PUSH_MEMORY -> {
                    int memory_address = (int)program[program_counter++];
                    long value = stack[base_pointer + memory_address];
                    stack[stack_pointer++] = value;
                }

                case ASSIGN_POP -> {
                    int memory_address = (int)program[program_counter++];
                    stack[base_pointer + memory_address] = stack[--stack_pointer];
                }

                case ASSIGN_ADDRESS -> {
                    int memory_address = (int)program[program_counter++];
                    int variable = (int)program[program_counter++];
                    stack[base_pointer + memory_address] = base_pointer + variable;
                }

                case ASSIGN_DEREFERENCE -> {
                    int memory_address = (int)program[program_counter++];
                    int pointer = (int)program[program_counter++];
                    int value_address = (int)stack[base_pointer + pointer];
                    stack[base_pointer + memory_address] = stack[value_address];
                }

                case ASSIGN_ARRAY_INDEX -> {
                    int memory_address = (int)program[program_counter++];
                    int pointer_address = (int)program[program_counter++];
                    int pointer = (int)stack[base_pointer+pointer_address];

                    int offset_address = (int)program[program_counter++];
                    int offset = (int)stack[base_pointer + offset_address];
                    pointer += offset;
                    stack[base_pointer + memory_address] = stack[pointer];
                }

                case ADD, SUBTRACT, MULTIPLY, DIVIDE, LESS_THAN, GREATER_THAN, EQUALS -> {
                    int storage_location = (int)program[program_counter++];
                    int mem_a = (int)program[program_counter++];
                    int mem_b = (int)program[program_counter++];
                    long a = stack[base_pointer + mem_a];
                    long b = stack[base_pointer + mem_b];
                    long result = switch (instruction){
                        case LESS_THAN -> a < b ? 1 : 0;
                        case GREATER_THAN -> a > b ? 1 : 0;
                        case EQUALS -> a == b ? 1 : 0;
                        case ADD -> a + b;
                        case SUBTRACT -> a - b;
                        case MULTIPLY -> a * b;
                        case DIVIDE -> a / b;
                        default -> 0;
                    };
                    stack[base_pointer + storage_location] = result;
                }

                case FLOAT_ADD, FLOAT_SUBTRACT, FLOAT_MULTIPLY, FLOAT_DIVIDE, FLOAT_LESS_THAN, FLOAT_GREATER_THAN, FLOAT_EQUALS -> {
                    int storage_location = (int)program[program_counter++];
                    int mem_a = (int)program[program_counter++];
                    int mem_b = (int)program[program_counter++];
                    double a = Double.longBitsToDouble(stack[base_pointer + mem_a]);
                    double b = Double.longBitsToDouble(stack[base_pointer + mem_b]);
                    double result = switch (instruction){
                        case FLOAT_LESS_THAN -> a < b ? 1 : 0;
                        case FLOAT_GREATER_THAN -> a > b ? 1 : 0;
                        case FLOAT_EQUALS -> a == b ? 1 : 0;
                        case FLOAT_ADD -> a + b;
                        case FLOAT_SUBTRACT -> a - b;
                        case FLOAT_MULTIPLY -> a * b;
                        case FLOAT_DIVIDE -> a / b;
                        default -> 0;
                    };

                    boolean comparison = switch (instruction){
                        case FLOAT_LESS_THAN, FLOAT_GREATER_THAN, FLOAT_EQUALS -> true;
                        default -> false;
                    };
                    stack[base_pointer + storage_location] = (!comparison)? Double.doubleToLongBits(result) : (long)result;
                }

                case RETURN -> {
                    int inputs_size = (int)program[program_counter++];
                    stack_pointer = base_pointer;
                    int previous_base_ptr = (int)stack[--stack_pointer];
                    int return_location = (int)stack[--stack_pointer];
                    base_pointer = previous_base_ptr;
                    program_counter = return_location;
                    stack_pointer -= inputs_size;
                }

                case PROCEDURE_HEADER -> {
                    stack[stack_pointer++] = base_pointer;
                    base_pointer = stack_pointer;
                }

                case CALL_PROCEDURE -> {
                    int procedure_location = (int)program[program_counter++];
                    stack[stack_pointer++] = program_counter; // where to return to
                    program_counter = procedure_location; // jump to function
                }

                case JUMP -> {
                    int location = (int)program[program_counter++];
                    program_counter = location;
                }

                case JUMP_IF -> {
                    int location = (int)program[program_counter++];
                    int boolean_address = (int)program[program_counter++];
                    if(stack[base_pointer + boolean_address] == 1){
                        program_counter = location;
                    }
                }
                case JUMP_IF_NOT -> {
                    int location = (int)program[program_counter++];
                    int boolean_address = (int)program[program_counter++];
                    if(stack[base_pointer + boolean_address] != 1){
                        program_counter = location;
                    }
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
                            case "long"->{
                                args[i] = value;
                            }
                            case "float"->{
                                args[i] = Double.longBitsToDouble(value);
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

                    stack_pointer -= parameters.length; // size assumed 1
                }
            }
        }
    }
}
