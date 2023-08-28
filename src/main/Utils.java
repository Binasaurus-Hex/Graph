package main;

public class Utils {

    public static void print_token_error(Tokenizer tokenizer, String message){
        Token current_token = tokenizer.tokens.get(tokenizer.token_index);
        int line_start = current_token.index;
        int line_end = current_token.index;
        while (line_start > 0){
            if(tokenizer.program_text.charAt(--line_start) == '\n'){
                line_start++;
                break;
            }
        }

        while (line_end < tokenizer.program_text.length()){
            if(tokenizer.program_text.charAt(++line_end) == '\n'){
                line_end--;
                break;
            }
        }

        int previous_line_start = line_start - 1;
        while (previous_line_start > 0){
            if(tokenizer.program_text.charAt(--previous_line_start) == '\n'){
                previous_line_start++;
                break;
            }
        }

        System.out.println(message);
        String previous_line = tokenizer.program_text.substring(previous_line_start, line_start - 1);
        System.out.println(previous_line);
        String line = tokenizer.program_text.substring(line_start, line_end);
        System.out.println(line);
        int error_index = current_token.index - line_start;
        for(int i = 0; i < error_index; i++){
            System.out.print("_");
        }
        System.out.println("^");
        System.exit(0);
    }

}
