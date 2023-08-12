public class Token {


    public enum Type {
        STRING,
        IDENTIFIER,
        INTEGER,

        // keywords
        WHILE("while"),
        FOR("for"),
        AND("and"),
        OR("or"),
        IS("is"),
        IF("if"),
        ELSE("else"),

        // punctuation
        OPEN_BRACE("{"),
        CLOSE_BRACE("}"),
        OPEN_PARENTHESIS("("),
        CLOSE_PARENTHESIS(")"),
        COLON(":"),
        SEMI_COLON(";"),
        BACK_ARROW("<-"),
        FORWARD_ARROW("->"),
        LESS_THAN("<"),
        GREATER_THAN(">"),
        DOT("."),
        DOUBLE_EQUALS("=="),
        EQUALS("="),
        COMMA(","),

        MINUS("-"),
        PLUS("+"),
        FORWARD_SLASH("/"),
        STAR("*"),

        // whitespace,
        SPACE(" "),
        NEWLINE("\n"),
        CARRIAGE_RETURN("\r"),
        TAB("\t");

        String text;
        Type(String value){
            text = value;
        }
        Type(){}
    }

    Type type;
    int index;
    int length;

    Token(Type type, int index){
        this.type = type;
        this.index = index;

        if(type.text != null) length = type.text.length();
    }

    boolean is_punctuation(){
        return switch (type) {
            case SPACE, NEWLINE, CARRIAGE_RETURN, TAB -> true;
            default -> false;
        };
    }

    boolean is_keyword(){
        return switch (type){
            case WHILE,FOR,AND,OR,IF,ELSE,BACK_ARROW -> true;
            default -> false;
        };
    }

    boolean is_binary_operator(){
        return switch (type){
            case MINUS,PLUS,FORWARD_SLASH,STAR,DOUBLE_EQUALS,LESS_THAN,GREATER_THAN-> true;
            default -> false;
        };
    }
}
