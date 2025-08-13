package org.example.bookstoreapp.repository.user;

import jakarta.validation.constraints.NotNull;
import org.example.bookstoreapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByEmail(@NotNull(message = "Email cannot be null") String email);
}
