package ru.practicum.shareit.booking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.intf.Create;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings", schema = "public")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(groups = Create.class, message = "Time cannot be empty")
    @FutureOrPresent(groups = Create.class, message = "The booking start time must be in the future")
    @Column(name = "start_date")
    private LocalDateTime start;

    @NotNull(groups = Create.class, message = "Time cannot be empty")
    @Future(groups = Create.class, message = "The booking end time must be in the future")
    @Column(name = "end_date")
    private LocalDateTime end;

    @ManyToOne
    @NotNull(groups = Create.class, message = "The item cannot be empty")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @NotNull(groups = Create.class, message = "The booker cannot be empty")
    @JoinColumn(name = "booker_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User booker;

    @NotNull(groups = Create.class, message = "Status cannot be empty")
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus status;
}