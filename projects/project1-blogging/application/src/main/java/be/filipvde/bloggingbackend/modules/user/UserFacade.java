package be.filipvde.bloggingbackend.modules.user;

import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.UUID;

public interface UserFacade {
    UserInfoDTO create(@Valid CreateUserRequestDTO request);

    UserInfoDTO findById(UUID id);

    @Transactional
    UserInfoDTO update(UUID id, @Valid UpdateUserRequestDTO request);

    @Transactional
    UserInfoDTO updateCredentials(UUID id, @Valid UpdateCredentialsRequestDTO request);

    boolean existsById(UUID id);

    boolean existsByEmail(String email);

    Collection<String> findAvailableTimezones();
}
