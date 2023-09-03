package SyntaxNodes;

public class BinaryOperator implements Node {
    public enum Operation {
        LESS_THAN,
        GREATER_THAN,
        EQUALS,
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE,
        INDEX;


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
