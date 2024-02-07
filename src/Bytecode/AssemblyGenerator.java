package Bytecode;

import SyntaxNodes.ProcedureDeclaration;
import main.Utils;

import java.lang.reflect.Method;
import java.util.*;

public class AssemblyGenerator {

    static class DLL_Import {
        DLL_Import(String name, List<String> procedures){this.name = name; this.procedures = procedures;}
        String name;
        List<String> procedures;
    }

    static void declare_dlls(StringBuilder import_section, ArrayList<DLL_Import> dlls){
        for(DLL_Import dll : dlls){
            String name = dll.name.toLowerCase();
            import_section.append(String.format("dd 0,0,0,RVA %s_name,RVA %s_table\n", name, name));
        }
        import_section.append("dd 0,0,0,0,0\n");

        for(DLL_Import dll : dlls){
            import_section.append(String.format("%s_table:\n", dll.name.toLowerCase()));
            for(String procedure_name : dll.procedures){
                import_section.append(String.format("%s dq RVA _%s\n", procedure_name, procedure_name));
            }
            import_section.append("dq 0\n");
        }

        for(DLL_Import dll: dlls){
            import_section.append(String.format("%s_name db '%s',0\n", dll.name.toLowerCase(), dll.name));
        }
        for(DLL_Import dll : dlls){
            for (String procedure_name : dll.procedures){
                import_section.append(String.format("_%s dw 0\ndb '%s',0\n", procedure_name, procedure_name));
            }
        }
    }

    static void declare_print_float(StringBuilder builder){
        builder.append("""
                  print_float:
                  push rbp
                  mov rbp, rsp
                  sub rsp, 4 * 8
                  mov rcx, _float_format
                  mov rdx, [rbp + 2 * 8]
                  call [printf]
                  leave
                  ret
                  """);
    }

    static void declare_print_int(StringBuilder builder){
        builder.append("""
                  print_int:
                  push rbp
                  mov rbp, rsp
                  sub rsp, 4 * 8
                  mov rcx, _int_format
                  mov rdx, [rbp + 2 * 8]
                  call [printf]
                  leave
                  ret
                  """);
    }

    static void program_header(StringBuilder builder){
        builder.append("""
format PE64
entry start
                    
section '.text' code readable executable""");
    }

    static void movsd(StringBuilder builder, String register, int memory_location){
        builder.append("movsd ");
        builder.append(register);
        builder.append(", ");
        memory(builder, memory_location);
        builder.append("\n");
    }

