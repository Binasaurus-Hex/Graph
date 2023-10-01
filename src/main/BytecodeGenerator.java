package main;

import Bytecode.InstructionSet;
import SyntaxNodes.*;
import java.lang.reflect.Method;
import java.util.*;

public class BytecodeGenerator {

    Map<String, Long> externals = new HashMap<>();
    Map<String, Boolean> external_returns = new HashMap<>();

    class Scope {
        Map<String, Node> types = new HashMap<>();
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
            String name = Utils.external_name(method.getName());
            externals.put(name, external_id++);
            if(method.getReturnType().getName().equals("void")){
                external_returns.put(name, false);
            }
            else{
                external_returns.put(name, true);
            }
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
                    Node type = procedure.outputs.get(i);
                    long size = get_size(type);
                    input_offset -= size;
                    context.scope.locals.put(String.format("<-%d", procedure.outputs.size() - i - 1), input_offset);
                }

                bytecode.add(InstructionSet.PROCEDURE_HEADER.code());
                context.scope.labels.putAll(global_context.scope.labels); // add all the current functions in
                context.scope.procedures.putAll(global_context.scope.procedures);
                bytecode.add(InstructionSet.ALLOCATE.code());
                int allocation_index = bytecode.size();
                bytecode.add(0L); // will be overwritten with actual stack size

                generate_bytecode(procedure.block, bytecode, context);
                long stack_size = context.stack_offset;
                bytecode.set(allocation_index, stack_size);

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

    long get_size(Node type){
        if(type instanceof LiteralType){
            return 1;
        }
        if(type instanceof ArrayType){
            ArrayType array = (ArrayType) type;
            return get_size(array.type) * array.size;
        }
        if(type instanceof StructDeclaration){
            StructDeclaration struct = (StructDeclaration) type;
            long total_size = 0;
            for(Node node : struct.body){
                VariableDeclaration field = (VariableDeclaration)node;
                total_size += get_size(field.type);
            }
            return total_size;
        }
        if(type instanceof PointerType){
            return 1;
        }
        System.out.println("error cant calculate size of type");
        System.exit(1);
        return -1;
    }

    long get_field_offset(StructDeclaration struct, String field_name){
        if(field_name.equals("velocity")){
            System.out.println();
        }
        int offset = 0;
        for(Node node : struct.body){
            VariableDeclaration field = (VariableDeclaration) node;
            if(field.name.equals(field_name)){
                return offset;
            }
            offset += get_size(field.type);
        }
        System.out.println("couldn't find field " + field_name + " for " + struct.name);
        System.exit(0);
        return -1;
    }

