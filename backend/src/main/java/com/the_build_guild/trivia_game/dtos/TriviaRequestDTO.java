package com.the_build_guild.trivia_game.dtos;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TriviaRequestDTO {
    private String numberOfQuestions;
    private String difficulty;
    private String category;

}