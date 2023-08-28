package SyntaxNodes;

import java.util.List;

public class ProcedureDeclaration implements Node {
    public String name;
    public List<Node> inputs;
    public String return_type;
    public List<Node> block;
    public boolean external = false;
}
