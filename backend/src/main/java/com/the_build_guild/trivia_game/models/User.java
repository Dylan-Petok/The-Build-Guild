package com.the_build_guild.trivia_game.models;

import jakarta.persistence.*;

import lombok.*;

@Data
@Entity 
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "game_user")
@EqualsAndHashCode(callSuper = false)
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;


    @Column(unique = true)
    private String username;


    public String getUsername(){
        return username;
    }
    
    public void setUsername(String username){
        this.username = username;
    }

}
