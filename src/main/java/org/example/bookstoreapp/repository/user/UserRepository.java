package org.example.bookstoreapp.repository.user;

import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import org.example.bookstoreapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email = :email")
    Optional<User> findByEmail(
            @NotNull(message = "Email cannot be null")
            @Param("email") String email);
}
