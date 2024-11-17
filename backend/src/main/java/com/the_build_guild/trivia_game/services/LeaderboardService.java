package com.the_build_guild.trivia_game.services;

import com.the_build_guild.trivia_game.dtos.AllTimeLeaderboardDTO;
import com.the_build_guild.trivia_game.models.User;
import com.the_build_guild.trivia_game.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaderboardService {

    @Autowired
    private UserRepository userRepository;

    public List<AllTimeLeaderboardDTO> getAllTimeLeaderboard() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .sorted((u1, u2) -> Integer.compare(u2.getScore(), u1.getScore()))
                .map(user -> new AllTimeLeaderboardDTO(user.getUsername(), user.getScore()))
                .collect(Collectors.toList());
    }
}
