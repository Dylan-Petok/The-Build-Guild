package com.the_build_guild.trivia_game.models;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "friend_game_stats")
@EqualsAndHashCode(callSuper = false)
public class FriendGameStats {
    @Id
    private String friendShipId;
    private String userId1;
    private String userId2;
    private Integer winCountUser1;
    private Integer winCountUser2;
}
