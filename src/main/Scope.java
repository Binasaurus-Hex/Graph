package main;

import SyntaxNodes.ProcedureDeclaration;
import SyntaxNodes.StructDeclaration;
import SyntaxNodes.VariableDeclaration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scope {
    public ProcedureDeclaration enclosing_procedure = null; // for return statements
    public List<ProcedureDeclaration> procedures;
    public List<VariableDeclaration> variables = new ArrayList<>();
    public Map<String, VariableDeclaration> variable_map = new HashMap<>();
    public List<StructDeclaration> structs;

    public ProcedureDeclaration find_procedure(String name){
        for(ProcedureDeclaration procedure : procedures){
            if(procedure.name.equals(name)){
                return procedure;
            }
        }
        return null;
    }

    public VariableDeclaration find_variable(String name){
        return variable_map.get(name);
    }
}
