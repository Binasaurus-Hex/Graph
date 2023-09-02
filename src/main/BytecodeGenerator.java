package main;

import Bytecode.InstructionSet;
import SyntaxNodes.*;

import java.lang.reflect.Method;
import java.util.*;

public class BytecodeGenerator {

    Map<String, Long> externals = new HashMap<>();

    class Scope {
        Map<String, String> types = new HashMap<>();
        Map<String, Long> locals = new HashMap<>(); // variable name to stack offset
        Map<String, Long> labels = new HashMap<>(); // function name to program index
        Map<String, ProcedureDeclaration> procedures = new HashMap<>();
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
                global_scope.procedures.put(procedure.name, procedure);
                Context context = new Context();
                long input_offset = -2;
                for(int i = procedure.inputs.size() - 1; i >= 0; i--){
                    VariableDeclaration declaration = (VariableDeclaration) procedure.inputs.get(i);
                    long size = get_size(declaration.type);
                    input_offset -= size;
                    context.scope.locals.put(declaration.name, input_offset);
                }
                for(int i = procedure.outputs.size() -1; i >= 0; i--){
                    String type = procedure.outputs.get(i);
                    long size = get_size(type);
                    input_offset -= size;
                    context.scope.locals.put(String.format("<-%d", procedure.outputs.size() - i - 1), input_offset);
                }

                bytecode.add(InstructionSet.PROCEDURE_HEADER.code());
                context.scope.labels.putAll(global_context.scope.labels); // add all the current functions in
                context.scope.procedures.putAll(global_context.scope.procedures);
                generate_bytecode(procedure.block, bytecode, context);
                if(!procedure.name.equals("main")){
                    bytecode.add(InstructionSet.RETURN.code());
                    bytecode.add((long)procedure.inputs.size());
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
        if(type == null){
            System.out.println("");
        }
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
                    else if(type.equals("bool")){
                        Literal<Boolean> bool_literal = (Literal<Boolean>) assign.value;
                        value = bool_literal.value? 1 : 0;
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
                        case LESS_THAN -> {
                            if(value_type.equals("int"))code = InstructionSet.LESS_THAN.code();
                            else code = InstructionSet.FLOAT_LESS_THAN.code();
                        }
                        case GREATER_THAN -> {
                            if(value_type.equals("int"))code = InstructionSet.GREATER_THAN.code();
                            else code = InstructionSet.FLOAT_GREATER_THAN.code();
                        }
                        case EQUALS -> {
                            if(value_type.equals("int"))code = InstructionSet.EQUALS.code();
                            else code = InstructionSet.FLOAT_EQUALS.code();
                        }
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
                else if(assign.value instanceof ProcedureCall){
                    generate_bytecode(Collections.singletonList(assign.value), bytecode, context);
                    bytecode.add(InstructionSet.ASSIGN_POP.code());
                    context.stack_offset -= get_size(type);
                    bytecode.add(memory_address);
                    bytecode.add(get_size(type));
                }
            }

            if(node instanceof ProcedureCall){
                ProcedureCall call = (ProcedureCall)node;

                if(!call.external){
                    ProcedureDeclaration procedure = context.scope.procedures.get(call.name);
                    for(String output : procedure.outputs){
                        long size = get_size(output);
                        bytecode.add(InstructionSet.ALLOCATE.code());
                        bytecode.add(size);
                        context.stack_offset += size;
                    }
                }

                long offset = context.stack_offset;
                for(Node input : call.inputs){
                    VariableCall variable_call = (VariableCall) input; // assumed

                    // copy inputs
                    long memory_location = offset;
                    long size = get_size(variable_call.type);
                    bytecode.add(InstructionSet.ALLOCATE.code());
                    bytecode.add(size);
                    offset += size;

                    bytecode.add(InstructionSet.ASSIGN_MEMORY.code());
                    bytecode.add(memory_location);
                    bytecode.add(context.scope.locals.get(variable_call.name));
                }

                if(call.external){
                    bytecode.add(InstructionSet.CALL_EXTERNAL.code());
                    bytecode.add(externals.get(call.name));
                }
                else{
                    bytecode.add(InstructionSet.CALL_PROCEDURE.code());
                    long procedure_location = context.scope.labels.get(call.name);
                    bytecode.add(procedure_location);
                }
            }

            if(node instanceof If){
                If if_statement = (If) node;
                VariableCall var_call = (VariableCall) if_statement.condition;
                bytecode.add(InstructionSet.JUMP_IF_NOT.code());

                List<Long> block_bytecode = new ArrayList<>();
                generate_bytecode(if_statement.block, block_bytecode, context);

                bytecode.add((long)(bytecode.size() + block_bytecode.size() + 2));
                bytecode.add(context.scope.locals.get(var_call.name));
                bytecode.addAll(block_bytecode);
            }

            if(node instanceof While){
                While while_statement = (While) node;

                List<Long> block_bytecode = new ArrayList<>();
                generate_bytecode(while_statement.block, block_bytecode, context);
                long condition_location = bytecode.size() + block_bytecode.size() + 2;
                bytecode.add(InstructionSet.JUMP.code());
                bytecode.add(condition_location);

                long block = bytecode.size();

                bytecode.addAll(block_bytecode);

                List<Long> condition = new ArrayList<>();
                generate_bytecode(while_statement.condition_block, condition, context);
                bytecode.addAll(condition);
                bytecode.add(InstructionSet.JUMP_IF.code());
                bytecode.add(block);
                VariableCall condition_var = (VariableCall) while_statement.condition;
                bytecode.add(context.scope.locals.get(condition_var.name));

                /*

                jmp :condition
 block :        z = x + 2
                print(z)
 condition :    x = 3           <-|
                y = 4             |
                cmp x < y       <-|
                if eq jmp :block
  while end     ...
                 */

            }

            if(node instanceof Return){
                Return return_statement = (Return) node;
                VariableCall value = (VariableCall) return_statement.value;
                bytecode.add(InstructionSet.ASSIGN_MEMORY.code());
                long return_location = context.scope.locals.get("<-0");
                bytecode.add(return_location);
                bytecode.add(context.scope.locals.get(value.name));
            }
        }
    }
}
