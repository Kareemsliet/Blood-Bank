CREATE TABLE blog_client
(
    blog_id   BIGINT NOT NULL,
    client_id BIGINT NOT NULL,
    CONSTRAINT pk_blog_client PRIMARY KEY (blog_id, client_id)
);

CREATE TABLE clients
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    name        VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    city_id     BIGINT       NOT NULL,
    birth_date  date         NOT NULL,
    blood_type  VARCHAR(5) NULL,
    created_at  datetime(6)           NULL,
    updated_at  datetime(6)           NULL,
    CONSTRAINT pk_clients PRIMARY KEY (id)
);

ALTER TABLE clients
    ADD CONSTRAINT uc_clients_email UNIQUE (email);

ALTER TABLE clients
    ADD CONSTRAINT FK_CLIENTS_ON_CITY FOREIGN KEY (city_id) REFERENCES cities (id) on delete restrict;

ALTER TABLE blog_client
    ADD CONSTRAINT fk_blog_client_on_blog_entity FOREIGN KEY (blog_id) REFERENCES blogs (id) on delete restrict;

ALTER TABLE blog_client
    ADD CONSTRAINT fk_blog_client_on_client_entity FOREIGN KEY (client_id) REFERENCES clients (id) on delete cascade;