package com.zeromus.eventmanager.model.entity;

import jakarta.persistence.*;
import com.zeromus.eventmanager.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "em_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    private String username;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String email;

    private String password;
}
