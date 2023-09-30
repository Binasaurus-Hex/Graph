package main;

import SyntaxNodes.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TreePrinter {

    public static void printType(StringBuilder builder, Node type){
        if(type instanceof PointerType){
            PointerType pointer = (PointerType) type;
            builder.append("*");
            printType(builder, pointer.type);
        }
        if(type instanceof LiteralType){
            LiteralType literal = (LiteralType) type;
            switch (literal.type){
                case INT -> builder.append("int");
                case BOOL -> builder.append("bool");
                case FLOAT -> builder.append("float");
            }
        }
        if(type instanceof StructDeclaration){
            StructDeclaration struct = (StructDeclaration)type;
            builder.append(struct.name);
        }
    }

    public static void printVarDecl(StringBuilder builder, VariableDeclaration declaration){
        builder.append(declaration.name);
        builder.append(":");
        printType(builder, declaration.type);
    }

    public static void printVarCall(StringBuilder builder, VariableCall call){
        builder.append(call.name);
    }

    public static void printStructAssign(StringBuilder builder, StructAssign structAssign){
        printVarCall(builder, (VariableCall) structAssign.struct);
        builder.append(".");
        printVarCall(builder, (VariableCall) structAssign.field);
        builder.append("=");
        printVarCall(builder, (VariableCall) structAssign.value);
    }

    public static void printProcedureCall(StringBuilder builder, ProcedureCall call){
        builder.append(call.name);
        builder.append("()");
    }

    public static void printBinaryOperator(StringBuilder builder, BinaryOperator operator){
        printVarCall(builder, (VariableCall) operator.left);
        switch (operator.operation){
            case DOT -> builder.append(".");
            case MULTIPLY -> builder.append("*");
            case ADD -> builder.append("+");
            case SUBTRACT -> builder.append("-");
            case ASSIGN -> builder.append("=");
            case EQUALS -> builder.append("==");
            case LESS_THAN -> builder.append("<");
            case GREATER_THAN -> builder.append(">");
        }
        printVarCall(builder, (VariableCall) operator.right);

    }

    public static void printBlock(StringBuilder builder, List<Node> block){
        for(Node statement : block){
            builder.append("\t");
            if(statement instanceof VariableDeclaration){
                printVarDecl(builder, (VariableDeclaration) statement);
            }
            if(statement instanceof StructAssign){
                printStructAssign(builder, (StructAssign) statement);
            }
            if(statement instanceof BinaryOperator){
                printBinaryOperator(builder, (BinaryOperator) statement);
            }
            if(statement instanceof VariableAssign){
                VariableAssign variableAssign = (VariableAssign)statement;
                builder.append(variableAssign.variable_name);
                builder.append(" = ");
                if(variableAssign.value instanceof Literal){
                    Literal<?> literal = (Literal<?>) variableAssign.value;
                    builder.append(literal.value);
                }
                if(variableAssign.value instanceof BinaryOperator){
                    printBinaryOperator(builder, (BinaryOperator) variableAssign.value);
                }
                if(variableAssign.value instanceof VariableCall){
                    printVarCall(builder, (VariableCall) variableAssign.value);
                }
                if(variableAssign.value instanceof ProcedureCall){
                    printProcedureCall(builder, (ProcedureCall) variableAssign.value);
                }
            }
            if(statement instanceof While){
                builder.append("while ");
                printVarCall(builder, (VariableCall) ((While) statement).condition);
                builder.append("{\n");
                printBlock(builder, ((While) statement).block);
                builder.append("}");
            }
            builder.append("\n");
        }
    }

    public static void printProcedure(StringBuilder builder, ProcedureDeclaration procedure){
        builder.append(procedure.name);
        builder.append(" :: ");
        builder.append("(");
        for(Node node : procedure.inputs){
            printVarDecl(builder, (VariableDeclaration) node);
            if(procedure.inputs.indexOf(node) != procedure.inputs.size() -1 ){
                builder.append(",");
            }
        }
        builder.append(")");
        if(procedure.outputs != null && !procedure.outputs.isEmpty()){
            builder.append("->");
            printType(builder, procedure.outputs.get(0));
        }
        builder.append("{\n");
        printBlock(builder, procedure.block);
        builder.append("}\n");
    }

    public static void print(List<Node> program){
        StringBuilder builder = new StringBuilder();
        for(Node node : program){
            if(node instanceof ProcedureDeclaration){
                printProcedure(builder, (ProcedureDeclaration) node);
            }
        }
        try {
            Files.writeString(Path.of("Flattened.txt"), builder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
