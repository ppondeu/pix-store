package dev.ppondeu.java_starter.users.interfaces;

import dev.ppondeu.java_starter.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.refreshToken = :refresh_token")
    Optional<User> findUserByRefreshToken(String refreshToken);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.refreshToken = :refreshToken WHERE u.id = :userId")
    int updateRefreshToken(UUID userId, String refreshToken);

}
