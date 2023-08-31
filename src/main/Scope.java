package main;

import SyntaxNodes.ProcedureDeclaration;
import SyntaxNodes.VariableDeclaration;

import java.util.List;

public class Scope {
    public ProcedureDeclaration enclosing_procedure = null; // for return statements
    public List<ProcedureDeclaration> procedures;
    public List<VariableDeclaration> variables;

    public ProcedureDeclaration find_procedure(String name){
        for(ProcedureDeclaration procedure : procedures){
            if(procedure.name.equals(name)){
                return procedure;
            }
        }
        return null;
    }

    public VariableDeclaration find_variable(String name){
        for(VariableDeclaration variable : variables){
            if(variable.name.equals(name)){
                return variable;
            }
        }
        return null;
    }
}
