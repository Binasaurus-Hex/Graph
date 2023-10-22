package SyntaxNodes;

public class LiteralType implements Node {
    public enum Type {
        FLOAT,INT,BOOL
    };
    public Type type;

    public static LiteralType INT(){
        LiteralType literal = new LiteralType();
        literal.type = Type.INT;
        return literal;
    }

    public static LiteralType BOOL(){
        LiteralType literal = new LiteralType();
        literal.type = Type.BOOL;
        return literal;
    }
}
