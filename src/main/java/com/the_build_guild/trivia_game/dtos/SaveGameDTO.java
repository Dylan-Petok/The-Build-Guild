package com.the_build_guild.trivia_game.dtos;


import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveGameDTO {
    private String userId;
    private String topic;
    private String difficulty;
    private Integer correctAnswers;
    private Integer totalQuestions;
    private Integer gameScore;
    private Date datePlayed;
}
