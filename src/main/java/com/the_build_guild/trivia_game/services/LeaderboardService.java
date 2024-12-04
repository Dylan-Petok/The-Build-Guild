package com.the_build_guild.trivia_game.services;

import com.the_build_guild.trivia_game.dtos.AllTimeLeaderboardDTO;
import com.the_build_guild.trivia_game.dtos.PersonalBestDTO;
import com.the_build_guild.trivia_game.models.Game;
import com.the_build_guild.trivia_game.models.User;
import com.the_build_guild.trivia_game.repositories.GameRepository;
import com.the_build_guild.trivia_game.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaderboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    public List<AllTimeLeaderboardDTO> getAllTimeLeaderboard() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .sorted((u1, u2) -> Integer.compare(u2.getScore(), u1.getScore()))
                .map(user -> new AllTimeLeaderboardDTO(user.getUsername(), user.getScore()))
                .collect(Collectors.toList());
    }

    public List<PersonalBestDTO> getPersonalLeaderboardByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return Collections.emptyList();
        }

        List<Game> games = gameRepository.findByUserIdContaining(user.getId());
        return games.stream()
            .sorted((g1, g2) -> Integer.compare(g2.getGameScore(), g1.getGameScore()))
            .map(game -> new PersonalBestDTO(
                    game.getGameScore(),
                    game.getDatePlayed(),
                    game.getTopic(),
                    game.getDifficulty()
            ))
            .collect(Collectors.toList());
    }
}