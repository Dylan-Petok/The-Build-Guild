package com.the_build_guild.trivia_game;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import com.the_build_guild.trivia_game.dtos.TriviaRequestDTO;
import com.the_build_guild.trivia_game.dtos.TriviaResponseDTO;
import com.the_build_guild.trivia_game.enums.TriviaStatusCodes;
import com.the_build_guild.trivia_game.services.TriviaService;

@SpringBootTest
class TriviaGameApplicationTests {

	@Mock
	private RestTemplate restTemplate;
	
	@InjectMocks
	private TriviaService triviaService;
	

	@Test
	public void testGetQuestions_Success(){

		TriviaRequestDTO request = new TriviaRequestDTO();
		request.setNumberOfQuestions("5");
		request.setCategory("Science");
		request.setDifficulty("easy");

		TriviaResponseDTO mockResponse = new TriviaResponseDTO();
		mockResponse.setResponseCode(TriviaStatusCodes.SUCCESS.getCode());

		when(restTemplate.getForObject(any(String.class), any(Class.class)))
			.thenReturn(mockResponse);

		TriviaResponseDTO response = triviaService.getQuestions(request);

		assertNotNull(response, "The response should not be null");
		assertNotNull(response.getResponseCode(), "The response code should not be null");
	}

}