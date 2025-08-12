package org.example.bookstoreapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Data
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Email cannot be null")
    @Column(unique = true, nullable = false)
    private String email;

    @NotNull(message = "Password cannot be null")
    @Column(nullable = false)
    private String password;

    @NotNull(message = "First name cannot be null")
    @Column(nullable = false)
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    @Column(nullable = false)
    private String lastName;
    
    private String shippingAddress;
}
