package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.intf.Create;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Long id;

    @NotBlank(groups = Create.class, message = "Name cannot be empty")
    private String name;

    @NotBlank(groups = Create.class, message = "Email cannot be empty")
    @Email(groups = Create.class, message = "Email cannot be empty and must contain the '@' symbol")
    private String email;
}