package SyntaxNodes;

public class BinaryOperator implements Node {
    public enum Operation {
        ASSIGN,
        AND,
        OR,
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

        public boolean is_logical_operator(){
            return switch (this){
                case AND, OR -> true;
                default -> false;
            };
        }
    }
    public Operation operation;
    public Node left;
    public Node right;
}