    static void memory(StringBuilder builder, int memory_location){
        builder.append("QWORD [rbp - ");
        builder.append((memory_location + 1) * 8);
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
        StringBuilder program_text = new StringBuilder();
        program_text.append("format PE64\n");
        program_text.append("entry main\n");

        StringBuilder text_segment = new StringBuilder();
        text_segment.append("section '.text' code readable executable\n");
        declare_print_float(text_segment);
        declare_print_int(text_segment);

        StringBuilder data_segment = new StringBuilder();
        data_segment.append("section '.data' data readable writeable\n");
        data_segment.append("_float_format db '%f',  0\n");
        data_segment.append("_int_format db '%d',  0\n");
        StringBuilder import_segment = new StringBuilder();
        import_segment.append("section '.idata' import data readable writeable\n");

        InstructionSet[] instructions = InstructionSet.values();
        int index = 0;
        String current_label = null;
        while(index < program.length) {

            for(Map.Entry<ProcedureDeclaration, Integer> label : labels.entrySet()){
                if(label.getValue() == index){;
                    current_label = label.getKey().name;
                    text_segment.append(current_label).append(":\n");
                }
            }
            InstructionSet instruction = instructions[program[index++]];

            switch (instruction) {
                case ALLOCATE -> {
                    int size = program[index++];
                    text_segment.append("sub rsp, ");
                    text_segment.append(size * 8);
                    text_segment.append("\n");
                }
                case DEALLOCATE -> {
                    int size = program[index++];
                    text_segment.append("add rsp, ");
                    text_segment.append(size * 8);
                    text_segment.append("\n");
                }
                case ASSIGN_LITERAL -> {
                    int memory_address = program[index++];
                    int value = program[index++];
                    text_segment.append("mov ");
                    memory(text_segment, memory_address);
                    text_segment.append(", ");
                    text_segment.append(value);
                    text_segment.append("\n");
                }
                case ASSIGN_RAW_DATA -> {
                    int to_memory = program[index++];
                    int size = program[index++];
                    for(int i = 0; i < size; i++){
                        int a = program[index++];
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
                        text_segment.append("mov ");
                        memory(text_segment, from_memory + i * 8);
                        text_segment.append(", ");
                        memory(text_segment, to_memory + i * 8);
                        text_segment.append("\n");
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
                    for(int i = 0; i < size; i++){
                        text_segment.append("push ");
                        memory(text_segment, memory_address + i);
                        text_segment.append("\n");
                    }
                }

                case ASSIGN_POP -> {
                    int memory_address = program[index++];
                    int size = program[index++];
                    for(int i = 0; i < size; i++){
                        text_segment.append("pop ");
                        memory(text_segment, memory_address + size - i);
                        text_segment.append("\n");
                    }
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

                    text_segment.append("mov rax, ");
                    memory(text_segment, mem_a);
                    text_segment.append("\n");

                    text_segment.append("mov rbx, ");
                    memory(text_segment, mem_b);
                    text_segment.append("\n");

                    String operation = switch (instruction){
                        case ADD -> "add";
                        case SUBTRACT -> "sub";
                        case MULTIPLY -> "imul";
                        case DIVIDE -> "idiv";
                        default -> "";
                    };
                    if(instruction == InstructionSet.DIVIDE){
                        text_segment.append("xor rdx, rdx\n");
                    }
                    text_segment.append(operation);
                    if(instruction == InstructionSet.DIVIDE){
                        text_segment.append(" rbx\n");
                    }
                    else{
                        text_segment.append(" rax, rbx\n");
                    }
                    text_segment.append("mov "); memory(text_segment, storage_location); text_segment.append(", rax\n");
                }

                case FLOAT_ADD, FLOAT_SUBTRACT, FLOAT_MULTIPLY, FLOAT_DIVIDE, FLOAT_LESS_THAN, FLOAT_GREATER_THAN, FLOAT_EQUALS -> {
                    int storage_location = program[index++];
                    int mem_a = program[index++];
                    int mem_b = program[index++];
                    movsd(text_segment, "xmm0", mem_a);
                    movsd(text_segment, "xmm1", mem_b);
                    String operation = switch (instruction){
                        case FLOAT_ADD -> "addsd";
                        case FLOAT_SUBTRACT -> "subsd";
                        case FLOAT_MULTIPLY -> "mulsd";
                        case FLOAT_DIVIDE -> "divsd";
                        default -> "";
                    };
                    float_op(text_segment, operation, "xmm0", "xmm1");
                    movsd(text_segment, storage_location, "xmm0");
                }

                case RETURN -> {
                    text_segment.append("leave\n");
                    text_segment.append("ret\n");
                }


                case PROGRAM_EXIT -> {
                    text_segment.append("xor rax, rax\n");
                    text_segment.append("call ExitProcess\n");
                }

                case PROCEDURE_HEADER -> {
                    text_segment.append("push rbp\n");
                    text_segment.append("mov rbp, rsp\n");
                }

                case CALL_PROCEDURE -> {
                    int procedure_location = program[index++];
                    for(Map.Entry<ProcedureDeclaration, Integer> label : labels.entrySet()){
                        if(label.getValue() == procedure_location){
                            text_segment.append("call ");
                            text_segment.append(label.getKey().name);
                            text_segment.append("\n");
                        }
                    }
                }

                case CALL_EXTERNAL -> {
                    int external_location = program[index++];;
                    text_segment.append("call ");
                    text_segment.append(external_procedures[external_location].getName());
                    text_segment.append("\n");
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
        }

        program_text.append(text_segment);
        program_text.append(data_segment);

        ArrayList<DLL_Import> dlls = new ArrayList<>();
        dlls.add(new DLL_Import("KERNEL32.DLL", Arrays.asList("ExitProcess")));
        dlls.add(new DLL_Import("MSVCRT.DLL", Arrays.asList("printf")));
        declare_dlls(import_segment, dlls);

        program_text.append(import_segment);

        return program_text.toString();
    }
}
