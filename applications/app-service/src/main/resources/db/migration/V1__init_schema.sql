CREATE TABLE franchises (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE products (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE branches (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL,
    id_franchise BIGINT NOT NULL,
    CONSTRAINT fk_franchise
        FOREIGN KEY(id_franchise)
        REFERENCES franchises(id)
);

CREATE TABLE branch_product (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    id_branch BIGINT NOT NULL,
    id_product BIGINT NOT NULL,
    stock BIGINT NOT NULL,

    CONSTRAINT fk_branch
        FOREIGN KEY(id_branch)
        REFERENCES branches(id),
    CONSTRAINT fk_product
        FOREIGN KEY(id_product)
        REFERENCES products(id),

    UNIQUE (id_branch, id_product)
);