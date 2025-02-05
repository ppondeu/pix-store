package dev.ppondeu.java_starter.pictures.interfaces;

import dev.ppondeu.java_starter.pictures.entities.PicturePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IPicturePermissionRepository extends JpaRepository<PicturePermission, UUID> {
}
