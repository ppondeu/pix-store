package dev.ppondeu.java_starter.pictures.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import dev.ppondeu.java_starter.pictures.dtos.PictureCreateDTO;
import dev.ppondeu.java_starter.users.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;
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

    @Column(name="updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name="deletedAt")
    @ColumnDefault("null")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "picture", cascade = CascadeType.ALL)
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
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
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

    public LocalDateTime getupdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getdeletedAt() {
        return deletedAt;
    }

    public void setName(@NotBlank(message = "Picture Name is required") @Length(min = 1, max = 128, message = "Picture Name length must be between 1 and 128") String name) {
        this.name = name.strip();
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setdeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public List<PicturePermission> getPermissions() {
        return permissions;
    }

    @Override
    public String toString() {
        return "Picture{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", filePath='" + filePath + '\'' +
                ", user=" + user.getId() +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                '}';
    }
}
