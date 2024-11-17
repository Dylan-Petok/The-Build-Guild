package com.the_build_guild.trivia_game.models;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.Date;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "game_user")
@EqualsAndHashCode(callSuper = false)
public class Game {
    @Id
    private String gameId;
    private String userId;
    private String topic;
    private String difficulty;
    private Integer correctAnswers;
    private Integer totalQuestions;
    private Date datePlayed;
    private Integer score;
}
