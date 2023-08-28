package main;

import java.util.List;

public class Tokenizer {
    int token_index;
    String program_text;
    List<Token> tokens;

    Token eat_token(){
        while(token_index < tokens.size()){
            Token next_token = tokens.get(token_index++);
            if(next_token.is_punctuation()){
                continue;
            }
            return next_token;
        }
        return null;
    }

    Token peek_token(int lookahead){
        int found_tokens = 0;
        int peek_index = token_index;
        while (peek_index < tokens.size()){
            Token peek_token = tokens.get(peek_index++);
            if(peek_token.is_punctuation()){
                continue;
            }
            found_tokens++;
            if(found_tokens == lookahead){
                return peek_token;
            }
        }
        return null;
    }

    Token peek_token(){
        return peek_token(1);
    }
}
