package com.the_build_guild.trivia_game.dtos;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileInfoDTO {
    private int gamesPlayedCount;
    private int highestScoreInGame;
    private int globalRank;
    private int currentScore;
}