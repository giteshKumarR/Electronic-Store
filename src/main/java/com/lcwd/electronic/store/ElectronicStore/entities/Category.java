package com.lcwd.electronic.store.ElectronicStore.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "categories")
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

    // Manage OneToMany relationship with Product entity
    // fetch type Lazy means that when we fetch category, product are not fetched
    // eagerly they will only be fetched on demand
    @OneToMany(mappedBy = "category",
        cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Product> products;

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
