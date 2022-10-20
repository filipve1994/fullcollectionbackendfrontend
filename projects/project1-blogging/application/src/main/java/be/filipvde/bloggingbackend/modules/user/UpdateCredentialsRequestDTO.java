package be.filipvde.bloggingbackend.modules.user;

import jakarta.validation.constraints.NotNull;

public record UpdateCredentialsRequestDTO(
        @NotNull(message = "{user.oldPassword.notNull}") String oldPassword,
        @NotNull(message = "{user.newPassword.notNull}") String newPassword) {
}
