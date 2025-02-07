package dev.ppondeu.java_starter.pictures.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dev.ppondeu.java_starter.users.entities.User;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "picture_permissions")
public class PicturePermission {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "picture_id")
    @JsonBackReference
    private Picture picture;

    @Enumerated(EnumType.STRING)
    private PicturePermissionType type;

    public PicturePermission() {
    }

    public PicturePermission(User user, Picture picture, PicturePermissionType type) {
        this.user = user;
        this.picture = picture;
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Picture getPicture() {
        return picture;
    }

    public PicturePermissionType getType() {
        return type;
    }

    public void setType(PicturePermissionType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PicturePermission{" +
                "id=" + id +
                ", user=" + user +
                ", picture=" + picture +
                ", type=" + type +
                '}';
    }
}
