import SyntaxNodes.*;
import Runtime.VirtualMachine;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    static String read_file_as_text(String filepath){
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(filepath));
            return new String(bytes);
        } catch (IOException e) {
            return "";
        }
    }

    static boolean matches(String token_text, String program_text, int program_text_index){
        if(token_text == null){
            return false;
        }
        if(program_text_index + token_text.length() >= program_text.length())return false;

        for(int i = 0; i < token_text.length(); i++){
            char a = token_text.charAt(i);
            char b = program_text.charAt(program_text_index + i);
            if(a != b) return false;
        }
        return true;
    }

    static String get_identifier_text(String program_text, Token identifier){
        return program_text.substring(identifier.index, identifier.index + identifier.length);
    }

    static int get_integer_value(String program_text, Token integer_token){
        String text = get_identifier_text(program_text,integer_token);
        return Integer.parseInt(text);
    }

    static String get_string_text(String program_text, Token string){
        return program_text.substring(string.index + 1, string.index + string.length - 1);
    }

    static BinaryOperator.Operation to_binary_operation(Token op){

        switch (op.type){
            case PLUS:          return BinaryOperator.Operation.ADD;
            case MINUS:         return BinaryOperator.Operation.SUBTRACT;
            case STAR:          return BinaryOperator.Operation.MULTIPLY;
            case FORWARD_SLASH: return BinaryOperator.Operation.DIVIDE;
            case DOUBLE_EQUALS: return BinaryOperator.Operation.EQUALS;
            case LESS_THAN:     return BinaryOperator.Operation.LESS_THAN;
            case GREATER_THAN:  return BinaryOperator.Operation.GREATER_THAN;
        }
        return null;
    }

    static boolean is_number(char c){
        return c >= '0' && c <= '9';
    }

    static List<Token> tokenize(String program_text){
        List<Token> tokens = new ArrayList<>();

        Token.Type[] token_types = Token.Type.values();
        int text_index = 0;
        int last_token_index = 0;
        while (text_index < program_text.length()){

            char current_char = program_text.charAt(text_index);
            boolean found_token = false;

            // parse strings
            if (current_char == '\"'){
                for(int search_index = text_index + 1; search_index < program_text.length(); search_index++){
                    char next_char = program_text.charAt(search_index);
                    if(next_char == '\"'){
                        Token string_token = new Token(Token.Type.STRING, text_index);
                        string_token.length = 1 + search_index - text_index;
                        tokens.add(string_token);
                        text_index += string_token.length;
                        last_token_index = text_index;
                        found_token = true;
                        break;
                    }
                }
            }
            if(found_token)continue;

            for(Token.Type token_type : token_types){
                if(matches(token_type.text, program_text, text_index)){

                    // check for identifiers
                    if(text_index > last_token_index){
                        Token identifier = new Token(Token.Type.IDENTIFIER, last_token_index);
                        identifier.length = text_index - last_token_index;

                        boolean is_number = true;
                        for(int i = last_token_index; i < text_index; i++){
                            char c = program_text.charAt(i);
                            if(!is_number(c)){
                                is_number = false;
                                break;
                            }
                        }
                        if (is_number) identifier.type = Token.Type.INTEGER;

                        tokens.add(identifier);
                    }

                    Token token = new Token(token_type, text_index);
                    tokens.add(token);
                    text_index += token_type.text.length();
                    last_token_index = text_index;
                    found_token = true;
                }
            }
            if(found_token)continue;

            text_index ++;
        }

        return tokens;
    }

    static void print_tokens(List<Token> tokens, String program_text){

        for(Token token : tokens){
            if(token.type == Token.Type.STRING){
                System.out.print(String.format("S<%s>", get_string_text(program_text, token)));
                continue;
            }
            if(token.type == Token.Type.IDENTIFIER){
                System.out.print(String.format("T<%s>", get_identifier_text(program_text, token)));
                continue;
            }
            if(token.type == Token.Type.INTEGER){
                System.out.print(String.format("I<%s>", get_identifier_text(program_text,token)));
                continue;
            }
            System.out.print(token.type.text);
        }
    }

    interface NodeFunc {
        Node call(Tokenizer tokenizer);
    }

    static Node parse_subexpression(Tokenizer tokenizer){
        // brackets or literal
        Token next_token = tokenizer.peek_token();
        switch (next_token.type){
            case INTEGER -> {
                Token int_token = tokenizer.eat_token();
                next_token = tokenizer.peek_token();
                if(next_token.type == Token.Type.DOT){
                    // floating point
                    tokenizer.eat_token();

                    String whole = get_identifier_text(tokenizer.program_text, int_token);
                    String decimal = "";

                    next_token = tokenizer.peek_token();
                    if(next_token.type == Token.Type.INTEGER){
                        tokenizer.eat_token();
                        decimal = get_identifier_text(tokenizer.program_text, next_token);
                    }
                    return new Literal<Float>(Float.parseFloat(whole+"."+decimal));
                }
                return new Literal<Integer>(get_integer_value(tokenizer.program_text, int_token));
            }
            case STRING -> {
                Token string_token = tokenizer.eat_token();
                return new Literal<String>(get_string_text(tokenizer.program_text, string_token));
            }
            case OPEN_PARENTHESIS -> {
                tokenizer.eat_token();
                Node expression = parse_expression(tokenizer);
                if(tokenizer.peek_token().type != Token.Type.CLOSE_PARENTHESIS){
                    // error
                    Utils.print_token_error(tokenizer, "missing closing parenthesis");
                }
                tokenizer.eat_token();
                return expression;
            }
            case IDENTIFIER -> {
                Token identifier = tokenizer.eat_token();

                String name = get_identifier_text(tokenizer.program_text, identifier);
                next_token = tokenizer.peek_token();
                if(next_token.type == Token.Type.OPEN_PARENTHESIS){
                    ProcedureCall procedure_call = new ProcedureCall();
                    procedure_call.name = name;
                    procedure_call.inputs = parse_variadic(
                            tokenizer,
                            Main::parse_expression,
                            Token.Type.OPEN_PARENTHESIS,
                            Token.Type.COMMA,
                            Token.Type.CLOSE_PARENTHESIS);
                    return procedure_call;
                }

                VariableCall variable_call = new VariableCall();
                variable_call.name = name;
                return variable_call;
            }
        }
        return null;
    }

    static Node parse_expression(Tokenizer tokenizer, int precedence){
        BinaryOperator operator = new BinaryOperator();
        Node subexpression = parse_subexpression(tokenizer);
        Token next_token = tokenizer.peek_token();
        if (!next_token.is_binary_operator()) {
            return subexpression;
        }
        operator.left = subexpression;

        while(true) {

            operator.operation = to_binary_operation(tokenizer.peek_token());

            if (operator.operation.ordinal() < precedence) {
                return operator.left;
            }
            tokenizer.eat_token();

            operator.right = parse_expression(tokenizer, operator.operation.ordinal());

            next_token = tokenizer.peek_token();
            if (!next_token.is_binary_operator()) {
                return operator;
            }

            BinaryOperator temp = operator;
            operator = new BinaryOperator();
            operator.left = temp;
        }
    }

    static Node parse_expression(Tokenizer tokenizer){
        return parse_expression(tokenizer, -10000); // small number
    }

    static List<Node> parse_variadic(Tokenizer tokenizer, NodeFunc func, Token.Type start, Token.Type separator, Token.Type end){
        List<Node> nodes = new ArrayList<>();

        Token next_token = tokenizer.peek_token();
        if(next_token.type != start){
            Utils.print_token_error(tokenizer, String.format("missing '%s'", start.text));
        }
        tokenizer.eat_token();
        next_token = tokenizer.peek_token();
        while (next_token.type != end){
            Node node = func.call(tokenizer);
            nodes.add(node);
            next_token = tokenizer.peek_token();
            if(!(next_token.type == end || next_token.type == separator)){
                // there's an issue
                Utils.print_token_error(tokenizer, "unknown symbol");
            }
            if(next_token.type == separator){
                tokenizer.eat_token();
                next_token = tokenizer.peek_token();
            }
        }
        tokenizer.eat_token();
        return nodes;
    }

    static List<Node> parse_block(Tokenizer tokenizer){
        List<Node> statements = new ArrayList<>();
        if(tokenizer.peek_token().type != Token.Type.OPEN_BRACE){
            return null;
        }
        tokenizer.eat_token();

        while (tokenizer.peek_token().type != Token.Type.CLOSE_BRACE){
            Node statement = parse_statement(tokenizer);
            if(tokenizer.tokens.get(tokenizer.token_index -1).type != Token.Type.CLOSE_BRACE){
                Token semi_colon = tokenizer.peek_token();
                if(semi_colon.type != Token.Type.SEMI_COLON){
                    Utils.print_token_error(tokenizer, "Error missing semi colon");
                }
                tokenizer.eat_token();
            }
            statements.add(statement);
        }
        tokenizer.eat_token();
        return statements;
    }

    static Node parse_statement(Tokenizer tokenizer){
        Token name_or_keyword = tokenizer.peek_token();
        if(name_or_keyword.type == Token.Type.IDENTIFIER){
            tokenizer.eat_token();
            String name = get_identifier_text(tokenizer.program_text, name_or_keyword);
            Token next_token = tokenizer.peek_token();
            if(next_token.type == Token.Type.COLON){
                tokenizer.eat_token();
                next_token = tokenizer.peek_token();

                String type = "";
                // parse type
                if(next_token.type == Token.Type.IDENTIFIER){
                    type = get_identifier_text(tokenizer.program_text, next_token);
                    tokenizer.eat_token();
                    next_token = tokenizer.peek_token();
                }

                if(next_token.type == Token.Type.COLON){
                    tokenizer.eat_token();
                    // constant or procedure
                    // for now only procedure
                    ProcedureDeclaration procedure = new ProcedureDeclaration();
                    procedure.name = name;
                    procedure.inputs = parse_variadic(tokenizer,Main::parse_statement, Token.Type.OPEN_PARENTHESIS, Token.Type.COMMA, Token.Type.CLOSE_PARENTHESIS);
                    procedure.block = parse_block(tokenizer);
                    return procedure;
                }
                else{
                    VariableDeclaration variable_decl = new VariableDeclaration();
                    variable_decl.name = name;
                    variable_decl.type = type;

                    if(tokenizer.peek_token().type == Token.Type.EQUALS){
                        tokenizer.eat_token();
                        VariableDeclAssign decl_assign = new VariableDeclAssign();
                        decl_assign.decl = variable_decl;
                        VariableAssign assign = new VariableAssign();
                        assign.variable_name = name;
                        assign.value = parse_expression(tokenizer);
                        decl_assign.assign = assign;
                        return decl_assign;
                    }
                }
            }
            else if(next_token.type == Token.Type.EQUALS){
                tokenizer.eat_token();
                VariableAssign assign = new VariableAssign();
                assign.variable_name = name;
                assign.value = parse_expression(tokenizer);
                return assign;
            }
            else{
                return parse_expression(tokenizer);
            }
        }
        else if (name_or_keyword.is_keyword()){
            Token keyword = tokenizer.eat_token();
            if(keyword.type == Token.Type.WHILE){
                While while_statement = new While();
                while_statement.condition = parse_expression(tokenizer);
                while_statement.block = parse_block(tokenizer);
                return while_statement;
            }
            else if(keyword.type == Token.Type.IF){
                If if_statement = new If();
                if_statement.condition = parse_expression(tokenizer);
                if_statement.block = parse_block(tokenizer);
                return if_statement;
            }
            else if(keyword.type == Token.Type.BACK_ARROW){
                Return return_statement = new Return();
                return_statement.value = parse_expression(tokenizer);
                return return_statement;
            }
        }
        else{
            return parse_expression(tokenizer);
        }
        return null;
    }

    static List<Node> parse_program(Tokenizer tokenizer){
        List<Node> statements = new ArrayList<>();

        while (tokenizer.peek_token() != null){
            Node statement = parse_statement(tokenizer);
            if(tokenizer.tokens.get(tokenizer.token_index -1).type != Token.Type.CLOSE_BRACE){
                Token semi_colon = tokenizer.peek_token();
                if(semi_colon.type != Token.Type.SEMI_COLON){
                    Utils.print_token_error(tokenizer,"Error missing semi colon");
                }
                tokenizer.eat_token();
            }
            statements.add(statement);
        }
        return statements;
    }

    public static void main(String[] args) {
        String program_text = read_file_as_text("main.graph");
        System.out.println();
        List<Token> tokens = tokenize(program_text);
        //print_tokens(tokens, program_text);

        Tokenizer tokenizer = new Tokenizer();
        tokenizer.tokens = tokens;
        tokenizer.program_text = program_text;

        List<Node> program = parse_program(tokenizer);

        VirtualMachine vm = new VirtualMachine();
    }
}
