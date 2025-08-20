package org.example.bookstoreapp.repository.shoppingCart;

import jakarta.validation.constraints.NotNull;
import org.example.bookstoreapp.model.ShoppingCart;
import org.example.bookstoreapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    @Query("SELECT sc FROM ShoppingCart sc LEFT JOIN FETCH sc.cartItems WHERE sc.user.id = :userId")
    Optional<ShoppingCart> findByUser(@NotNull @Param("userId") Long userId);
}
