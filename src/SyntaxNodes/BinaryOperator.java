package SyntaxNodes;

public class BinaryOperator implements Node {
    public enum Operation {
        ASSIGN,
        AND,
        LESS_THAN,
        GREATER_THAN,
        EQUALS,
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE,
        INDEX,
        DOT,

        IN;

        public boolean is_comparison(){
            switch (this){
                case LESS_THAN, GREATER_THAN, EQUALS -> {
                    return true;
                }
                default -> {
                    return false;
                }
            }
        }
    }
    public Operation operation;
    public Node left;
    public Node right;
}
