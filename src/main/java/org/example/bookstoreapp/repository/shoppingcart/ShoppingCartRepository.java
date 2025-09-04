package org.example.bookstoreapp.repository.shoppingcart;

import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import org.example.bookstoreapp.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    @Query("SELECT sc FROM ShoppingCart sc LEFT JOIN FETCH sc.cartItems WHERE sc.user.id = :userId")
    Optional<ShoppingCart> findByUserId(@NotNull @Param("userId") Long userId);
}
