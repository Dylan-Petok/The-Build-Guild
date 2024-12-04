package com.the_build_guild.trivia_game.unit_tests.services;

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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class GameServiceUnitTest {

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
        Game game = Game.builder().userId("user1").gameScore(100).build();
        when(gameRepository.save(game)).thenReturn(game);

        Game savedGame = gameService.saveGame(game);
        assertNotNull(savedGame);
        assertEquals("user1", savedGame.getUserId());
        assertEquals(100, savedGame.getScore());
        verify(gameRepository, times(1)).save(game);
    }

    @Test
    public void testSaveGameWithNull() {
        assertThrows(NullPointerException.class, () -> gameService.saveGame(null));
    }

    @Test
    public void testGetGamesByUserIdWithNoGames() {
        when(gameRepository.findByUserIdContaining("user1")).thenReturn(Collections.emptyList());

        List<Game> games = gameService.getGamesByUserId("user1");
        assertNotNull(games);
        assertEquals(0, games.size());
        verify(gameRepository, times(1)).findByUserIdContaining("user1");
    }

    @Test
    public void testGetGamesByUserIdWithNullUserId() {
        when(gameRepository.findByUserIdContaining(null)).thenReturn(Collections.emptyList());

        List<Game> games = gameService.getGamesByUserId(null);
        assertNotNull(games);
        assertEquals(0, games.size());
        verify(gameRepository, times(1)).findByUserIdContaining(null);
    }

    @Test
    public void testGetHighestScoreInGameWithNoGames() {
        when(gameRepository.findByUserIdContaining("user1")).thenReturn(Collections.emptyList());

        int highestScore = gameService.getHighestScoreInGame("user1");
        assertEquals(0, highestScore);
    }

    @Test
    public void testGetHighestScoreInGameWithNegativeScores() {
        Game game1 = Game.builder().userId("user1").gameScore(-50).build();
        Game game2 = Game.builder().userId("user1").gameScore(-10).build();
        when(gameRepository.findByUserIdContaining("user1")).thenReturn(Arrays.asList(game1, game2));

        int highestScore = gameService.getHighestScoreInGame("user1");
        assertEquals(-10, highestScore);
    }
}