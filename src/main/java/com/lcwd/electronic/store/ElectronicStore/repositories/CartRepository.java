package com.lcwd.electronic.store.ElectronicStore.repositories;

import com.lcwd.electronic.store.ElectronicStore.entities.Cart;
import com.lcwd.electronic.store.ElectronicStore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findByUser(User user); // This method will be useful for lookup if we want to perform operation
                                          // like deletion etc, USE THIS ONLY...
    Optional<Cart> findByUser_UserId(String userId); // This method is only good for lookups but if the object returned from this method is used for deletion then somehow it doesn't work so while implementing the
                                                     // remove logic of any Kind please avoid this

    boolean existsByUser_UserId(String userId);

}
