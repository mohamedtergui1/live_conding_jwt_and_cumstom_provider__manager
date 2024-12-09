package ma.tr.livr_coding.repository;

import ma.tr.livr_coding.domain.entity.RefreshToken;
import ma.tr.livr_coding.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);
}