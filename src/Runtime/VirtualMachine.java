package Runtime;

import java.util.Arrays;
import java.util.Timer;

public class VirtualMachine {
    long[] stack;
    int stack_pointer = 0;
    int program_counter = 0;

    enum InstructionSet {
        ADD,
        INCREMENT,
        SUBTRACT,
        MULTIPLY,
        DIVIDE,
        LESS_THAN,
        JUMP_IF,
        JUMP,
        PRINT
    }

    public VirtualMachine(){
        long start = System.nanoTime();
        stack = new long[10000];
        long[] example_program = {(long)InstructionSet.INCREMENT.ordinal(), 0, 1, InstructionSet.LESS_THAN.ordinal(), 1, 0, 100_000_000,InstructionSet.JUMP_IF.ordinal(), 1, 0, InstructionSet.PRINT.ordinal(), 0};
        System.out.println(Arrays.toString(example_program));
        run(example_program);
        System.out.println((System.nanoTime() - start)/1e9);
    }

    void run(long[] program){
        while (program_counter < program.length){
            long instruction = program[program_counter++];
            if(instruction == InstructionSet.ADD.ordinal()){
                long memory_address = program[program_counter++];
                long a = program[program_counter++];
                long b = program[program_counter++];
                stack[(int)memory_address] = a + b;
            }
            if(instruction == InstructionSet.INCREMENT.ordinal()){
                long memory_address = program[program_counter++];
                long a = program[program_counter++];
                stack[(int)memory_address] += a;
            }
            if(instruction == InstructionSet.PRINT.ordinal()){
                long memory_address = program[program_counter++];
                System.out.println(stack[(int)memory_address]);
            }
            if(instruction == InstructionSet.JUMP.ordinal()){
                long program_address = program[program_counter++];
                program_counter = (int)program_address;
            }
            if(instruction == InstructionSet.LESS_THAN.ordinal()){
                int store_address = (int)program[program_counter++];
                long check_address = program[program_counter++];
                long comparison = program[program_counter++];
                long check_value = stack[(int)check_address];
                if(check_value < comparison) {
                    stack[store_address] = 1;
                }
                else{
                    stack[store_address] = 0;
                }
            }
            if(instruction == InstructionSet.JUMP_IF.ordinal()){
                long memory_address = program[program_counter++];
                long jump_address = program[program_counter++];
                long variable = stack[(int)memory_address];
                if(variable == 1)program_counter = (int)jump_address;
            }
        }
    }
}
