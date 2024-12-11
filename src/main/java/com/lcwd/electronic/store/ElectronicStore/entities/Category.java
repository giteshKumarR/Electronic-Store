package com.lcwd.electronic.store.ElectronicStore.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class Category {
    @Id
    @Column(name = "category_id")
    private String categoryId;
    @Column(name="category_title", length=40, nullable = false)
    private String categoryTitle;
    @Column(name = "category_desc", length=50)
    private String categoryDescription;
    @Column(name = "category_image")
    private String categoryCoverImage;

    @Column(name = "category_created_on")
    private LocalDateTime createdOn;

    @Column(name = "category_updated_on")
    private LocalDateTime updatedOn;

    @PrePersist
    public void onPrePersist() {
        // This will run only when the entity will be first created
        this.createdOn = LocalDateTime.now();
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedOn = LocalDateTime.now();
    }
}
