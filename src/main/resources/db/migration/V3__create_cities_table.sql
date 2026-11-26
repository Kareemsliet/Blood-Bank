CREATE TABLE cities
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    name       VARCHAR(255) NOT NULL,
    state_id   BIGINT NOT NULL,
    created_at datetime(6)           NULL,
    updated_at datetime(6)           NULL,
    CONSTRAINT pk_cities PRIMARY KEY (id)
);

ALTER TABLE cities
    ADD CONSTRAINT FK_CITIES_ON_STATE FOREIGN KEY (state_id) REFERENCES states (id) on delete cascade;