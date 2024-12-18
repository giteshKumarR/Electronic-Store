package com.lcwd.electronic.store.ElectronicStore.repositories;
import com.lcwd.electronic.store.ElectronicStore.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
