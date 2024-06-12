CREATE TABLE carts_products (
                                id SERIAL PRIMARY KEY,
                                cart_id BIGINT NOT NULL,
                                product_id BIGINT NOT NULL,
                                quantity BIGINT NOT NULL

);

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       first_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL
);

-- Добавление колонки `username` в таблицу `users`
ALTER TABLE users
    ADD COLUMN username VARCHAR(255) NOT NULL;

CREATE TABLE users_orders (
                              id SERIAL PRIMARY KEY,
                              user_id BIGINT NOT NULL,
                              order_id BIGINT NOT NULL
);

CREATE TABLE products (
                          id SERIAL PRIMARY KEY,
                          title VARCHAR(255) NOT NULL,
                          price DECIMAL(8, 2) NOT NULL,
                          description VARCHAR(255) NOT NULL,
                          picture VARCHAR(255) NOT NULL
);

CREATE TABLE carts (
                       id SERIAL PRIMARY KEY,
                       user_id BIGINT NOT NULL,
                       FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE orders (
                        id SERIAL PRIMARY KEY
);

ALTER TABLE carts_products
    ADD CONSTRAINT carts_products_cart_id_foreign
        FOREIGN KEY (cart_id) REFERENCES carts(id);

ALTER TABLE carts_products
    ADD CONSTRAINT carts_products_product_id_foreign
        FOREIGN KEY (product_id) REFERENCES products(id);

ALTER TABLE users_orders
    ADD CONSTRAINT users_orders_user_id_foreign
        FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE users_orders
    ADD CONSTRAINT users_orders_order_id_foreign
        FOREIGN KEY (order_id) REFERENCES orders(id);


-- Создание таблицы "categories"

CREATE TABLE categories (
                            id BIGINT PRIMARY KEY,
                            name VARCHAR(255),
                            image VARCHAR(255)
);

-- Добавление колонки "category_d" в таблицу "products"

ALTER TABLE products
    ADD COLUMN category_id BIGINT,

    ADD CONSTRAINT fk_category
        FOREIGN KEY (category_id)
        REFERENCES categories(id);
