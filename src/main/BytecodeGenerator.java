package main;

import Bytecode.InstructionSet;
import SyntaxNodes.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BytecodeGenerator {

    Map<String, Long> externals = new HashMap<>();

    class Scope {
        Map<String, String> types = new HashMap<>();
        Map<String, Long> locals = new HashMap<>(); // variable name to stack offset
        Map<String, Long> labels = new HashMap<>(); // function name to program index
    }

    class Context {
        Scope scope = new Scope();
        long stack_offset;
    }

    BytecodeProgram generate_bytecode(List<Node> program){
        int entry_point = -1;
        Context global_context = new Context();
        Scope global_scope = new Scope();
        global_context.scope = global_scope;

        // declare externals
        long external_id = 0;
        for(Method method : ExternalProcedures.class.getDeclaredMethods()){
            externals.put(method.getName(), external_id++);
        }

        List<Long> bytecode = new ArrayList<>();

        // procedures
        for(Node node : program){
            if(node instanceof ProcedureDeclaration){
                ProcedureDeclaration procedure = (ProcedureDeclaration) node;
                long program_line = bytecode.size();
                if(procedure.name.equals("main")){
                    entry_point = (int)program_line;
                }
                global_scope.labels.put(procedure.name, program_line);
                Context context = new Context();
                long input_offset = -1;
                for(int i = procedure.inputs.size() - 1; i >= 0; i--){
                    VariableDeclaration declaration = (VariableDeclaration) procedure.inputs.get(i);
                    long size = get_size(declaration.type);
                    input_offset -= size;
                    context.scope.locals.put(declaration.name, input_offset);
                }
                context.scope.labels.putAll(global_context.scope.labels); // add all the current functions in
                generate_bytecode(procedure.block, bytecode, context);
                if(!procedure.name.equals("main")){
                    bytecode.add(InstructionSet.POP_FRAME.code());
                }
            }
        }

        long[] final_bytecode = new long[bytecode.size()];
        for(int i = 0; i < bytecode.size(); i++){
            final_bytecode[i] = bytecode.get(i);
        }

        BytecodeProgram bytecode_program = new BytecodeProgram();
        bytecode_program.code = final_bytecode;
        bytecode_program.entry_point = entry_point;
        return bytecode_program;
    }

    long get_size(String type){
        switch (type){
            case "int":
            case "float":
                return 1;
        }
        return -1; // error
    }

    void generate_bytecode(List<Node> program, List<Long> bytecode, Context context){
        for(Node node : program){
            if(node instanceof VariableDeclaration){
                VariableDeclaration declaration = (VariableDeclaration) node;
                long size = get_size(declaration.type);
                bytecode.add(InstructionSet.ALLOCATE.code());
                bytecode.add(size);
                long memory_address = context.stack_offset;
                context.stack_offset += size;
                context.scope.locals.put(declaration.name, memory_address);
                context.scope.types.put(declaration.name, declaration.type);
            }
            if(node instanceof VariableAssign){
                VariableAssign assign = (VariableAssign) node;
                String type = context.scope.types.get(assign.variable_name);
                long memory_address = context.scope.locals.get(assign.variable_name);

                if(assign.value instanceof Literal){
                    long value = 0;
                    if(type.equals("float")){
                        Literal<Float> float_literal = (Literal<Float>) assign.value;
                        value = Float.floatToIntBits(float_literal.value);
                    }
                    else if(type.equals("int")){
                        Literal<Integer> int_literal = (Literal<Integer>) assign.value;
                        value = int_literal.value;
                    }

                    bytecode.add(InstructionSet.ASSIGN_LITERAL.code());
                    bytecode.add(memory_address);
                    bytecode.add(value);
                }
                else if(assign.value instanceof VariableCall){
                    VariableCall variable_call = (VariableCall) assign.value;
                    bytecode.add(InstructionSet.ASSIGN_MEMORY.code());
                    bytecode.add(memory_address);
                    bytecode.add(context.scope.locals.get(variable_call.name));
                }
                else if(assign.value instanceof BinaryOperator){
                    BinaryOperator operator = (BinaryOperator) assign.value;
                    VariableCall a = (VariableCall)operator.left;
                    VariableCall b = (VariableCall)operator.right;

                    String value_type = a.type; // assumed to be the same type

                    long mem_a = context.scope.locals.get(a.name);
                    long mem_b = context.scope.locals.get(b.name);

                    long code;
                    switch (operator.operation){
                        case ADD -> {
                            if(value_type.equals("int")) code = InstructionSet.ADD.code();
                            else code = InstructionSet.FLOAT_ADD.code();
                        }
                        case SUBTRACT -> {
                            if(value_type.equals("int")) code = InstructionSet.SUBTRACT.code();
                            else code = InstructionSet.FLOAT_SUBTRACT.code();
                        }
                        case MULTIPLY -> {
                            if(value_type.equals("int")) code = InstructionSet.MULTIPLY.code();
                            else code = InstructionSet.FLOAT_MULTIPLY.code();
                        }
                        case DIVIDE -> {
                            if(value_type.equals("int")) code = InstructionSet.DIVIDE.code();
                            else code = InstructionSet.FLOAT_DIVIDE.code();
                        }
                        default -> code = -1;
                    };

                    bytecode.add(code);
                    bytecode.add(memory_address);
                    bytecode.add(mem_a);
                    bytecode.add(mem_b);
                }
            }

            if(node instanceof ProcedureCall){
                ProcedureCall call = (ProcedureCall)node;
                for(Node input : call.inputs){
                    VariableCall variable_call = (VariableCall) input; // assumed

                    // copy inputs
                    long memory_location = context.stack_offset;
                    long size = get_size(variable_call.type);
                    bytecode.add(InstructionSet.ALLOCATE.code());
                    bytecode.add(size);
                    context.stack_offset += size;

                    bytecode.add(InstructionSet.ASSIGN_MEMORY.code());
                    bytecode.add(memory_location);
                    bytecode.add(context.scope.locals.get(variable_call.name));
                }

                if(call.external){
                    bytecode.add(InstructionSet.CALL_EXTERNAL.code());
                    bytecode.add(externals.get(call.name));
                }
                else{
                    bytecode.add(InstructionSet.PUSH_FRAME.code());
                    long procedure_location = context.scope.labels.get(call.name);
                    bytecode.add(InstructionSet.JUMP.code());
                    bytecode.add(procedure_location);
                }
            }
        }
    }
}
