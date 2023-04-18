package org.rangiffler.data;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "photos")
public class PhotoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;

    @Column(nullable = false)
    private String country;

    @Column(columnDefinition = "bytea")
    private byte[] photo;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private String username;

    public UUID getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public String getDescription() {
        return description;
    }

    public String getUsername() {
        return username;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
