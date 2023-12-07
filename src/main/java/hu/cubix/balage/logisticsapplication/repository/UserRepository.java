package hu.cubix.balage.logisticsapplication.repository;

import hu.cubix.balage.logisticsapplication.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
}