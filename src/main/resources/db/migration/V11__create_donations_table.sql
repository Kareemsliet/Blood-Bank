CREATE TABLE donations
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    client_id  BIGINT NULL,
    name       VARCHAR(255) NOT NULL,
    age        INT          NOT NULL,
    phone      VARCHAR(255) NOT NULL,
    city_id    BIGINT NULL,
    blood_type VARCHAR(5) NULL,
    comment    VARCHAR(255) NULL,
    address    VARCHAR(255) NOT NULL,
    amount     INT          NOT NULL,
    latitude   DECIMAL NULL,
    longitude  DECIMAL NULL,
    created_at datetime(6)           NULL,
    updated_at datetime(6)           NULL,
    CONSTRAINT pk_donations PRIMARY KEY (id)
);

ALTER TABLE donations
    ADD CONSTRAINT FK_DONATIONS_ON_CITY FOREIGN KEY (city_id) REFERENCES cities (id) on delete restrict;

ALTER TABLE donations
    ADD CONSTRAINT FK_DONATIONS_ON_CLIENT FOREIGN KEY (client_id) REFERENCES clients (id) on delete cascade;