package com.the_build_guild.trivia_game.unit_tests.services;

import com.the_build_guild.trivia_game.dtos.UserCreationDTO;
import com.the_build_guild.trivia_game.dtos.UserLoginDTO;
import com.the_build_guild.trivia_game.models.User;
import com.the_build_guild.trivia_game.repositories.UserRepository;
import com.the_build_guild.trivia_game.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceUnitTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser() {
        UserCreationDTO userCreationDTO = new UserCreationDTO("user1", "user1@example.com", "password123");
        User user = User.builder().username("user1").email("user1@example.com").passwordHash("password123").build();
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(userCreationDTO);
        assertNotNull(createdUser);
        assertEquals("user1", createdUser.getUsername());
        assertEquals("user1@example.com", createdUser.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testAuthenticateUserWithInvalidPassword() {
        UserLoginDTO userLoginDTO = new UserLoginDTO("user1", "wrongPassword");
        User user = User.builder().username("user1").passwordHash("password123").build();
        when(userRepository.findByUsername("user1")).thenReturn(user);

        User authenticatedUser = userService.authenticateUser(userLoginDTO);
        assertNull(authenticatedUser);
    }

    @Test
    public void testAuthenticateUserWithNonExistentUser() {
        UserLoginDTO userLoginDTO = new UserLoginDTO("nonExistentUser", "password123");
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(null);

        User authenticatedUser = userService.authenticateUser(userLoginDTO);
        assertNull(authenticatedUser);
    }
}
