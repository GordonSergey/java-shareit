-- 1. Создание таблицы пользователей
CREATE TABLE IF NOT EXISTS public.users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

-- 2. Создание таблицы запросов
CREATE TABLE IF NOT EXISTS public.requests (
    id BIGSERIAL PRIMARY KEY,
    description TEXT NOT NULL,
    requester_id BIGINT NOT NULL,
    created TIMESTAMP NOT NULL,
    CONSTRAINT fk_requests_requester FOREIGN KEY (requester_id)
        REFERENCES public.users(id) ON DELETE CASCADE
);

-- 3. Создание таблицы предметов
CREATE TABLE IF NOT EXISTS public.items (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    is_available BOOLEAN NOT NULL,
    owner_id BIGINT NOT NULL,
    request_id BIGINT,
    CONSTRAINT fk_items_owner FOREIGN KEY (owner_id)
        REFERENCES public.users(id) ON DELETE CASCADE,
    CONSTRAINT fk_items_request FOREIGN KEY (request_id)
        REFERENCES public.requests(id) ON DELETE SET NULL
);

-- 4. Создание таблицы бронирований
CREATE TABLE IF NOT EXISTS public.bookings (
    id BIGSERIAL PRIMARY KEY,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    item_id BIGINT NOT NULL,
    booker_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_bookings_item FOREIGN KEY (item_id)
        REFERENCES public.items(id),
    CONSTRAINT fk_bookings_booker FOREIGN KEY (booker_id)
        REFERENCES public.users(id)
);

-- 5. Создание таблицы комментариев
CREATE TABLE IF NOT EXISTS public.comments (
    id BIGSERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    item_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_comments_item FOREIGN KEY (item_id)
        REFERENCES public.items(id),
    CONSTRAINT fk_comments_author FOREIGN KEY (author_id)
        REFERENCES public.users(id)
);

-- Заполнение тестовыми данными

-- Пользователи
INSERT INTO public.users (id, name, email) VALUES
    (1, 'Тестовый Пользователь', 'test@example.com'),
    (2, 'Тестовый Пользователь 2', 'test2@example.com'),
    (3, 'Иван Иванов', 'ivan@example.com'),
    (4, 'Петр Петров', 'petr@example.com');

-- Запросы на вещи
INSERT INTO public.requests (id, description, requester_id, created) VALUES
    (1, 'Нужна дрель для ремонта', 1, '2023-10-01 12:00:00'),
    (2, 'Ищу шуруповерт на выходные', 1, '2023-10-02 14:30:00'),
    (3, 'Требуется лестница для покраски потолка', 2, '2023-10-03 09:15:00');

-- Предметы
INSERT INTO public.items (id, name, description, is_available, owner_id, request_id) VALUES
    (1, 'Дрель', 'Аккумуляторная дрель', true, 3, null),
    (2, 'Шуруповерт', 'Сетевой шуруповерт', true, 4, null),
    (3, 'Лестница', 'Алюминиевая лестница 3 метра', true, 3, 1),
    (4, 'Молоток', 'Строительный молоток', false, 4, null);

-- Бронирования
INSERT INTO public.bookings (id, start_date, end_date, item_id, booker_id, status) VALUES
    (1, '2023-07-20 12:00:00', '2023-07-25 12:00:00', 1, 3, 'APPROVED'),
    (2, '2023-08-01 09:30:00', '2023-08-05 18:00:00', 2, 4, 'WAITING'),
    (3, '2023-09-10 10:00:00', '2023-09-15 10:00:00', 3, 1, 'REJECTED');

-- Комментарии
INSERT INTO public.comments (id, text, item_id, author_id, created_at) VALUES
    (1, 'Отличная дрель, всем рекомендую!', 1, 3, '2023-07-26 10:00:00'),
    (2, 'Шуруповерт немного слабоват', 2, 4, '2023-08-06 15:30:00'),
    (3, 'Лестница очень удобная и легкая', 3, 1, '2023-09-16 11:45:00');

-- Сдвигаем последовательности, чтобы избежать конфликта с автоинкрементом
    SELECT setval(pg_get_serial_sequence('users', 'id'), (SELECT MAX(id) FROM users));
    SELECT setval(pg_get_serial_sequence('requests', 'id'), (SELECT MAX(id) FROM requests));
    SELECT setval(pg_get_serial_sequence('items', 'id'), (SELECT MAX(id) FROM items));
    SELECT setval(pg_get_serial_sequence('bookings', 'id'), (SELECT MAX(id) FROM bookings));
    SELECT setval(pg_get_serial_sequence('comments', 'id'), (SELECT MAX(id) FROM comments));