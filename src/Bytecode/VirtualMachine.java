package Bytecode;

import main.Utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class VirtualMachine {

    Method[] external_procedures = Utils.get_external_procedures();

    public VirtualMachine(){
    }

    // returns the stack
    public int[] run(int[] program){
        int[] stack = new int[1000000];
        int stack_pointer = 0;
        int base_pointer = 0;
        int program_counter = 0;

        int max_stack_ptr = 0;

        InstructionSet[] instructions = InstructionSet.values();
        while (program_counter < program.length) {
            if(stack_pointer > max_stack_ptr){
                max_stack_ptr = stack_pointer;
            }
            InstructionSet instruction = instructions[program[program_counter++]];
            switch (instruction){
                case ALLOCATE -> {
                    int size = program[program_counter++];
                    stack_pointer += size;
                }

                case DEALLOCATE -> {
                    int size = program[program_counter++];
                    stack_pointer -= size;
                }
                case ASSIGN_LITERAL -> {
                    int to_memory = program[program_counter++];
                    int value = program[program_counter++];
                    stack[base_pointer + to_memory] = value;
                }
                case ASSIGN_MEMORY -> {
                    int to_memory = program[program_counter++];
                    int from_memory = program[program_counter++];
                    int size = program[program_counter++];
                    for(int i = 0; i < size; i++){
                        int value = stack[base_pointer + from_memory + i];
                        stack[base_pointer + to_memory + i] = value;
                    }
                }

                case ASSIGN_VAR_FROM_LOCATION -> {
                    int to_memory = program[program_counter++];
                    int from_location = stack[base_pointer + program[program_counter++]];
                    int size = program[program_counter++];
                    for(int i = 0; i < size; i++){
                        int addr = base_pointer + to_memory + i;
                        stack[addr] = stack[from_location + i];
                    }
                }

                case ASSIGN_VAR_TO_LOCATION -> {
                    int to_location = stack[base_pointer + program[program_counter++]];
                    int from_memory = program[program_counter++];
                    int size = program[program_counter++];
                    for(int i = 0; i < size; i++){
                        stack[to_location + i] = stack[base_pointer + from_memory + i];
                    }
                }

                case ASSIGN_TO_LOCATION_FROM_LOCATION -> {
                    int to_location = stack[base_pointer + program[program_counter++]];
                    int from_location = stack[base_pointer + program[program_counter++]];
                    int size = program[program_counter++];
                    for(int i = 0; i < size; i++){
                        stack[to_location + i] = stack[from_location + i];
                    }
                }

                case PUSH_MEMORY -> {
                    int memory_address = program[program_counter++];
                    int size = program[program_counter++];
                    for(int i = 0; i < size; i++){
                        int value = stack[base_pointer + memory_address + i];
                        stack[stack_pointer++] = value;
                    }
                }

                case ASSIGN_POP -> {
                    int memory_address = program[program_counter++];
                    int size = program[program_counter++];

                    for(int i = 0; i < size; i++){
                        stack[base_pointer + memory_address + i] = stack[stack_pointer - size + i];
                    }
                    stack_pointer = stack_pointer - size;
                }

                case ASSIGN_ADDRESS -> {
                    int memory_address = program[program_counter++];
                    int variable = program[program_counter++];
                    stack[base_pointer + memory_address] = base_pointer + variable;
                }

                case ASSIGN_DEREFERENCE -> {
                    int memory_address = program[program_counter++];
                    int pointer = program[program_counter++];
                    int value_address = stack[base_pointer + pointer];
                    stack[base_pointer + memory_address] = stack[value_address];
                }

                case STRUCT_LOCATION -> {
                    int memory_address = program[program_counter++];
                    int struct = program[program_counter++];
                    int field = program[program_counter++];
                    stack[base_pointer + memory_address] = base_pointer + struct + field;
                }

                case STRUCT_PTR_LOCATION -> {
                    int memory_address = program[program_counter++];
                    int struct_ptr = stack[base_pointer + program[program_counter++]];
                    int field = program[program_counter++];
                    stack[base_pointer + memory_address] = struct_ptr + field;
                }

                case ARRAY_LOCATION -> {
                    int memory_address = program[program_counter++];
                    int array = program[program_counter++];
                    int index = stack[base_pointer + program[program_counter++]]; // read value of index
                    int size = program[program_counter++];

                    stack[base_pointer + memory_address] = base_pointer + array + index * size;
                }

                case ARRAY_PTR_LOCATION -> {
                    int memory_address = program[program_counter++];
                    int array_ptr = stack[base_pointer + program[program_counter++]];
                    int index = stack[base_pointer + program[program_counter++]]; // read value of index
                    int size = program[program_counter++];

                    stack[base_pointer + memory_address] = array_ptr + index * size;
                }

                case ADD, SUBTRACT, MULTIPLY, DIVIDE, LESS_THAN, GREATER_THAN, EQUALS, AND -> {
                    int storage_location = (int)program[program_counter++];
                    int mem_a = (int)program[program_counter++];
                    int mem_b = (int)program[program_counter++];
                    int a = stack[base_pointer + mem_a];
                    int b = stack[base_pointer + mem_b];
                    int result = switch (instruction){
                        case LESS_THAN -> a < b ? 1 : 0;
                        case GREATER_THAN -> a > b ? 1 : 0;
                        case EQUALS -> a == b ? 1 : 0;
                        case AND -> a == 1 && b == 1 ? 1 : 0;
                        case ADD -> a + b;
                        case SUBTRACT -> a - b;
                        case MULTIPLY -> a * b;
                        case DIVIDE -> a / b;
                        default -> 0;
                    };
                    stack[base_pointer + storage_location] = result;
                }

                case FLOAT_ADD, FLOAT_SUBTRACT, FLOAT_MULTIPLY, FLOAT_DIVIDE, FLOAT_LESS_THAN, FLOAT_GREATER_THAN, FLOAT_EQUALS -> {
                    int storage_location = program[program_counter++];
                    int mem_a = program[program_counter++];
                    int mem_b = program[program_counter++];
                    float a = Float.intBitsToFloat(stack[base_pointer + mem_a]);
                    float b = Float.intBitsToFloat(stack[base_pointer + mem_b]);
                    float result = switch (instruction){
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
                    stack[base_pointer + storage_location] = (!comparison)? Float.floatToIntBits(result) : (int)result;
                }

                case RETURN -> {
                    int inputs_size = program[program_counter];
                    stack_pointer = base_pointer;
                    int previous_base_ptr = stack[--stack_pointer];
                    int return_location = stack[--stack_pointer];
                    base_pointer = previous_base_ptr;
                    program_counter = return_location;
                    stack_pointer -= inputs_size;
                }

                case PROGRAM_EXIT -> {
                    return stack;
                }

                case PROCEDURE_HEADER -> {
                    stack[stack_pointer++] = base_pointer;
                    base_pointer = stack_pointer;
                }

                case CALL_PROCEDURE -> {
                    int procedure_location = program[program_counter++];
                    stack[stack_pointer++] = program_counter; // where to return to
                    program_counter = procedure_location; // jump to function
                }

                case JUMP -> {
                    program_counter = program[program_counter];
                }

                case JUMP_IF -> {
                    int location = program[program_counter++];
                    int boolean_address = program[program_counter++];
                    if(stack[base_pointer + boolean_address] == 1){
                        program_counter = location;
                    }
                }
                case JUMP_IF_NOT -> {
                    int location = program[program_counter++];
                    int boolean_address = program[program_counter++];
                    if(stack[base_pointer + boolean_address] != 1){
                        program_counter = location;
                    }
                }

                case CALL_EXTERNAL -> {
                    int external_index = program[program_counter++];
                    Method external_procedure = external_procedures[external_index];
                    Parameter[] parameters = external_procedure.getParameters();
                    Object[] args = new Object[external_procedure.getParameters().length];

                    int offset = stack_pointer;
                    for(int i = parameters.length - 1; i >= 0; i--){
                        Parameter parameter = parameters[i];
                        int size = 1; // assumed
                        offset -= size;
                        int value = stack[offset];
                        String type = parameter.getType().getName();
                        switch (type){
                            case "bool", "int" ->{
                                args[i] = value;
                            }
                            case "float"->{
                                args[i] = Float.intBitsToFloat(value);
                            }
                        }
                    }
                    try {
                        Object result = external_procedure.invoke(null, args);
                        String return_type = external_procedure.getReturnType().getName();
                        offset -= 1;
                        switch (return_type){
                            case "float" -> {
                                Float double_result = (Float) result;
                                stack[offset] = Float.floatToIntBits(double_result);
                            }
                            case "int" -> {
                                int int_result = (Integer)result;
                                stack[offset] = int_result;
                            }
                            case "boolean" -> {
                                int bool_result = ((Boolean)result)? 1 : 0;
                                stack[offset] = bool_result;
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                    stack_pointer -= parameters.length; // size assumed 1
                }
            }
        }
        return stack;
    }
}
