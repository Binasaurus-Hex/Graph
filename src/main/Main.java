package main;

import SyntaxNodes.*;
import Bytecode.VirtualMachine;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        if(program_text_index + token_text.length() > program_text.length())return false;

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
            case DOT:           return BinaryOperator.Operation.DOT;
            case EQUALS:        return BinaryOperator.Operation.ASSIGN;
            case OPEN_BRACKET:  return BinaryOperator.Operation.INDEX;
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

                        for(Map.Entry<Token.Type, String> keyword : Token.keywords.entrySet()){
                            if(matches(keyword.getValue(), program_text, last_token_index)){
                                identifier.type = keyword.getKey();
                                break;
                            }
                        }
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
                    return new Literal<Double>(Double.parseDouble(whole+"."+decimal));
                }
                return new Literal<Integer>(get_integer_value(tokenizer.program_text, int_token));
            }
            case STRING -> {
                Token string_token = tokenizer.eat_token();
                return new Literal<String>(get_string_text(tokenizer.program_text, string_token));
            }
            case TRUE -> {
                tokenizer.eat_token();
                return new Literal<Boolean>(true);
            }
            case FALSE -> {
                tokenizer.eat_token();
                return new Literal<Boolean>(false);
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
        if(subexpression == null && next_token.is_unary_operator()){
            UnaryOperator unary_operator = new UnaryOperator();
            switch (tokenizer.eat_token().type){
                case ADDRESS -> {
                    unary_operator.operation = UnaryOperator.Operation.REFERENCE;
                }
                case STAR -> {
                    unary_operator.operation = UnaryOperator.Operation.DEREFERENCE;
                }
                case MINUS -> {
                    unary_operator.operation = UnaryOperator.Operation.MINUS;
                }
            }
            unary_operator.node = parse_subexpression(tokenizer);
            subexpression = unary_operator;
            next_token = tokenizer.peek_token();
        }
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

            if(operator.operation == BinaryOperator.Operation.INDEX){
                if(tokenizer.peek_token().type != Token.Type.CLOSE_BRACKET){
                    Utils.print_token_error(tokenizer, "missing closing bracket");
                }
                tokenizer.eat_token();
            }
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

    static Node parse_type(Tokenizer tokenizer){
        switch (tokenizer.peek_token().type){
            case OPEN_BRACKET -> {
                tokenizer.eat_token();

                if(tokenizer.peek_token().type != Token.Type.INTEGER){
                    Utils.print_token_error(tokenizer, "missing array size specifier");
                }
                int value = get_integer_value(tokenizer.program_text, tokenizer.eat_token());
                ArrayType array = new ArrayType();
                array.size = value;

                if(tokenizer.peek_token().type != Token.Type.CLOSE_BRACKET){
                    Utils.print_token_error(tokenizer, "missing closing bracket on type");
                }
                tokenizer.eat_token();
                array.type = parse_type(tokenizer);
                return array;
            }
            case IDENTIFIER -> {
                LiteralType literal = new LiteralType();
                String text = get_identifier_text(tokenizer.program_text, tokenizer.eat_token());
                switch (text){
                    case "float" -> literal.type = LiteralType.Type.FLOAT;
                    case "int" -> literal.type = LiteralType.Type.INT;
                    case "bool" -> literal.type = LiteralType.Type.BOOL;
                    default -> {
                        StructType struct = new StructType();
                        struct.name = text;
                        return struct;
                    }
                }
                return literal;
            }
            case STAR -> {
                // pointer
                tokenizer.eat_token();
                PointerType pointer = new PointerType();
                pointer.type = parse_type(tokenizer);
                return pointer;
            }
        }
        return null;
    }

    static Node parse_statement(Tokenizer tokenizer){
        Token name_or_keyword = tokenizer.peek_token();
        if(name_or_keyword.type == Token.Type.IDENTIFIER){
            String name = get_identifier_text(tokenizer.program_text, name_or_keyword);
            Token next_token = tokenizer.peek_token(2);
            if(next_token.type == Token.Type.COLON){
                tokenizer.eat_token(); // name
                tokenizer.eat_token(); // colon
                next_token = tokenizer.peek_token();

                Node type = parse_type(tokenizer);

                if(next_token.type == Token.Type.COLON){
                    tokenizer.eat_token();

                    if(tokenizer.peek_token().type == Token.Type.STRUCT){
                        tokenizer.eat_token(); // struct
                        StructDeclaration struct = new StructDeclaration();
                        struct.name = name;
                        struct.body = parse_block(tokenizer);
                        return struct;
                    }
                    // constant or procedure
                    // for now only procedure
                    ProcedureDeclaration procedure = new ProcedureDeclaration();
                    procedure.name = name;
                    procedure.inputs = parse_variadic(tokenizer,Main::parse_statement, Token.Type.OPEN_PARENTHESIS, Token.Type.COMMA, Token.Type.CLOSE_PARENTHESIS);
                    procedure.outputs = new ArrayList<>();
                    if(tokenizer.peek_token().type == Token.Type.FORWARD_ARROW){
                        tokenizer.eat_token();
                        while (tokenizer.peek_token().type != Token.Type.OPEN_BRACE){
                            // PARSE TYPE
                            Node output_type = parse_type(tokenizer);
                            if(output_type == null){
                                Utils.print_token_error(tokenizer, "unknown type definition");
                            }
                            procedure.outputs.add(output_type);
                            if(tokenizer.peek_token().type != Token.Type.COMMA)break;
                        }
                    }

                    procedure.block = parse_block(tokenizer);
                    return procedure;
                }
                else{

                    if(tokenizer.peek_token().type == Token.Type.EQUALS){
                        tokenizer.eat_token();
                        VariableDeclaration declaration = new VariableDeclaration();
                        declaration.name = name;
                        declaration.type = type;
                        declaration.value = parse_expression(tokenizer);
                        return declaration;
                    }
                    else{
                        VariableDeclaration declaration = new VariableDeclaration();
                        declaration.name = name;
                        declaration.type = type;
                        // uninitialized
                        return declaration;
                    }
                }
            }
//            else if(next_token.type == Token.Type.EQUALS){
//                tokenizer.eat_token(); // name
//                tokenizer.eat_token(); // =
//                VariableAssign assign = new VariableAssign();
//                assign.variable_name = name;
//                assign.value = parse_expression(tokenizer);
//                return assign;
//            }
//            else if(next_token.type == Token.Type.OPEN_BRACKET){
//                Node left = parse_expression(tokenizer);
//                if(!(left instanceof BinaryOperator)){
//                    Utils.print_token_error(tokenizer, "invalid array access");
//                }
//
//                BinaryOperator index = (BinaryOperator) left;
//
//                if(tokenizer.peek_token().type != Token.Type.EQUALS){
//                    Utils.print_token_error(tokenizer, "no assignment for array");
//                }
//                tokenizer.eat_token();
//                Node value = parse_expression(tokenizer);
//
//                ArrayAssign array_assign = new ArrayAssign();
//                array_assign.array = index.left;
//                array_assign.index = index.right;
//                array_assign.value = value;
//                return array_assign;
//            }
            else{
                Node expression = parse_expression(tokenizer);
                return expression;
            }
        }
        // statement "keywords"
        else if (name_or_keyword.is_keyword() || name_or_keyword.type == Token.Type.BACK_ARROW){
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

    static LiteralType get_type(Literal literal){
        LiteralType literal_type = new LiteralType();
        if(literal.value instanceof Double){
            literal_type.type = LiteralType.Type.FLOAT;
        }
        if(literal.value instanceof Integer){
            literal_type.type = LiteralType.Type.INT;
        }
        if(literal.value instanceof Boolean){
            literal_type.type = LiteralType.Type.BOOL;
        }
        return literal_type;
    }

    static int generated_name_counter = 0;

    static Node generate_variable_to(Node expression, List<Node> generated_statements, Node type){
        VariableDeclaration declaration = new VariableDeclaration();
        declaration.name = String.format("generated_ident_%d", generated_name_counter++);
        declaration.type = type;

        if(type == null && expression instanceof Literal){
            declaration.type = get_type((Literal)expression);
        }

        generated_statements.add(declaration);

        VariableAssign assign = new VariableAssign();
        assign.variable_name = declaration.name;
        assign.value = expression;
        generated_statements.add(assign);

        VariableCall variable_call = new VariableCall();
        variable_call.name = declaration.name;
        variable_call.type = declaration.type;
        return variable_call;
    }

    static Node enrich_type(Node node, Scope scope){
        if(node instanceof PointerType){
            PointerType pointer_type = (PointerType) node;
            pointer_type.type = enrich_type(pointer_type.type, scope);
        }
        if(node instanceof ArrayType){
            ArrayType array_type = (ArrayType)node;
            array_type.type = enrich_type(array_type.type, scope);
        }
        if(node instanceof StructType){
            StructType struct_type = (StructType) node;
            for(StructDeclaration struct : scope.structs){
                if(struct.name.equals(struct_type.name)){
                    return struct;
                }
            }
        }
        return node;
    }

    static Node flatten_expression(Node expression, List<Node> generated_statements, boolean top_level, Node type, Scope scope){
        if(expression instanceof VariableCall){
            VariableCall variable_call = (VariableCall)expression;
            VariableDeclaration declaration = scope.find_variable(variable_call.name);
            if(declaration == null){
                System.out.println(String.format("error cant find variable %s", variable_call.name));
                System.exit(0);
            }
            variable_call.type = declaration.type;
            return variable_call;
        }
        if(expression instanceof Literal){
            if(!top_level){
                return generate_variable_to(expression, generated_statements, type);
            }
            else{
                return expression;
            }
        }
        if(expression instanceof ProcedureCall){
            ProcedureCall procedure_call = (ProcedureCall) expression;
            ProcedureDeclaration procedure_declaration = scope.find_procedure(procedure_call.name);
            procedure_call.external = procedure_declaration.external;

            for(int i = 0; i < procedure_call.inputs.size(); i++){
                VariableDeclaration input = (VariableDeclaration) procedure_declaration.inputs.get(i);
                procedure_call.inputs.set(i, flatten_expression(procedure_call.inputs.get(i), generated_statements, false, input.type, scope));
            }

            if(!(procedure_declaration.outputs == null || procedure_declaration.outputs.isEmpty())){
                type = procedure_declaration.outputs.get(0); // TODO change for multiple return
            }

            if(!top_level){
                return generate_variable_to(procedure_call, generated_statements, type);
            }
            return procedure_call;
        }
        if(expression instanceof BinaryOperator){
            BinaryOperator operator = (BinaryOperator) expression;

            if(operator.operation == BinaryOperator.Operation.ASSIGN){
                // assign to array
                // assign to variable
                // assign to struct
                if(operator.left instanceof BinaryOperator){
                    BinaryOperator left = (BinaryOperator) operator.left;
                    switch (left.operation){
                        case DOT -> {
                            StructAssign struct_assign = new StructAssign();
                            struct_assign.struct = flatten_expression(left.left, generated_statements, false, null, scope);
                            struct_assign.field = left.right;
                            struct_assign.value = flatten_expression(operator.right, generated_statements, false, null, scope);
                            return struct_assign;
                        }
                        case INDEX -> {
                            ArrayAssign array_assign = new ArrayAssign();
                            array_assign.array = flatten_expression(left.left, generated_statements, false, null, scope);
                            array_assign.index = flatten_expression(left.right, generated_statements, false, LiteralType.INT(), scope);
                            array_assign.value = flatten_expression(operator.right, generated_statements, false, null, scope);
                            return array_assign;
                        }
                    }
                }
                else{
                    VariableAssign variable_assign = new VariableAssign();
                    operator.left = flatten_expression(operator.left, generated_statements, false, null, scope);
                    operator.right = flatten_expression(operator.right, generated_statements, false, null, scope);

                    VariableCall left = (VariableCall) operator.left;
                    VariableCall right = (VariableCall) operator.right;
                    variable_assign.variable_name = left.name;
                    variable_assign.value = right;
                    return variable_assign;
                }
            }

            operator.left = flatten_expression(operator.left, generated_statements, false, type, scope);

            if(operator.operation == BinaryOperator.Operation.DOT){
                if(!(operator.right instanceof VariableCall)){
                    System.out.println("dot operator cannot have expression on right hand side");
                    System.exit(0);
                }
            }
            else{
                operator.right = flatten_expression(operator.right, generated_statements, false, type, scope);
            }

            if(!top_level){

                // calculate return type
                if(operator.operation.is_comparison()){
                    LiteralType literal_type = new LiteralType();
                    literal_type.type = LiteralType.Type.BOOL;
                    type = literal_type;
                }
                if(type == null){
                    if(operator.left instanceof VariableCall){
                        VariableCall left = (VariableCall)operator.left;
                        type = left.type;
                    }
                    else{
                        System.out.println();
                    }
                }

                if(operator.operation == BinaryOperator.Operation.INDEX){
                    VariableCall array = (VariableCall) operator.left;
                    ArrayType array_type = (ArrayType) array.type;
                    type = array_type.type;
                }

                if(operator.operation == BinaryOperator.Operation.DOT){
                    VariableCall struct_call = (VariableCall) operator.left;

                    StructDeclaration struct;
                    if(struct_call.type instanceof PointerType){
                        struct = (StructDeclaration) ((PointerType)struct_call.type).type;
                    }
                    else{
                        if(struct_call.type instanceof LiteralType){
                            System.out.println();
                        }
                        struct = (StructDeclaration) struct_call.type;
                    }

                    VariableCall field = (VariableCall) operator.right;
                    for(Node node : struct.body){
                        VariableDeclaration struct_field = (VariableDeclaration) node;
                        if(struct_field.name.equals(field.name)){
                            type = struct_field.type;
                        }
                    }
                }
                // end

                return generate_variable_to(operator, generated_statements, type);
            }
            else{
                System.out.println("top level binary operator?");
            }
        }
        if(expression instanceof UnaryOperator){
            UnaryOperator operator = (UnaryOperator) expression;
            operator.node = flatten_expression(operator.node, generated_statements, false, type, scope);

            if(operator.operation == UnaryOperator.Operation.MINUS || operator.operation == UnaryOperator.Operation.PLUS){
                BinaryOperator binary_operator = new BinaryOperator();
                if(operator.operation == UnaryOperator.Operation.MINUS){
                    binary_operator.operation = BinaryOperator.Operation.SUBTRACT;
                }
                if(operator.operation == UnaryOperator.Operation.PLUS){
                    binary_operator.operation = BinaryOperator.Operation.ADD;
                }

                VariableCall var_call = (VariableCall) operator.node;
                LiteralType op_type = (LiteralType) var_call.type;
                switch (op_type.type){
                    case FLOAT -> {
                        Literal<Double> zero = new Literal<>(0.0);
                        binary_operator.left = generate_variable_to(zero, generated_statements, op_type);
                    }
                    case INT -> {
                        Literal<Integer> zero = new Literal<>(0);
                        binary_operator.left = generate_variable_to(zero, generated_statements, op_type);
                    }
                }
                binary_operator.right = operator.node;
                expression = binary_operator;
            }

            if(operator.operation == UnaryOperator.Operation.DEREFERENCE){
                PointerType pointer_type = (PointerType) ((VariableCall) operator.node).type;
                type = pointer_type.type;
            }

            if(operator.operation == UnaryOperator.Operation.REFERENCE){
                PointerType pointer_type = new PointerType();
                pointer_type.type = ((VariableCall)operator.node).type;
                type = pointer_type;
            }

            if(!top_level){
                return generate_variable_to(expression, generated_statements, type);
            }
            return expression;
        }
        return expression;
    }

    static List<Node> flatten(List<Node> input, Scope scope){
        List<Node> output = new ArrayList<>();
        for(Node node : input){
            if(node instanceof ProcedureDeclaration){
                ProcedureDeclaration procedure = (ProcedureDeclaration)node;

                Scope sub_scope = new Scope();
                sub_scope.enclosing_procedure = procedure;
                sub_scope.variables = new ArrayList<>();
                sub_scope.procedures = new ArrayList<>();
                sub_scope.structs = new ArrayList<>();
                sub_scope.variables.addAll(scope.variables);
                sub_scope.procedures.addAll(scope.procedures);
                sub_scope.structs.addAll(scope.structs);

                for(int i = 0; i < procedure.inputs.size(); i++){
                    VariableDeclaration declaration = (VariableDeclaration) procedure.inputs.get(i);
                    declaration.type = enrich_type(declaration.type, scope);
                    sub_scope.variables.add(declaration);
                }
                for(int i = 0; i < procedure.outputs.size(); i++){
                    procedure.outputs.set(i, enrich_type(procedure.outputs.get(i), scope));
                }

                procedure.block = flatten(procedure.block, sub_scope);
                output.add(procedure);
                continue;
            }
            if(node instanceof VariableDeclaration){
                VariableDeclaration declaration = (VariableDeclaration) node;
                declaration.type = enrich_type(declaration.type, scope);

                scope.variables.add(declaration);
                output.add(declaration);
                if(declaration.value != null) {
                    VariableAssign assign = new VariableAssign();
                    assign.variable_name = declaration.name;
                    assign.value = declaration.value;
                    declaration.value = null;
                    assign.value = flatten_expression(assign.value, output, false, declaration.type, scope);
                    VariableCall call = (VariableCall) assign.value;
                    if(declaration.type == null){
                        declaration.type = call.type;
                    }
                    else{
                        // check type equality
                    }
                    output.add(assign);
                }
                continue;
            }
            if(node instanceof Return){
                Return return_statement = (Return) node;
                return_statement.procedure = scope.enclosing_procedure;
                return_statement.type = scope.enclosing_procedure.outputs.get(0);
                return_statement.value = flatten_expression(return_statement.value, output, false, return_statement.type, scope);
                output.add(return_statement);
                continue;
            }
            if(node instanceof If){
                If if_statement = (If) node;
                Scope sub_scope = new Scope();
                sub_scope.enclosing_procedure = scope.enclosing_procedure;
                sub_scope.variables = new ArrayList<>();
                sub_scope.procedures = new ArrayList<>();
                sub_scope.variables.addAll(scope.variables);
                sub_scope.procedures.addAll(scope.procedures);

                if_statement.condition = flatten_expression(if_statement.condition, output, false, null, sub_scope);
                if_statement.block = flatten(if_statement.block, sub_scope);
                output.add(if_statement);
                continue;
            }
            if(node instanceof While){
                While while_statement = (While) node;
                Scope sub_scope = new Scope();
                sub_scope.enclosing_procedure = scope.enclosing_procedure;
                sub_scope.variables = new ArrayList<>();
                sub_scope.procedures = new ArrayList<>();
                sub_scope.variables.addAll(scope.variables);
                sub_scope.procedures.addAll(scope.procedures);

                while_statement.condition = flatten_expression(while_statement.condition, while_statement.condition_block, false, null, sub_scope);
                while_statement.block = flatten(while_statement.block, sub_scope);
                output.add(while_statement);
                continue;
            }

            output.add(flatten_expression(node, output, true, null, scope));
        }
        return output;
    }

    static Node convert_java_type(String java_type){
        LiteralType literal_type = new LiteralType();

        literal_type.type = switch (java_type){
            case "long" -> LiteralType.Type.INT;
            case "double" -> LiteralType.Type.FLOAT;
            default -> null;
        };
        return literal_type;
    }

    public static void main(String[] args) {
        String program_text = read_file_as_text("main.graph");
        List<Token> tokens = tokenize(program_text);
        //print_tokens(tokens, program_text);

        Tokenizer tokenizer = new Tokenizer();
        tokenizer.tokens = tokens;
        tokenizer.program_text = program_text;


        List<Node> program = parse_program(tokenizer);
        List<ProcedureDeclaration> procedures = new ArrayList<>();
        List<StructDeclaration> structs = new ArrayList<>();

        // external procedures
        for(Method method : ExternalProcedures.class.getDeclaredMethods()){
            ProcedureDeclaration external_procedure = new ProcedureDeclaration();
            external_procedure.name = Utils.external_name(method.getName());
            external_procedure.external = true;
            external_procedure.inputs = new ArrayList<>();
            for(Parameter parameter : method.getParameters()){
                VariableDeclaration declaration = new VariableDeclaration();
                declaration.name = parameter.getName();
                declaration.type = convert_java_type(parameter.getType().getName());
                external_procedure.inputs.add(declaration);
            }
            procedures.add(external_procedure);
        }

        List<VariableDeclaration> globals = new ArrayList<>();
        for(Node node : program){
            if(node instanceof ProcedureDeclaration){
                procedures.add((ProcedureDeclaration) node);
                continue;
            }
            if(node instanceof VariableDeclaration){
                globals.add((VariableDeclaration) node);
            }
            if(node instanceof StructDeclaration){
                structs.add((StructDeclaration) node);
            }
        }

        Scope global_scope = new Scope();
        global_scope.procedures = procedures;
        global_scope.variables = globals;
        global_scope.structs = structs;

        program = flatten(program, global_scope);

        BytecodeGenerator generator = new BytecodeGenerator();
        BytecodeProgram bytecode = generator.generate_bytecode(program);
        VirtualMachine vm = new VirtualMachine();

        vm.run(bytecode.code, bytecode.entry_point);
    }
}
