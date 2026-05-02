CREATE SEQUENCE IF NOT EXISTS customer_seq
    START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS employee_seq
    START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS product_seq
    START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS orders_seq
    START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS order_items_seq
    START WITH 1 INCREMENT BY 50;




CREATE TABLE customer (
    id           BIGINT PRIMARY KEY DEFAULT nextval('customer_seq'),
    name         VARCHAR(255) NOT NULL,
    surname      VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL
);

CREATE UNIQUE INDEX uq_customer_email
    ON customer (email);

CREATE UNIQUE INDEX uq_customer_phone_number
    ON customer (phone_number);




CREATE TABLE employee (
    id       BIGINT PRIMARY KEY DEFAULT nextval('employee_seq'),
    name     VARCHAR(255) NOT NULL,
    surname  VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(50)  NOT NULL
);

CREATE UNIQUE INDEX uq_employee_email
    ON employee (email);




CREATE TABLE product (
    id          BIGINT         PRIMARY KEY DEFAULT nextval('product_seq'),
    name        VARCHAR(255)   NOT NULL,
    description VARCHAR(255)   NOT NULL,
    price       NUMERIC(10, 2) NOT NULL
);

CREATE UNIQUE INDEX uq_product_name
    ON product (name);




CREATE TABLE orders (
    id          BIGINT       PRIMARY KEY DEFAULT nextval('orders_seq'),
    status      VARCHAR(50)  NOT NULL,
    created_at  TIMESTAMPTZ  NOT NULL,
    items_count INTEGER      NOT NULL DEFAULT 0,
    customer_id BIGINT       REFERENCES customer (id) ON DELETE SET NULL
);

CREATE INDEX idx_orders_status
    ON orders (status);

CREATE INDEX idx_orders_created_at
    ON orders (created_at);

CREATE INDEX idx_orders_items_count
    ON orders (items_count);

CREATE INDEX idx_orders_customer_id
    ON orders (customer_id);




CREATE TABLE order_items (
    id         BIGINT         PRIMARY KEY DEFAULT nextval('order_items_seq'),
    order_id   BIGINT         NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    product_id BIGINT         NOT NULL REFERENCES product (id),
    quantity   INTEGER        NOT NULL,
    unit_price NUMERIC(10, 2) NOT NULL,
    CONSTRAINT uq_order_items_order_product UNIQUE (order_id, product_id)
);

CREATE INDEX idx_order_items_product_id
    ON order_items (product_id);