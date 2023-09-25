package SyntaxNodes;

public class StructAssign implements Node {
    public Node struct;
    public Node field;
    public Node value;
    public boolean pointer;
}
