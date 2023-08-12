package SyntaxNodes;

public class BinaryOperator implements Node {
    public enum Operation {
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE,
        LESS_THAN,
        GREATER_THAN,
        EQUALS,
    }
    public Operation operation;
    public Node left;
    public Node right;
}
