package com.the_build_guild.trivia_game.integration;

import com.the_build_guild.trivia_game.models.User;
import com.the_build_guild.trivia_game.repositories.UserRepository;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Tag("integration")
class UserRepositoryIT {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void clean() { userRepository.deleteAll(); }

    private User newUser(String username, int score) {
        return User.builder()
                .username(username)
                .email(username + "@example.com")
                .passwordHash("not-relevant-for-repository-tests")
                .friends(new String[0])
                .gamesPlayedCount(0)
                .score(score)
                .build();
    }

    @Test
    void findByUsernameContainingIgnoreCase_matchesSubstring_ignoringCase() {
        userRepository.saveAll(List.of(
                newUser("alice", 10),
                newUser("aliceInWonderland", 20),
                newUser("malice", 30),
                newUser("bob", 40)
        ));

        List<User> results = userRepository.findByUsernameContainingIgnoreCase("ALI");

        assertThat(results)
                .extracting(User::getUsername)
                .containsExactlyInAnyOrder("alice", "aliceInWonderland", "malice");
    }


    @Test
    void findByUsernameContainingIgnoreCase_returnsEmptyList_whenNothingMatches(){
        userRepository.saveAll(List.of(
                newUser("george", 10),
                newUser("tom", 0),
                newUser("bob", 50),
                newUser("tim", 300)
        ));

        List<User> results = userRepository.findByUsernameContainingIgnoreCase("tog");

        assertThat(results).isEmpty();
    }

    @Test
    void save_thenFindById_roundTripsEveryField() {
        User jim = User.builder()
                .username("jim")
                .email("jimtest@gmail.com")
                .passwordHash("23oi5nifonsaoifn")
                .friends(new String[]{"jimtest1", "jimtest2"})
                .gamesPlayedCount(20)
                .score(300)
                .build();

        assertThat(jim.getId()).isNull();

        User saved = userRepository.save(jim);
        User found = userRepository.findById(saved.getId()).orElseThrow();

        assertThat(saved.getId()).isNotNull();
        assertThat(found.getUsername()).isEqualTo("jim");
        assertThat(found.getEmail()).isEqualTo("jimtest@gmail.com");
        assertThat(found.getPasswordHash()).isEqualTo("23oi5nifonsaoifn");
        assertThat(found.getGamesPlayedCount()).isEqualTo(20);
        assertThat(found.getScore()).isEqualTo(300);
        assertThat(found.getFriends()).containsExactly("jimtest1", "jimtest2");
    }

    @Test
    void findAllByOrderByScoreDesc_sortsByScoreDescending() {
        userRepository.saveAll(List.of(
                newUser("low", 20),
                newUser("high", 50),
                newUser("mid", 10)
        ));

        List<User> ranked = userRepository.findAllByOrderByScoreDesc();

        assertThat(ranked)
                .extracting(User::getUsername)
                .containsExactly("high", "low", "mid");
    }
}
