package com.the_build_guild.trivia_game.unit_tests;

import com.the_build_guild.trivia_game.dtos.TriviaRequestDTO;
import com.the_build_guild.trivia_game.dtos.TriviaResponseDTO;
import com.the_build_guild.trivia_game.services.TriviaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@SpringBootTest
public class TriviaServiceUnitTests {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TriviaService triviaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetQuestionsWithNullResponse() {
        TriviaRequestDTO request = new TriviaRequestDTO("5", "easy", "Science");
        when(restTemplate.getForObject(anyString(), eq(TriviaResponseDTO.class))).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> triviaService.getQuestions(request));
        assertEquals("Failed to retrieve trivia questions", exception.getMessage());
    }
}
