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
public class UserCreationDTO {
    
    @JsonProperty("username")
    private String userName;

    @JsonProperty("email")
    private String emailAddr;

    @JsonProperty("password")
    private String password;
    
    }

