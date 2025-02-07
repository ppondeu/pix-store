package dev.ppondeu.java_starter.pictures.interfaces;

import dev.ppondeu.java_starter.pictures.entities.Picture;
import dev.ppondeu.java_starter.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IPictureRepository extends JpaRepository<Picture, UUID> {
    @Query("SELECT p FROM Picture p WHERE p.user.id = :userId")
    List<Picture> findPicturesByUser(UUID userId);
}
