package ma.tr.livr_coding.repository;

import ma.tr.livr_coding.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.rmi.server.UID;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, UID> {
    Optional<User> findByEmail(String username);
    boolean existsByEmail(String email);
}
