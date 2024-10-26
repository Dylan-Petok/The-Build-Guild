package com.the_build_guild.trivia_game.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
@EqualsAndHashCode(callSuper = false)
public class User{

    @Id
    private String Id;
    private String username;
    private String email;
    private String passwordHash;
    private String[] friends;
    private String[] gamesPlayed;
    private Integer winCount;
}