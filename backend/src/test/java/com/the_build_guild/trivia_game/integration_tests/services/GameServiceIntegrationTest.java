package com.the_build_guild.trivia_game.integration_tests.services;
import com.the_build_guild.trivia_game.models.Game;
import com.the_build_guild.trivia_game.repositories.GameRepository;
import com.the_build_guild.trivia_game.services.GameService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GameServiceIntegrationTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveGame() {
        Game game = Game.builder()
                .userId("user1")
                .gameScore(100)
                .build();
        when(gameRepository.save(game)).thenReturn(game);

        Game savedGame = gameService.saveGame(game);
        assertEquals("user1", savedGame.getUserId());
        assertEquals(100, savedGame.getScore());
    }

    @Test
    public void testGetGamesByUserId() {
        Game game1 = Game.builder()
                .userId("user1")
                .gameScore(100)
                .build();
        Game game2 = Game.builder()
                .userId("user1")
                .gameScore(200)
                .build();
        when(gameRepository.findByUserIdContaining("user1")).thenReturn(Arrays.asList(game1, game2));

        List<Game> games = gameService.getGamesByUserId("user1");
        assertEquals(2, games.size());
    }

    @Test
    public void testGetHighestScoreInGame() {
        Game game1 = Game.builder()
                .userId("user1")
                .gameScore(100)
                .build();
        Game game2 = Game.builder()
                .userId("user1")
                .gameScore(200)
                .build();
        when(gameRepository.findByUserIdContaining("user1")).thenReturn(Arrays.asList(game1, game2));

        int highestScore = gameService.getHighestScoreInGame("user1");
        assertEquals(200, highestScore);
    }
}