package dev.ppondeu.java_starter.pictures.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import dev.ppondeu.java_starter.pictures.dtos.PictureCreateDTO;
import dev.ppondeu.java_starter.users.entities.User;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "pictures")
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name="file_path", nullable = false)
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at", nullable = false)
    private LocalDateTime updated_at;

    @Column(name="deleted_at")
    @ColumnDefault("null")
    private LocalDateTime deleted_at;

    @OneToMany(mappedBy = "picture")
    @JsonManagedReference
    private List<PicturePermission> permissions;

    public Picture() {
    }

    public Picture(PictureCreateDTO pictureCreateDTO, User user) {
        this.name = pictureCreateDTO.getName();
        this.filePath = pictureCreateDTO.getFilePath();
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updated_at = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFilePath() {
        return filePath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public LocalDateTime getDeleted_at() {
        return deleted_at;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setDeleted_at(LocalDateTime deleted_at) {
        this.deleted_at = deleted_at;
    }

    public List<PicturePermission> getPermissions() {
        return permissions;
    }
}
