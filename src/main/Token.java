package main;

import java.util.HashMap;
import java.util.Map;

public class Token {

    public static Map<Type, String> keywords  = new HashMap<>() {{
        put(Type.STRUCT, "struct");
        put(Type.WHILE, "while");
        put(Type.ELSE, "else");
        put(Type.IF, "if");
        put(Type.TRUE, "true");
        put(Type.FALSE, "false");
        put(Type.FOR, "for");
        put(Type.AND, "and");
        put(Type.OR, "or");
        put(Type.IS, "is");
    }};

    public enum Type {
        STRING,
        IDENTIFIER,
        INTEGER,

        // keywords
        STRUCT,
        WHILE,
        ELSE,
        TRUE,
        FALSE,
        FOR,
        AND,
        OR,
        IS,
        IF,

        // punctuation
        OPEN_BRACKET("["),
        CLOSE_BRACKET("]"),
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

        ADDRESS("&"),

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
            case WHILE,FOR,AND,OR,IF,ELSE,STRUCT -> true;
            default -> false;
        };
    }

    boolean is_binary_operator(){
        return switch (type){
            case MINUS,PLUS,FORWARD_SLASH,STAR,DOUBLE_EQUALS,LESS_THAN,GREATER_THAN, DOT, EQUALS, OPEN_BRACKET -> true;
            default -> false;
        };
    }

    boolean is_unary_operator(){
        return switch (type) {
            case STAR, ADDRESS, MINUS, PLUS -> true;
            default -> false;
        };
    }
}
