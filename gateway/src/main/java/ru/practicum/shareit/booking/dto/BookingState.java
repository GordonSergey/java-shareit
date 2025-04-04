package ru.practicum.shareit.booking.dto;

import java.util.Optional;

/**
 * Состояния, в которых может находиться бронирование.
 */
public enum BookingState {
	/** Все бронирования без фильтрации по статусу */
	ALL,

	/** Активные бронирования, происходящие в данный момент */
	CURRENT,

	/** Бронирования, запланированные на будущее */
	FUTURE,

	/** Завершённые бронирования, срок которых истёк */
	PAST,

	/** Бронирования, отклонённые владельцем */
	REJECTED,

	/** Бронирования, которые ожидают одобрения */
	WAITING;

	/**
	 * Пытается преобразовать переданную строку в {@link BookingState}.
	 *
	 * @param stringState текстовое значение состояния
	 * @return {@link Optional} с найденным состоянием или {@link Optional#empty()}, если совпадений нет
	 */
	public static Optional<BookingState> from(String stringState) {
		for (BookingState state : values()) {
			if (state.name().equalsIgnoreCase(stringState)) {
				return Optional.of(state);
			}
		}
		return Optional.empty();
	}
}