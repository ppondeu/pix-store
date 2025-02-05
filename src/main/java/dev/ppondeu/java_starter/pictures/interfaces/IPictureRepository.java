package dev.ppondeu.java_starter.pictures.interfaces;

import dev.ppondeu.java_starter.pictures.entities.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IPictureRepository extends JpaRepository<Picture, UUID> {
}
