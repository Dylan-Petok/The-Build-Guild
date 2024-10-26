package com.the_build_guild.trivia_game.models;
import java.lang.annotation.Inherited;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "leaderboard")
@EqualsAndHashCode(callSuper = false)
public class Leaderboard {
    @Id
    private String leaderBoardId;
    private String userId;
    private Integer score;
    private String rank;
}
