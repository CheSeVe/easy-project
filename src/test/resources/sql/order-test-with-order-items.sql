INSERT INTO product (id, name, description, price)
VALUES (1, 'Ноутбук', 'Маленький', '1000.00'),
       (2, 'PC', 'Огромный', '3000.00'),
       (3, 'Смартфон', 'Новый', '888.00');

INSERT INTO order_items (id, order_id, product_id, quantity, unit_price)
VALUES (1, 1, 1, 2, '1000.00'),
       (2, 2, 1, 3, '1000.00'),
       (3, 2, 3, 2, '888.00'),
       (4, 3, 3, 1, '888.00'),
       (5, 3, 2, 1, '3000.00');

