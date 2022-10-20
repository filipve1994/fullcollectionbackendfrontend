package be.filipvde.bloggingbackend.modules.user;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class UserNotFoundException extends RuntimeException {
    private final UUID id;

    @Override
    public String getMessage() {
        return "User with ID '" + id + "' was not found";
    }
}

