package com.lcwd.electronic.store.ElectronicStore.repositories;

import com.lcwd.electronic.store.ElectronicStore.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    List<Category> findByCategoryTitleContaining(String keyword);
}
