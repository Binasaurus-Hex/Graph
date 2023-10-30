package main;

import SyntaxNodes.*;
import Bytecode.VirtualMachine;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
        int token_size = token_text.length();
        if(program_text_index + token_size > program_text.length())return false;

        for(int i = 0; i < token_size; i++){
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
            case IN:            return BinaryOperator.Operation.IN;
            case AND:           return BinaryOperator.Operation.AND;
            case OR:            return BinaryOperator.Operation.OR;
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
        boolean inside_comment = false;
        boolean inside_line_comment = false;
        while (text_index < program_text.length()){

            if(inside_comment){
                if(matches(Token.Type.COMMENT_END.text, program_text, text_index)){
                    inside_comment = false;
                    text_index += Token.Type.COMMENT_END.text.length();
                    last_token_index = text_index;
                }
                else{
                    text_index++;
                }
                continue;
            }
            if(inside_line_comment){
                 if(matches(Token.Type.NEWLINE.text, program_text, text_index)){
                     inside_line_comment = false;
                     text_index += Token.Type.NEWLINE.text.length();
                     last_token_index = text_index;
                 }
                 else{
                     text_index++;
                     continue;
                 }
            }

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
                            String keyword_text = keyword.getValue();
                            if(identifier.length != keyword_text.length())continue;
                            if(matches(keyword_text, program_text, last_token_index)){
                                identifier.type = keyword.getKey();
                                break;
                            }
                        }
                        tokens.add(identifier);
                    }

                    Token token = new Token(token_type, text_index);
                    text_index += token_type.text.length();
                    last_token_index = text_index;
                    if(token.type == Token.Type.COMMENT_START){
                        inside_comment = true;
                    }
                    if(token.type == Token.Type.COMMENT_LINE){
                        inside_line_comment = true;
                    }
                    else{
                        tokens.add(token);
                    }
                    found_token = true;
                }
            }
            if(found_token)continue;

            text_index ++;
        }

        return tokens;
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
            case DOT -> {
                Token number = tokenizer.peek_token(2);
                if(number.type == Token.Type.INTEGER){
                    tokenizer.eat_token(); // .
                    tokenizer.eat_token(); // INT
                    String decimal = get_identifier_text(tokenizer.program_text, number);
                    return new Literal<Double>(Double.parseDouble("."+decimal));
                }
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
            case OPEN_BRACE -> {
                // struct literal
                List<Node> struct_args = parse_variadic(tokenizer,
                        Main::parse_expression,
                        Token.Type.OPEN_BRACE,
                        Token.Type.COMMA,
                        Token.Type.CLOSE_BRACE);
                StructLiteral struct_literal = new StructLiteral();
                struct_literal.arguments = struct_args;
                return struct_literal;
            }
            case OPEN_BRACKET -> {
                // array literal
                List<Node> array_args = parse_variadic(tokenizer,
                        Main::parse_expression,
                        Token.Type.OPEN_BRACKET,
                        Token.Type.COMMA,
                        Token.Type.CLOSE_BRACKET);
                ArrayLiteral array_literal = new ArrayLiteral();
                array_literal.arguments = array_args;
                return array_literal;
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

            case DIRECTIVE -> {
                tokenizer.eat_token();
                Token directive = tokenizer.peek_token();
                if(directive.type != Token.Type.IDENTIFIER){
                    Utils.print_token_error(tokenizer, "invalid directive");
                }
                tokenizer.eat_token();
                String text = get_identifier_text(tokenizer.program_text, directive);
                if(text.equals("run")){
                    RunDirective run_directive = new RunDirective();
                    run_directive.expression = parse_expression(tokenizer);
                    return run_directive;
                }
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
            unary_operator.node = parse_expression(tokenizer);
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
            if(!is_block_statement(statement)){
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
                        do {
                            // PARSE TYPE
                            Node output_type = parse_type(tokenizer);
                            if(output_type == null){
                                Utils.print_token_error(tokenizer, "unknown type definition");
                            }
                            procedure.outputs.add(output_type);
                            if(tokenizer.peek_token().type != Token.Type.COMMA)break;
                        }
                        while (tokenizer.peek_token().type != Token.Type.OPEN_BRACE);
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
                if(if_statement.block == null){
                    // try parse inline if
                    if_statement.block = new ArrayList<>();
                    if_statement.block.add(parse_statement(tokenizer));
                }
                return if_statement;
            }
            else if(keyword.type == Token.Type.FOR){
                For for_statement = new For();
                Node iteration_or_sequence = parse_expression(tokenizer);
                for_statement.sequence = iteration_or_sequence;
                if(iteration_or_sequence instanceof BinaryOperator){
                    BinaryOperator operator = (BinaryOperator) iteration_or_sequence;
                    if(operator.operation == BinaryOperator.Operation.IN){
                        for_statement.iteration = (VariableCall) operator.left;
                        for_statement.sequence = operator.right;
                    }
                }

                for_statement.block = parse_block(tokenizer);
                return for_statement;
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

    static boolean is_block_statement(Node statement){
        if(statement instanceof StructDeclaration) return true;
        if(statement instanceof ProcedureDeclaration) return true;
        if(statement instanceof While) return true;
        if(statement instanceof If) return true;
        if(statement instanceof For) return true;
        return false;
    }

    static List<Node> parse_program(Tokenizer tokenizer){
        List<Node> statements = new ArrayList<>();

        while (tokenizer.peek_token() != null){
            Node statement = parse_statement(tokenizer);

            if(!is_block_statement(statement)){
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

    static BinaryOperator fix_dot_tree(BinaryOperator dot_operator){
        BinaryOperator fixed = dot_operator;
        if(dot_operator.right instanceof BinaryOperator){
            BinaryOperator right = (BinaryOperator) dot_operator.right;
            dot_operator.right = right.left;
            right.left = dot_operator;
            fixed = right;
        }
        return fixed;
    }

    static boolean types_equal(Node a, Node b){

        // nested structures
        if(a instanceof Location){
            Location location_a = (Location) a;
            if(! (b instanceof Location)){
                return types_equal(location_a.type, b);
            }
            Location location_b = (Location) b;
            return types_equal(location_a.type, location_b.type);
        }
        if(b instanceof Location){
            Location location_b = (Location) b;
            return types_equal(location_b.type, a);
        }
        if(a instanceof PointerType){
            if(! (b instanceof PointerType))return false;
            PointerType pointer_a = (PointerType) a;
            PointerType pointer_b = (PointerType) b;
            return types_equal(pointer_a.type, pointer_b.type);
        }
        if(a instanceof ArrayType){
            if(!(b instanceof ArrayType))return false;
            ArrayType array_a = (ArrayType) a;
            ArrayType array_b = (ArrayType) b;
            return types_equal(array_a.type, array_b.type) && array_a.size == array_b.size;
        }

        if(a instanceof StructDeclaration){
            if(! (b instanceof StructDeclaration))return false;
            StructDeclaration struct_a = (StructDeclaration) a;
            StructDeclaration struct_b = (StructDeclaration) b;
            return struct_a.name.equals(struct_b.name);
        }

        if(a instanceof LiteralType){
            if(! (b instanceof LiteralType))return false;
            LiteralType literal_a = (LiteralType) a;
            LiteralType literal_b = (LiteralType) b;
            return literal_a.type == literal_b.type;
        }
        return false;
    }

    static String type_to_string(Node type){
        if(type instanceof PointerType){
            PointerType pointer = (PointerType) type;
            return "*"+type_to_string(pointer.type);
        }
        if(type instanceof ArrayType){
            ArrayType array = (ArrayType)type;
            return "["+array.size+"]"+type_to_string(array.type);
        }
        if(type instanceof StructDeclaration){
            StructDeclaration struct = (StructDeclaration) type;
            return struct.name;
        }
        if(type instanceof LiteralType){
            LiteralType literal = (LiteralType) type;
            return switch (literal.type){
                case INT -> "int";
                case FLOAT -> "float";
                case BOOL -> "bool";
            };
        }
        return "unknown type";
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

    static String get_unique_name(){
        return String.valueOf(generated_name_counter++);
    }

    static VariableCall generate_variable_to(Node expression, List<Node> generated_statements, Node type){
        VariableDeclaration declaration = new VariableDeclaration();
        declaration.name = get_unique_name();
        declaration.type = type;

        if(expression instanceof Literal){
            declaration.type = get_type((Literal)expression);
        }

        generated_statements.add(declaration);

        VariableAssign assign = new VariableAssign();
        assign.location = declaration.type instanceof Location;
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
        if(expression instanceof RunDirective){
            RunDirective run_directive = (RunDirective) expression;
            List<Node> run_directive_statements = new ArrayList<>();
            Scope directive_scope = scope.duplicate();
            run_directive.expression = flatten_expression(run_directive.expression, run_directive_statements, false, null, directive_scope);

            List<Node> full_directive = new ArrayList<>();
            if(type == null){
                VariableCall run_expression = (VariableCall) run_directive.expression;
                type = run_expression.type;
            }
            for(ProcedureDeclaration procedure : scope.procedures){
                if(procedure.name.equals("main"))continue;
                else if(procedure == scope.enclosing_procedure)continue;
                else if(procedure.external)continue;
                full_directive.add(procedure);
            }

            ProcedureDeclaration run_procedure = new ProcedureDeclaration();
            run_procedure.name = "main";
            run_procedure.outputs = new ArrayList<>();
            run_procedure.inputs = new ArrayList<>();
            run_procedure.outputs.add(type);
            Return return_statement = new Return();
            return_statement.procedure = run_procedure;
            return_statement.type = type;
            return_statement.value = run_directive.expression;
            run_directive_statements.add(return_statement);

            run_procedure.block = run_directive_statements;


            full_directive.add(run_procedure);
            ProcedureCall procedureCall = new ProcedureCall();
            procedureCall.name = run_procedure.name;
            procedureCall.procedure = run_procedure;
            procedureCall.inputs = new ArrayList<>();
            full_directive.add(procedureCall);

            BytecodeGenerator generator = new BytecodeGenerator();
            int[] bytecode = generator.generate_bytecode(full_directive);
            VirtualMachine vm = new VirtualMachine();
            int[] result = vm.run(bytecode);
            RawData raw_data = new RawData();
            for(int i = 0; i < generator.get_size(return_statement.type); i++){
                raw_data.data.add(result[i]);
            }
            return generate_variable_to(raw_data, generated_statements, type);
        }


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
        if(expression instanceof StructLiteral){
            StructLiteral literal = (StructLiteral)expression;
            StructDeclaration struct = (StructDeclaration) type;

            // make an anonymous struct
            VariableDeclaration temp_struct = new VariableDeclaration();
            temp_struct.name = get_unique_name();
            temp_struct.type = type;

            generated_statements.add(temp_struct);
            scope.variable_map.put(temp_struct.name, temp_struct);

            VariableCall temp_struct_call = new VariableCall();
            temp_struct_call.name = temp_struct.name;
            temp_struct_call.type = temp_struct.type;

            int i = 0;
            for(Node node : struct.body){
                VariableDeclaration field = (VariableDeclaration) node;
                VariableCall field_call = new VariableCall();
                field_call.name = field.name;
                field_call.type = field.type;

                BinaryOperator assign = new BinaryOperator();
                assign.operation = BinaryOperator.Operation.ASSIGN;

                BinaryOperator dot = new BinaryOperator();
                dot.operation = BinaryOperator.Operation.DOT;

                dot.left = temp_struct_call;
                dot.right = field_call;

                assign.left = dot;
                assign.right = literal.arguments.get(i);

                VariableAssign assignment = (VariableAssign) flatten_expression(assign, generated_statements, false, null, scope);

                generated_statements.add(assignment);
                i++;
            }

            return temp_struct_call;
        }

        if(expression instanceof ArrayLiteral){
            ArrayLiteral array_literal = (ArrayLiteral) expression;
            ArrayType array_type = (ArrayType) type;

            VariableDeclaration temp_array = new VariableDeclaration();
            temp_array.name = get_unique_name();
            temp_array.type = array_type;

            for(int i = 0; i < array_type.size; i++){

            }
        }

        if(expression instanceof ProcedureCall){
            ProcedureCall procedure_call = (ProcedureCall) expression;
            if(procedure_call.name.equals("len")){
                VariableCall array_call = (VariableCall)flatten_expression(procedure_call.inputs.get(0), generated_statements, false, null, scope);
                if(!(array_call.type instanceof ArrayType)){
                    System.out.println("len only accepts arguments of arrays");
                    System.exit(0);
                }
                ArrayType array = (ArrayType) array_call.type;
                Literal<Integer> size = new Literal<>();
                size.value = array.size;
                return generate_variable_to(size, generated_statements, null);
            }

            ProcedureDeclaration procedure_declaration = scope.find_procedure(procedure_call.name);
            procedure_call.external = procedure_declaration.external;
            procedure_call.procedure = procedure_declaration;

            for(int i = 0; i < procedure_call.inputs.size(); i++){
                VariableDeclaration input = (VariableDeclaration) procedure_declaration.inputs.get(i);
                VariableCall flattened_input = (VariableCall) flatten_expression (procedure_call.inputs.get(i), generated_statements, false, input.type, scope);

                // locations can be cast to either pointer or value
                if(flattened_input.type instanceof Location){
                    Location location = (Location) flattened_input.type;
                    if(input.type instanceof PointerType){
                        PointerType pointer = new PointerType();
                        pointer.type = location.type;
                        flattened_input.type = pointer;
                    }
                    else{
                        flattened_input = generate_variable_to(flattened_input, generated_statements, location.type);
                    }
                }
                else{
                    // for other types we do explicit reference
                    if(input.type instanceof PointerType && !(flattened_input.type instanceof PointerType)){
                        UnaryOperator reference = new UnaryOperator();
                        reference.operation = UnaryOperator.Operation.REFERENCE;
                        reference.node = flattened_input;

                        PointerType pointer = new PointerType();
                        pointer.type = flattened_input.type;

                        flattened_input = generate_variable_to(reference, generated_statements, pointer);
                    }
                }


                if(!types_equal(input.type, flattened_input.type)){
                    System.out.println(String.format("error type mismatch %s cannot be assigned to variable of type %s", input.name, type_to_string(flattened_input.type)));
                    System.exit(0);
                }

                procedure_call.inputs.set(i, flattened_input);
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

            boolean flatten_right = true;
            if(operator.operation == BinaryOperator.Operation.DOT){
                operator = fix_dot_tree(operator);
                flatten_right = false;
            }

            operator.left = flatten_expression(operator.left, generated_statements, false, null, scope);
            VariableCall left = (VariableCall) operator.left;

            Node right_type = null;
            if(operator.operation == BinaryOperator.Operation.ASSIGN){
                if(left.type instanceof Location){
                    right_type = ((Location) left.type).type;
                }
                else{
                    right_type = left.type;
                }
            }

            if(flatten_right)  operator.right = flatten_expression(operator.right, generated_statements, false, right_type, scope);

            VariableCall right = (VariableCall) operator.right;

            type = left.type;

            switch (operator.operation){
                case DOT -> {
                    Node struct_type = left.type;
                    StructDeclaration struct;
                    if(struct_type instanceof Location){
                        Location location = (Location) struct_type;
                        struct = (StructDeclaration) location.type;
                    }
                    else if(struct_type instanceof PointerType){
                        PointerType pointer = (PointerType) struct_type;
                        struct = (StructDeclaration) pointer.type;
                    }
                    else{
                        struct = (StructDeclaration) struct_type;
                    }

                    VariableCall field = (VariableCall) operator.right;
                    for(Node node : struct.body){
                        VariableDeclaration struct_field = (VariableDeclaration) node;
                        if(struct_field.name.equals(field.name)){
                            type = struct_field.type;
                        }
                    }
                    Location location = new Location();
                    location.type = type;
                    type = location;
                }
                case INDEX -> {
                    Node array_type = left.type;
                    ArrayType array;
                    if(array_type instanceof Location){
                        Location location = (Location) array_type;
                        array = (ArrayType) location.type;
                    }
                    else if(array_type instanceof PointerType){
                        PointerType pointer = (PointerType) array_type;
                        array = (ArrayType) pointer.type;
                    }
                    else {
                        array = (ArrayType) array_type;
                    }

                    type = array.type;

                    Location location = new Location();
                    location.type = type;
                    type = location;
                }
                case ASSIGN -> {
                    VariableAssign variable_assign = new VariableAssign();
                    variable_assign.variable_name = left.name;
                    variable_assign.location = left.type instanceof Location;
                    variable_assign.top_level = top_level;
                    variable_assign.value = right;
                    if(!types_equal(left.type, right.type)){
                        System.out.println(String.format("error type mismatch %s cannot be assigned to variable of type %s", left.name, type_to_string(right.type)));
                        System.exit(0);
                    }
                    return variable_assign;
                }
                default -> {
                    if(left.type instanceof Location){
                        Location location = (Location) left.type;
                        operator.left = generate_variable_to(left, generated_statements, location.type);
                        type = location.type;
                    }
                    if(right.type instanceof Location){
                        Location location = (Location) right.type;
                        operator.right = generate_variable_to(right, generated_statements, location.type);
                    }
                }
            }

            if(operator.operation.is_comparison() || operator.operation.is_logical_operator()){
                LiteralType literal_type = new LiteralType();
                literal_type.type = LiteralType.Type.BOOL;
                type = literal_type;
            }

            return generate_variable_to(operator, generated_statements, type);
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
                        Literal<Float> zero = new Literal<>(0.0f);
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
                PointerType pointer = new PointerType();
                pointer.type = ((VariableCall)operator.node).type;
                if(pointer.type instanceof Location){ // since locations are essentially pointers, we can just take a pointer to the underlying value
                    Location location = (Location) pointer.type;
                    pointer.type = location.type;
                }
                type = pointer;
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

                Scope sub_scope = scope.duplicate();
                sub_scope.enclosing_procedure = procedure;

                for(int i = 0; i < procedure.inputs.size(); i++){
                    VariableDeclaration declaration = (VariableDeclaration) procedure.inputs.get(i);
                    declaration.type = enrich_type(declaration.type, scope);
                    sub_scope.variable_map.put(declaration.name, declaration);
                }
                for(int i = 0; i < procedure.outputs.size(); i++){
                    procedure.outputs.set(i, enrich_type(procedure.outputs.get(i), scope));
                }

                procedure.block = flatten(procedure.block, sub_scope);
                output.add(procedure);
                continue;
            }
            if(node instanceof StructDeclaration){
                StructDeclaration struct = (StructDeclaration) node;
                for(Node value : struct.body){
                    if(!(value instanceof VariableDeclaration)){
                        System.out.println("struct must only contain fields");
                        System.exit(1);
                    }
                    VariableDeclaration field = (VariableDeclaration) value;
                    field.type = enrich_type(field.type, scope);
                }
            }
            if(node instanceof VariableDeclaration){
                VariableDeclaration declaration = (VariableDeclaration) node;
                declaration.type = enrich_type(declaration.type, scope);

                scope.variable_map.put(declaration.name, declaration);
                output.add(declaration);
                if(declaration.value != null) {
                    VariableAssign assign = new VariableAssign();
                    assign.location = declaration.type instanceof Location;

                    assign.variable_name = declaration.name;
                    assign.value = declaration.value;
                    declaration.value = null;
                    assign.value = flatten_expression(assign.value, output, false, declaration.type, scope);
                    VariableCall call = (VariableCall) assign.value;
                    if(declaration.type == null){
                        declaration.type = call.type;
                    }
                    if(!types_equal(declaration.type, call.type)){
                        System.out.println(String.format(
                                "Error : can't assign %s of type %s to type %s",
                                declaration.name, type_to_string(declaration.type), type_to_string(call.type)));
                        System.exit(1);
                    }
                    output.add(assign);
                }
                continue;
            }
            if(node instanceof Return){
                Return return_statement = (Return) node;
                return_statement.procedure = scope.enclosing_procedure;
                List<Node> outputs = scope.enclosing_procedure.outputs;
                if(!outputs.isEmpty()){
                    return_statement.type = outputs.get(0);
                }
                return_statement.value = flatten_expression(return_statement.value, output, false, return_statement.type, scope);
                output.add(return_statement);
                continue;
            }
            if(node instanceof If){
                If if_statement = (If) node;
                Scope sub_scope = scope.duplicate();

                if_statement.condition = flatten_expression(if_statement.condition, output, false, null, sub_scope);
                if_statement.block = flatten(if_statement.block, sub_scope);
                output.add(if_statement);
                continue;
            }
            if(node instanceof While){
                While while_statement = (While) node;
                Scope sub_scope = scope.duplicate();

                while_statement.condition = flatten_expression(while_statement.condition, while_statement.condition_block, false, null, sub_scope);
                while_statement.block = flatten(while_statement.block, sub_scope);
                output.add(while_statement);
                continue;
            }
            if(node instanceof For){
                For for_statement = (For) node;
                Scope sub_scope = scope.duplicate();
                VariableCall sequence = (VariableCall) flatten_expression(for_statement.sequence, output, false, null, sub_scope);
                if(!(sequence.type instanceof ArrayType)){
                    System.out.println("for loop sequence must be an array");
                    System.exit(1);
                }
                ArrayType array_type = (ArrayType)sequence.type;

                VariableDeclaration iteration = new VariableDeclaration();
                iteration.name = for_statement.iteration.name;
                Location iteration_type = new Location();
                iteration_type.type = array_type.type;
                iteration.type = iteration_type;
                output.add(iteration);
                sub_scope.variable_map.put(iteration.name, iteration);
                VariableCall iteration_call = new VariableCall();
                iteration_call.name = iteration.name;
                iteration_call.type = iteration.type;

                VariableDeclaration index = new VariableDeclaration();
                index.name = get_unique_name();
                index.type = LiteralType.INT();
                output.add(index);
                sub_scope.variable_map.put(index.name, index);
                VariableAssign index_assign = new VariableAssign();
                index_assign.variable_name = index.name;
                index_assign.value = new Literal<>(0);
                output.add(index_assign);

                VariableCall index_call = new VariableCall();
                index_call.type = index.type;
                index_call.name = index.name;

                Literal<Integer> sequence_size = new Literal<>(array_type.size);
                VariableCall sequence_size_call = generate_variable_to(sequence_size, output, null);

                Literal<Integer> one = new Literal<>(1);
                VariableCall one_call = generate_variable_to(one, output, null);

                While while_statement = new While();
                BinaryOperator condition = new BinaryOperator();
                condition.operation = BinaryOperator.Operation.LESS_THAN;
                condition.left = index_call;
                condition.right = sequence_size_call;

                while_statement.condition = generate_variable_to(condition, while_statement.condition_block, LiteralType.BOOL());

                VariableAssign index_increment = new VariableAssign();
                index_increment.variable_name = index.name;
                BinaryOperator increment = new BinaryOperator();
                increment.operation = BinaryOperator.Operation.ADD;
                increment.left = index_call;
                increment.right = one_call;
                index_increment.value = increment;

                BinaryOperator array_index = new BinaryOperator();
                array_index.operation = BinaryOperator.Operation.INDEX;
                array_index.left = sequence;
                array_index.right = index_call;

                List<Node> loop_block = new ArrayList<>();
                BinaryOperator iteration_assign = new BinaryOperator();
                iteration_assign.operation = BinaryOperator.Operation.ASSIGN;
                iteration_assign.left = iteration_call;
                iteration_assign.right = array_index;

                VariableAssign assign = (VariableAssign) flatten_expression(iteration_assign, loop_block, false, null, sub_scope);
                loop_block.add(assign);

                loop_block.addAll(flatten(for_statement.block, sub_scope));
                loop_block.add(index_increment);

                while_statement.block = loop_block;
                output.add(while_statement);
                continue;
            }

            output.add(flatten_expression(node, output, true, null, scope));
        }
        return output;
    }

    static LiteralType convert_java_type(String java_type){
        LiteralType literal_type = new LiteralType();

        literal_type.type = switch (java_type){
            case "int" -> LiteralType.Type.INT;
            case "float" -> LiteralType.Type.FLOAT;
            case "boolean" -> LiteralType.Type.BOOL;
            default -> null;
        };
        return literal_type;
    }



    public static void main(String[] args) {

        long start = System.nanoTime();

        String program_name = Path.of(args[0]).getFileName().toString().split("\\.")[0];
        String program_text = read_file_as_text(args[0]);
        List<Token> tokens = tokenize(program_text);
        System.out.printf("tokenizing :: %f seconds\n", (System.nanoTime() - start) / 1e9);

        long stopMark = System.nanoTime();

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
            LiteralType return_type = convert_java_type(method.getReturnType().getName());
            if(return_type.type != null){
                external_procedure.outputs = new ArrayList<>();
                external_procedure.outputs.add(return_type);
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
        //global_scope.variables = globals;
        global_scope.structs = structs;

        System.out.printf("parsing :: %f seconds\n", (System.nanoTime() - stopMark) / 1e9);
        stopMark = System.nanoTime();

        ProcedureCall main_call = new ProcedureCall();
        main_call.name = "main";
        main_call.inputs = new ArrayList<>();
        program.add(main_call);

        program = flatten(program, global_scope);
        System.out.printf("flattening :: %f seconds\n", (System.nanoTime() - stopMark) / 1e9);

        stopMark = System.nanoTime();

        BytecodeGenerator generator = new BytecodeGenerator();
        int[] bytecode = generator.generate_bytecode(program);

        System.out.printf("bytecode generation :: %f seconds\n", (System.nanoTime() - stopMark) / 1e9);
        stopMark = System.nanoTime();

        ByteBuffer buffer = ByteBuffer.allocate(bytecode.length * Integer.BYTES);
        buffer.asIntBuffer().put(bytecode);
        try {
            Files.write(Path.of(String.format("%s.bytecode", program_name)), buffer.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.printf("file write :: %f seconds\n", (System.nanoTime() - stopMark) / 1e9);
        System.out.printf("compile took %f seconds\n", (System.nanoTime() - start) / 1e9);

        if (args.length == 1)return;
        if(args[1].equals("-run") || args[1].equals("-r")){
            VirtualMachine vm = new VirtualMachine();
            int[] output = vm.run(bytecode);
            System.out.println("return code : " + output[0]); // return code
        }
    }
}
