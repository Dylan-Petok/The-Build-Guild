package com.the_build_guild.trivia_game.unit_tests.services;

import com.the_build_guild.trivia_game.dtos.AllTimeLeaderboardDTO;
import com.the_build_guild.trivia_game.repositories.UserRepository;
import com.the_build_guild.trivia_game.services.LeaderboardService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LeaderboardServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LeaderboardService leaderboardService;

    @Test
    public void testGetAllTimeLeaderboardWithNoUsers() {
        when(userRepository.findAllByOrderByScoreDesc()).thenReturn(Collections.emptyList());

        List<AllTimeLeaderboardDTO> leaderboard = leaderboardService.getAllTimeLeaderboard();
        assertNotNull(leaderboard);
        assertEquals(0, leaderboard.size());
    }
}
