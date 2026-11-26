CREATE TABLE states
(
    id         BIGINT AUTO_INCREMENT     NOT NULL,
    name       VARCHAR(255) NOT NULL,
    created_at datetime(6) DEFAULT CURRENT_TIMESTAMP(6) NULL,
    updated_at datetime(6)               NULL,
    CONSTRAINT pk_states PRIMARY KEY (id)
);

ALTER TABLE states
    ADD CONSTRAINT uc_states_name UNIQUE (name);