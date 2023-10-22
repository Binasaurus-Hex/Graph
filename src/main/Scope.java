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
    public List<ProcedureDeclaration> procedures = new ArrayList<>();
    public Map<String, VariableDeclaration> variable_map = new HashMap<>();
    public List<StructDeclaration> structs = new ArrayList<>();

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

    public Scope duplicate(){
        Scope sub_scope = new Scope();
        sub_scope.enclosing_procedure = enclosing_procedure;
        sub_scope.procedures.addAll(procedures);
        sub_scope.variable_map.putAll(variable_map);
        sub_scope.structs.addAll(structs);
        return sub_scope;
    }
}
