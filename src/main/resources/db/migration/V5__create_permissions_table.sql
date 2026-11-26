CREATE TABLE permissions
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    name       VARCHAR(255) NOT NULL,
    authority  VARCHAR(255) NOT NULL,
    CONSTRAINT pk_permissions PRIMARY KEY (id)
);

ALTER TABLE permissions
    ADD CONSTRAINT uc_permissions_name UNIQUE (name);