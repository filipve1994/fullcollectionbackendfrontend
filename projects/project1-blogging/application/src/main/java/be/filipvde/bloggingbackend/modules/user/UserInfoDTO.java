package be.filipvde.bloggingbackend.modules.user;

import be.filipvde.bloggingbackend.modules.user.implementation.UserEntity;

import java.time.ZoneId;
import java.util.UUID;

public record UserInfoDTO(UUID id, String email, String name, ZoneId timezone) {
    public static UserInfoDTO ofEntity(UserEntity entity) {
        return new UserInfoDTO(entity.getId(), entity.getEmail(), entity.getName(), entity.getTimezone());
    }
}

