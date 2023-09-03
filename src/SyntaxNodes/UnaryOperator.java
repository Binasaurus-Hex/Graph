package SyntaxNodes;

public class UnaryOperator implements Node {
    public enum Operation {
        REFERENCE, DEREFERENCE
    }

    public UnaryOperator.Operation operation;
    public Node node;
}
