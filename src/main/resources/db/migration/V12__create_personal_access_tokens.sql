CREATE TABLE personal_access_tokens
(
    id             VARCHAR(255) NOT NULL,
    tokenable_type VARCHAR(255) NOT NULL,
    tokenable_id   BIGINT       NOT NULL,
    expires_at     datetime(6)  NOT NULL,
    created_at     datetime(6)  NULL,
    CONSTRAINT pk_personal_access_tokens PRIMARY KEY (id)
);