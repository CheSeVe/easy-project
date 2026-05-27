INSERT INTO customer (id, name, surname, email, phone_number)
VALUES (1, 'Иван', 'Иванов', 'ivan@test.ru', '+79991234567');

INSERT INTO orders (id, status, created_at, customer_id)
VALUES (1, 'NEW', '2026-01-01 00:00:00+00', 1),
       (2, 'CANCELED', '2026-01-02 00:00:00+00', 1),
       (3, 'COMPLETED', '2026-01-03 00:00:00+00', 1),
       (4, 'NEW', '2026-01-04 00:00:00+00', 1),
       (5, 'CANCELED', '2026-01-05 00:00:00+00', 1),
       (6, 'COMPLETED', '2026-01-06 00:00:00+00', 1),
       (7, 'NEW', '2026-01-07 00:00:00+00', 1),
       (8, 'CANCELED', '2026-01-08 00:00:00+00', 1),
       (9, 'COMPLETED', '2026-01-09 00:00:00+00', 1),
       (10, 'NEW', '2026-01-10 00:00:00+00', 1),
       (11, 'CANCELED', '2026-01-11 00:00:00+00', 1);

SELECT setval('orders_seq', 50);