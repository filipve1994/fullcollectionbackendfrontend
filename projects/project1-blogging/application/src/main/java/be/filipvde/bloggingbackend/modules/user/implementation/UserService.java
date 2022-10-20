package be.filipvde.bloggingbackend.modules.user.implementation;

import be.filipvde.bloggingbackend.modules.user.CreateUserRequestDTO;
import be.filipvde.bloggingbackend.modules.user.InvalidUserException;
import be.filipvde.bloggingbackend.modules.user.UpdateCredentialsRequestDTO;
import be.filipvde.bloggingbackend.modules.user.UpdateUserRequestDTO;
import be.filipvde.bloggingbackend.modules.user.UserAuthentication;
import be.filipvde.bloggingbackend.modules.user.UserFacade;
import be.filipvde.bloggingbackend.modules.user.UserInfoDTO;
import be.filipvde.bloggingbackend.modules.user.UserNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.ZoneId;
import java.util.Collection;
import java.util.UUID;

@Service
@Validated
@RequiredArgsConstructor
class UserService implements UserFacade, UserDetailsService {
    private final UserEntityRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserInfoDTO create(@Valid CreateUserRequestDTO request) {
        validateUniqueEmailAddress(request.email());
        String hash = passwordEncoder.encode(request.password());
        UserEntity user = UserEntity.of(request.email(), request.name(), hash, request.timezone());
        UserEntity savedUser = repository.save(user);
        return UserInfoDTO.ofEntity(savedUser);
    }

    @Override
    public UserInfoDTO findById(UUID id) {
        return repository
                .findById(id)
                .map(UserInfoDTO::ofEntity)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    @Transactional
    public UserInfoDTO update(UUID id, @Valid UpdateUserRequestDTO request) {
        UserEntity entity = findByIdOrThrowException(id);
        entity.setName(request.name());
        entity.setTimezone(request.timezone());
        return UserInfoDTO.ofEntity(entity);
    }

    @Override
    @Transactional
    public UserInfoDTO updateCredentials(UUID id, @Valid UpdateCredentialsRequestDTO request) {
        UserEntity entity = findByIdOrThrowException(id);
        validatePasswordsMatch(entity, request.oldPassword());
        entity.setPassword(passwordEncoder.encode(request.newPassword()));
        return UserInfoDTO.ofEntity(entity);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository
                .findByEmail(username)
                .map(UserAuthentication::ofEntity)
                .orElseThrow(() -> new UsernameNotFoundException("There is no such user"));
    }

    @Override
    public Collection<String> findAvailableTimezones() {
        return ZoneId
                .getAvailableZoneIds()
                .stream()
                .sorted()
                .toList();
    }

    private void validateUniqueEmailAddress(String email) {
        if (repository.existsByEmail(email)) {
            throw new InvalidUserException("There is already a user with this e-mail address");
        }
    }

    private UserEntity findByIdOrThrowException(UUID id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    private void validatePasswordsMatch(UserEntity entity, String oldPassword) {
        if (!passwordEncoder.matches(oldPassword, entity.getPassword())) {
            throw new InvalidUserException("The password is not correct");
        }
    }
}
