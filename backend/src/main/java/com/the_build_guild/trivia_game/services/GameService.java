package com.the_build_guild.trivia_game.services;

import com.the_build_guild.trivia_game.models.Game;
import com.the_build_guild.trivia_game.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }
}
