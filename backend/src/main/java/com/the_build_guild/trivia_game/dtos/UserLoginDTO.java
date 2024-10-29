package com.the_build_guild.trivia_game.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDTO {
    
    @JsonProperty("username")
    private String userName;

    @JsonProperty("password")
    private String password;
    
    }
