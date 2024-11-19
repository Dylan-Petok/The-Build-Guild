package com.the_build_guild.trivia_game.integration_tests;

import com.the_build_guild.trivia_game.dtos.AllTimeLeaderboardDTO;
import com.the_build_guild.trivia_game.models.User;
import com.the_build_guild.trivia_game.repositories.UserRepository;
import com.the_build_guild.trivia_game.services.LeaderboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class LeaderboardServiceIntegrationTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LeaderboardService leaderboardService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllTimeLeaderboard() {
        User user1 = User.builder()
                .username("user1")
                .score(100)
                .build();
        User user2 = User.builder()
                .username("user2")
                .score(200)
                .build();
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<AllTimeLeaderboardDTO> leaderboard = leaderboardService.getAllTimeLeaderboard();
        assertEquals(2, leaderboard.size());
        assertEquals("user2", leaderboard.get(0).getUsername());
        assertEquals(200, leaderboard.get(0).getScore());
        assertEquals("user1", leaderboard.get(1).getUsername());
        assertEquals(100, leaderboard.get(1).getScore());
    }

        @Test
    public void testGetAllTimeLeaderboardWithTies() {
        User user1 = User.builder().username("user1").score(200).build();
        User user2 = User.builder().username("user2").score(200).build();
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<AllTimeLeaderboardDTO> leaderboard = leaderboardService.getAllTimeLeaderboard();
        assertNotNull(leaderboard);
        assertEquals(2, leaderboard.size());
        assertEquals(200, leaderboard.get(0).getScore());
        assertEquals(200, leaderboard.get(1).getScore());
    }

    @Test
    public void testGetAllTimeLeaderboardWithNoUsers() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<AllTimeLeaderboardDTO> leaderboard = leaderboardService.getAllTimeLeaderboard();
        assertNotNull(leaderboard);
        assertEquals(0, leaderboard.size());
    }
}
