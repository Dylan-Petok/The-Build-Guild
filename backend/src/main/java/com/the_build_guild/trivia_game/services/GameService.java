package com.the_build_guild.trivia_game.services;

import com.the_build_guild.trivia_game.models.Game;
import com.the_build_guild.trivia_game.repositories.GameRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

        public List<Game> getGamesByUserId(String userId) {
        return gameRepository.findByUserIdContaining(userId);
    }

    
    public int getHighestScoreInGame(String userId) {
        List<Game> games = getGamesByUserId(userId);
        return games.stream()
                .mapToInt(Game::getScore) // Assuming Game has a getScore method
                .max()
                .orElse(0);
    }
}
