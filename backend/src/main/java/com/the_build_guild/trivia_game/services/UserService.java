package com.the_build_guild.trivia_game.services;

import com.the_build_guild.trivia_game.dtos.UserCreationDTO;
import com.the_build_guild.trivia_game.dtos.UserLoginDTO;
import com.the_build_guild.trivia_game.models.User;
import com.the_build_guild.trivia_game.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.the_build_guild.trivia_game.repositories.GameRepository;
import java.util.Date;



import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserService {
        private static final Logger logger = LoggerFactory.getLogger(UserService.class);
        private static final String SECRET_KEY = "your_secret_key";

    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

  

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(String id, User userDetails) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            user.setPasswordHash(userDetails.getPasswordHash());
            user.setFriends(userDetails.getFriends());
            user.setGamesPlayedCount(userDetails.getGamesPlayedCount());
            return userRepository.save(user);
        }
        return null;
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public User createUser(UserCreationDTO userCreationDTO) {
        User user = new User();
        user.setUsername(userCreationDTO.getUserName());
        user.setEmail(userCreationDTO.getEmailAddr());
        user.setPasswordHash(passwordEncoder.encode(userCreationDTO.getPassword())); // Ensure to hash the password before saving
        user.setScore(0);
        user.setGamesPlayedCount(0);
        user.setFriends(new String[0]);
        return userRepository.save(user);
    }

    
    // public User authenticateUser(UserLoginDTO userLoginDTO) {
    //     logger.info("Authenticating user: {}", userLoginDTO.getUserName());
    //     User user = userRepository.findByUsername(userLoginDTO.getUserName());
    //     if (user != null) {
    //         logger.info("Stored password hash: {}", user.getPasswordHash());
    //         logger.info("Raw password: {}", userLoginDTO.getPassword());
    //         if (passwordEncoder.matches(userLoginDTO.getPassword(), user.getPasswordHash())) {
    //             logger.info("Authentication successful for user: {}", userLoginDTO.getUserName());
    
    //             // Authenticate the user using Spring Security's AuthenticationManager
    //             Authentication authentication = authenticationManager.authenticate(
    //                     new UsernamePasswordAuthenticationToken(userLoginDTO.getUserName(), userLoginDTO.getPassword()));
    //             SecurityContextHolder.getContext().setAuthentication(authentication);
    
    //             return user;
    //         } else {
    //             logger.warn("Password mismatch for user: {}", userLoginDTO.getUserName());
    //         }
    //     } else {
    //         logger.warn("User not found: {}", userLoginDTO.getUserName());
    //     }
    //     logger.warn("Authentication failed for user: {}", userLoginDTO.getUserName());
    //     return null;
    // }

    public User authenticateUser(UserLoginDTO userLoginDTO, HttpServletRequest request) {
        logger.info("Authenticating user: {}", userLoginDTO.getUserName());

        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(userLoginDTO.getUserName(), userLoginDTO.getPassword())
        );

     // Set the authentication in the SecurityContext
    SecurityContext securityContext = SecurityContextHolder.getContext();
    securityContext.setAuthentication(authentication);

    // Ensure the SecurityContext is saved in the session
    HttpSession session = request.getSession(true);
    session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

    logger.info("Session ID for authenticated user {}: {}", userLoginDTO.getUserName(), session.getId());
    return userRepository.findByUsername(userLoginDTO.getUserName());
}
    

    public void updateScoreAndGamesPlayed(String userId, int scoreIncrement){
        logger.info("Updating score and games played for user ID: {}", userId);
        User user = userRepository.findById(userId).orElse(null);
        if (user != null){
            user.setScore(user.getScore() + scoreIncrement);
            user.setGamesPlayedCount(user.getGamesPlayedCount() + 1);
            userRepository.save(user);
            logger.info("User score and games played updated: {}", user);
        } else {
            logger.warn("User not found with ID: {}", userId);
        }
    }

    public int getGlobalRank(String userId) {
        List<User> users = userRepository.findAllByOrderByScoreDesc();
        for (int i = 0; i < users.size(); i++)  {
            if (users.get(i).getId().equals(userId)){
                return i + 1;
            }
        }
        return -1;
    }


    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public String getUserIdByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user != null ? user.getId() : null;
    }

    public List<User> searchUsers(String query, String requestingUsername) {
        List<User> users = userRepository.findByUsernameContainingIgnoreCase(query);
        return users.stream()
                    .filter(user -> !user.getUsername().equalsIgnoreCase(requestingUsername))
                    .collect(Collectors.toList());
    }
}