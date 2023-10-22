package SyntaxNodes;

import java.util.List;

public class For implements Node {
    public VariableCall iteration; // name of current iteration (optional)
    public VariableCall index; // index of iteration (optional)
    public Node sequence; // currently just arrays

    public List<Node> block;
}
