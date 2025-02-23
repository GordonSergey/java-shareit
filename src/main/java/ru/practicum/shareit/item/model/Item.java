package ru.practicum.shareit.item.model;
import ru.practicum.shareit.request.ItemRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Item {
    private Integer id;
    private Integer owner;
    private String name;
    private String description;
    private Boolean available;
    private ItemRequest request;
}