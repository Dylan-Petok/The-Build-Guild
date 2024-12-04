package com.the_build_guild.trivia_game.integration_tests.services;

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
    public void testGetAllTimeLeaderboardWithNoUsers() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<AllTimeLeaderboardDTO> leaderboard = leaderboardService.getAllTimeLeaderboard();
        assertNotNull(leaderboard);
        assertEquals(0, leaderboard.size());
    }
}
