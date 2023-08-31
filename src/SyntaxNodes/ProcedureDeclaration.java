package SyntaxNodes;

import java.util.List;

public class ProcedureDeclaration implements Node {
    public String name;
    public List<Node> inputs;
    public List<String> outputs;
    public List<Node> block;
    public boolean external = false;
}
