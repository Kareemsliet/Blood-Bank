CREATE TABLE cache
(
    `key`      VARCHAR(255) NOT NULL,
    value      VARCHAR(255) NOT NULL,
    expiration INT          NOT NULL,
    CONSTRAINT pk_cache PRIMARY KEY (`key`)
);