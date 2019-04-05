package com.github.denrion.mef_marketing.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
public class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Basic
    @Column(name = "last_modified", nullable = false)
    private LocalDateTime lastModified;

    @Version
    private Long version = 1L;


    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
    }

    @PreUpdate
    private void onUpdate() {
        this.lastModified = LocalDateTime.now();
    }


    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public Long getVersion() {
        return version;
    }
}
