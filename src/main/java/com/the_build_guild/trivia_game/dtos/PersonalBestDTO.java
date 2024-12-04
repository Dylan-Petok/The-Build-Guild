package com.the_build_guild.trivia_game.dtos;

import java.util.Date;

public class PersonalBestDTO {
    private int gameScore;
    private Date datePlayed;
    private String topic;
    private String difficulty;

    public PersonalBestDTO(int gameScore, Date datePlayed, String topic, String difficulty) {
        this.gameScore = gameScore;
        this.datePlayed = datePlayed;
        this.topic = topic;
        this.difficulty = difficulty;
    }

    public int getGameScore() {
        return gameScore;
    }

    public void setGameScore(int gameScore) {
        this.gameScore = gameScore;
    }

    public Date getDatePlayed() {
        return datePlayed;
    }

    public void setDatePlayed(Date datePlayed) {
        this.datePlayed = datePlayed;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
