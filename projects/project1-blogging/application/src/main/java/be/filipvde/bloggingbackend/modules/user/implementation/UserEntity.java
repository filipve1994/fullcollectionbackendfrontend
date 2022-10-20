package be.filipvde.bloggingbackend.modules.user.implementation;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;

@Entity
@Table(name = "\"USER\"")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    UUID id;
    String email;
    String name;
    String password;
    @Convert(converter = ZoneIdConverter.class)
    ZoneId timezone;
    @CreatedDate
    private Instant createdDate;
    @LastModifiedDate
    private Instant lastModifiedDate;

    public static UserEntity of(String email, String name, String password, ZoneId timezone) {
        return new UserEntity(UUID.randomUUID(), email, name, password, timezone, null, null);
    }
}
