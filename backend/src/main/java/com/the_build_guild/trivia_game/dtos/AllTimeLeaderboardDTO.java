package com.the_build_guild.trivia_game.dtos;

public class AllTimeLeaderboardDTO {

    private String username;
    private int score;

    public AllTimeLeaderboardDTO(String username, int score) {
        this.username = username;
        this.score = score;
    }

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

