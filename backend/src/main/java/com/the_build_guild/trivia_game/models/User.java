package com.the_build_guild.trivia_game.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "game_user")
@EqualsAndHashCode(callSuper = false)
public class User{

    @Id
    private String Id;


    private String username;


    public String getUsername(){
        return username;
    }
    
    public void setUsername(String username){
        this.username = username;
    }

}
