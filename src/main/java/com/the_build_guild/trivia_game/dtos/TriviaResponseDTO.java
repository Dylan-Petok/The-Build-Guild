package com.the_build_guild.trivia_game.dtos;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TriviaResponseDTO {
    @JsonProperty("response_code")
    private int responseCode;
    
    @JsonProperty("results")
    private List<Question> results;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Question {
        private String type;
        private String difficulty;
        private String category;
        private String question;
        
        @JsonProperty("correct_answer")
        private String correctAnswer;
        
        @JsonProperty("incorrect_answers")
        private List<String> incorrectAnswers;
    }
}
