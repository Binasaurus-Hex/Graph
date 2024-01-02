package SyntaxNodes;

public class BinaryOperator implements Node {
    public enum Operation {
        INCREMENT,
        DECREMENT,
        MULTIPLIER,
        DIVISOR,
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

        public boolean is_combined_assign(){
            return switch (this){
                case INCREMENT, DECREMENT, MULTIPLIER, DIVISOR -> true;
                default -> false;
            };
        }

        public Operation from_combined_assign(){
            return switch (this){
                case INCREMENT -> ADD;
                case DECREMENT -> SUBTRACT;
                case MULTIPLIER -> MULTIPLY;
                case DIVISOR -> DIVIDE;
                default -> null;
            };
        }
    }
    public Operation operation;
    public Node left;
    public Node right;
}
