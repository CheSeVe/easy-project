INSERT INTO customer (id, name, surname, email, phone_number)
VALUES (1, 'Иван', 'Иванов', 'ivan@test.ru', '+79991234567'),
       (2, 'Пётр', 'Петров', 'petr@test.ru', '+79997654321');

INSERT INTO orders (id, status, created_at, customer_id)
VALUES (1, 'NEW', '2026-01-01 00:00:00+00', 1),
       (2, 'NEW', '2026-01-01 00:00:00+00', 2);

INSERT INTO product (id, name, description, price)
VALUES (1, 'Ноутбук', 'Здоровый', '1000.00');

INSERT INTO order_items (id, order_id, product_id, quantity, unit_price)
VALUES (1, 1, 1, 1, '1000.00');