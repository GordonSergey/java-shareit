package ru.practicum.shareit.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.intf.Create;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(groups = Create.class, message = "Name cannot be empty")
    @Column(name = "name")
    private String name;

    @NotBlank(groups = Create.class, message = "Email cannot be empty")
    @Email(groups = Create.class, message = "Email cannot be empty and must contain the '@' symbol")
    @Column(name = "email", unique = true)
    private String email;
}