    void generate_bytecode(List<Node> program, List<Long> bytecode, Context context){
        for(Node node : program){
            if(node instanceof VariableDeclaration){
                VariableDeclaration declaration = (VariableDeclaration) node;
                long size = get_size(declaration.type);
                long memory_address = context.stack_offset;
                context.stack_offset += size;
                context.scope.locals.put(declaration.name, memory_address);
                context.scope.types.put(declaration.name, declaration.type);
            }
            if(node instanceof ArrayAssign){
                ArrayAssign assign = (ArrayAssign) node;
                VariableCall array = (VariableCall)assign.array;
                long array_address = context.scope.locals.get(array.name);

                VariableCall index = (VariableCall)assign.index;
                long index_address = context.scope.locals.get(index.name);

                VariableCall value = (VariableCall)assign.value;
                long value_address = context.scope.locals.get(value.name);

                bytecode.add(InstructionSet.ARRAY_ASSIGN.code());
                bytecode.add(array_address);
                bytecode.add(index_address);
                bytecode.add(value_address);
            }
            if(node instanceof StructAssign){
                StructAssign assign = (StructAssign) node;
                VariableCall struct = (VariableCall) assign.struct;
                VariableCall field = (VariableCall) assign.field;
                long struct_address = context.scope.locals.get(struct.name);

                StructDeclaration struct_declaration;
                InstructionSet instruction;
                if(struct.type instanceof PointerType){
                    struct_declaration = (StructDeclaration) ((PointerType)struct.type).type;
                    instruction = InstructionSet.PTR_STRUCT_FIELD_ASSIGN;
                }
                else{
                    struct_declaration = (StructDeclaration) struct.type;
                    instruction = InstructionSet.STRUCT_FIELD_ASSIGN;
                }

                long field_offset = get_field_offset(struct_declaration, field.name);
                VariableCall value = (VariableCall) assign.value;
                long value_address = context.scope.locals.get(value.name);

                bytecode.add(instruction.code());
                bytecode.add(struct_address);
                bytecode.add(field_offset);
                bytecode.add(value_address);
            }

            if(node instanceof VariableAssign){

                VariableAssign assign = (VariableAssign) node;
                Node type = context.scope.types.get(assign.variable_name);
                long memory_address = context.scope.locals.get(assign.variable_name);

                if(assign.value instanceof Literal){
                    long value = 0;
                    if(type instanceof LiteralType){
                        LiteralType literal_type = (LiteralType)type;
                        switch (literal_type.type){
                            case INT -> {
                                Literal<Integer> int_literal = (Literal<Integer>) assign.value;
                                value = int_literal.value;
                            }
                            case FLOAT -> {
                                Literal<Double> float_literal = (Literal<Double>) assign.value;
                                value = Double.doubleToLongBits(float_literal.value);
                            }
                            case BOOL -> {
                                Literal<Boolean> bool_literal = (Literal<Boolean>) assign.value;
                                value = bool_literal.value? 1 : 0;
                            }
                        }
                    }

                    bytecode.add(InstructionSet.ASSIGN_LITERAL.code());
                    bytecode.add(memory_address);
                    bytecode.add(value);
                }
                else if(assign.value instanceof VariableCall){
                    VariableCall variable_call = (VariableCall) assign.value;
                    long size = get_size(variable_call.type);
                    bytecode.add(InstructionSet.ASSIGN_MEMORY.code());
                    bytecode.add(memory_address);
                    bytecode.add(context.scope.locals.get(variable_call.name));
                    if(size > 1){
                        System.out.printf("");
                    }
                    bytecode.add(size);
                }
                else if(assign.value instanceof BinaryOperator){
                    BinaryOperator operator = (BinaryOperator) assign.value;
                    VariableCall a = (VariableCall)operator.left;
                    VariableCall b = (VariableCall)operator.right;

                    Node value_type = a.type; // assumed to be the same type

                    LiteralType literal_value_type = null;
                    if(value_type instanceof LiteralType){
                        literal_value_type = (LiteralType) value_type;
                    }

                    long mem_a = context.scope.locals.get(a.name);
                    if(b == null){
                        System.out.println();
                    }

                    long mem_b;
                    if(operator.operation == BinaryOperator.Operation.DOT){
                        StructDeclaration struct;
                        if(a.type instanceof PointerType){
                            struct = (StructDeclaration) ((PointerType)a.type).type;
                        }
                        else{
                            struct = (StructDeclaration) a.type;
                        }
                        mem_b = get_field_offset(struct, b.name);
                    }
                    else{
                        mem_b = context.scope.locals.get(b.name);
                    }


                    long code;
                    switch (operator.operation){
                        case LESS_THAN -> {
                            if(literal_value_type.type == LiteralType.Type.INT)code = InstructionSet.LESS_THAN.code();
                            else code = InstructionSet.FLOAT_LESS_THAN.code();
                        }
                        case GREATER_THAN -> {
                            if(literal_value_type.type == LiteralType.Type.INT)code = InstructionSet.GREATER_THAN.code();
                            else code = InstructionSet.FLOAT_GREATER_THAN.code();
                        }
                        case EQUALS -> {
                            if(literal_value_type.type == LiteralType.Type.INT)code = InstructionSet.EQUALS.code();
                            else code = InstructionSet.FLOAT_EQUALS.code();
                        }
                        case ADD -> {
                            if(literal_value_type.type == LiteralType.Type.INT) code = InstructionSet.ADD.code();
                            else code = InstructionSet.FLOAT_ADD.code();
                        }
                        case SUBTRACT -> {
                            if(literal_value_type.type == LiteralType.Type.INT) code = InstructionSet.SUBTRACT.code();
                            else code = InstructionSet.FLOAT_SUBTRACT.code();
                        }
                        case MULTIPLY -> {
                            if(literal_value_type.type == LiteralType.Type.INT) code = InstructionSet.MULTIPLY.code();
                            else code = InstructionSet.FLOAT_MULTIPLY.code();
                        }
                        case DIVIDE -> {
                            if(literal_value_type.type == LiteralType.Type.INT) code = InstructionSet.DIVIDE.code();
                            else code = InstructionSet.FLOAT_DIVIDE.code();
                        }
                        case INDEX -> {
                            code = InstructionSet.ASSIGN_ARRAY_INDEX.code();
                        }
                        case DOT -> {
                            if(a.type instanceof PointerType){
                                code = InstructionSet.ASSIGN_PTR_STRUCT_FIELD.code();
                                if(type instanceof PointerType){
                                    code = InstructionSet.ASSIGN_POINTER_FROM_STRUCT_PTR.code();
                                }
                            }
                            else {
                                code = InstructionSet.ASSIGN_STRUCT_FIELD.code();
                                if(type instanceof PointerType){
                                    code = InstructionSet.ASSIGN_POINTER_FROM_STRUCT.code();
                                }
                            }
                        }
                        default -> code = -1;
                    };

                    bytecode.add(code);
                    bytecode.add(memory_address);
                    bytecode.add(mem_a);
                    bytecode.add(mem_b);
                }
                else if(assign.value instanceof UnaryOperator){
                    UnaryOperator operator = (UnaryOperator) assign.value;
                    VariableCall variable = (VariableCall) operator.node;
                    switch (operator.operation){
                        case REFERENCE -> {
                            bytecode.add(InstructionSet.ASSIGN_ADDRESS.code());
                            bytecode.add(memory_address);
                            bytecode.add(context.scope.locals.get(variable.name));
                        }
                        case DEREFERENCE -> {
                            bytecode.add(InstructionSet.ASSIGN_DEREFERENCE.code());
                            bytecode.add(memory_address);
                            bytecode.add(context.scope.locals.get(variable.name));
                        }
                    }
                }

                else if(assign.value instanceof ProcedureCall){
                    generate_bytecode(Collections.singletonList(assign.value), bytecode, context);
                    bytecode.add(InstructionSet.ASSIGN_POP.code());
                    bytecode.add(memory_address);
                }
            }

            if(node instanceof ProcedureCall){
                ProcedureCall call = (ProcedureCall)node;

                if(!call.external){
                    ProcedureDeclaration procedure = context.scope.procedures.get(call.name);
                    for(Node output : procedure.outputs){
                        long size = get_size(output);
                        bytecode.add(InstructionSet.ALLOCATE.code());
                        bytecode.add(size);
                    }
                }
                else if(external_returns.get(call.name)){
                    bytecode.add(InstructionSet.ALLOCATE.code());
                    bytecode.add(1L); // we only support single size returns from externals
                }

                for(Node input : call.inputs){
                    VariableCall variable_call = (VariableCall) input; // assumed

                    bytecode.add(InstructionSet.PUSH_MEMORY.code());
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

                bytecode.add((long)(block_bytecode.size() + 1));
                bytecode.add(context.scope.locals.get(var_call.name));
                bytecode.addAll(block_bytecode);
            }

            if(node instanceof While){
                While while_statement = (While) node;

                List<Long> block_bytecode = new ArrayList<>();
                generate_bytecode(while_statement.block, block_bytecode, context);
                bytecode.add(InstructionSet.JUMP.code());
                bytecode.add((long)block_bytecode.size());

                long block = bytecode.size();

                bytecode.addAll(block_bytecode);

                List<Long> condition = new ArrayList<>();
                generate_bytecode(while_statement.condition_block, condition, context);
                bytecode.addAll(condition);
                bytecode.add(InstructionSet.JUMP_IF.code());
                bytecode.add(block - bytecode.size() - 1);
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
                bytecode.add(get_size(value.type));

                bytecode.add(InstructionSet.RETURN.code());
                bytecode.add((long)return_statement.procedure.inputs.size());
            }
        }
    }
}
