package com.the_build_guild.trivia_game.controllers;


public class FriendsDTO {
    private String username;
    private int score;

    // Constructor
    public FriendsDTO(String username, int score) {
        this.username = username;
        this.score = score;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
