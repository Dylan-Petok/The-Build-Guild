package com.the_build_guild.trivia_game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.the_build_guild.trivia_game.dtos.UserCreationDTO;
import com.the_build_guild.trivia_game.dtos.UserLoginDTO;
import com.the_build_guild.trivia_game.models.User;
import com.the_build_guild.trivia_game.repositories.UserRepository;
import com.the_build_guild.trivia_game.services.UserService;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class UserServiceIntegrationTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testCreateUser() {

        UserCreationDTO userCreationDTO = new UserCreationDTO();
        userCreationDTO.setUserName("testuser");
        userCreationDTO.setEmailAddr("testuser@example.com");
        userCreationDTO.setPassword("hashed_password");

        User mockUser = new User();
        mockUser.setId("1");
        mockUser.setUsername(userCreationDTO.getUserName());
        mockUser.setEmail(userCreationDTO.getEmailAddr());
        mockUser.setPasswordHash(userCreationDTO.getPassword());

        when(userRepository.save(any(User.class))).thenReturn(mockUser);


        User createdUser = userService.createUser(userCreationDTO);


        assertNotNull(createdUser, "Created user should not be null");
        assertEquals("testuser", createdUser.getUsername());
        assertEquals("testuser@example.com", createdUser.getEmail());
        verify(userRepository).save(any(User.class));
    }


    @Test
    public void testGetUserById_UserExists() {

        String userId = "1";
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setUsername("testuser");

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));


        User foundUser = userService.getUserById(userId);


        assertNotNull(foundUser, "Found user should not be null");
        assertEquals("testuser", foundUser.getUsername());
    }

    @Test
    public void testGetUserById_UserNotExists() {

        String userId = "2";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());


        User foundUser = userService.getUserById(userId);


        assertNull(foundUser, "User should be null when not found");
    }


    @Test
    public void testAuthenticateUser_Success() {

        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUserName("testuser");
        userLoginDTO.setPassword("hashed_password");

        User mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setPasswordHash("hashed_password");

        when(userRepository.findByUsername(userLoginDTO.getUserName())).thenReturn(mockUser);


        User authenticatedUser = userService.authenticateUser(userLoginDTO);


        assertNotNull(authenticatedUser, "Authenticated user should not be null");
        assertEquals("testuser", authenticatedUser.getUsername());
    }

    @Test
    public void testAuthenticateUser_Failure() {

        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUserName("testuser");
        userLoginDTO.setPassword("incorrect_password");

        User mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setPasswordHash("hashed_password");

        when(userRepository.findByUsername(userLoginDTO.getUserName())).thenReturn(mockUser);


        User authenticatedUser = userService.authenticateUser(userLoginDTO);


        assertNull(authenticatedUser, "User should be null for failed authentication");
    }
}
