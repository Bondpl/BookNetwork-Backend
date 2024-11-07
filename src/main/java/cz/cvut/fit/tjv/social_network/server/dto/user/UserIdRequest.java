package cz.cvut.fit.tjv.social_network.server.dto.user;

import lombok.Data;

import java.util.UUID;

@Data
public class UserIdRequest {

    private UUID uuid;
}
