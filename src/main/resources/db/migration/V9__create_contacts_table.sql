CREATE TABLE contacts
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    title      VARCHAR(255) NOT NULL,
    phone      VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    comment    VARCHAR(255) NULL,
    created_at datetime(6)           NULL,
    updated_at datetime(6)           NULL,
    CONSTRAINT pk_contacts PRIMARY KEY (id)
);