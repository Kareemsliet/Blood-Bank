CREATE TABLE user_has_roles
(
    role_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_user_has_roles PRIMARY KEY (role_id, user_id)
);

CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    email      VARCHAR(255) NOT NULL,
    name       VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    created_at datetime(6)           NULL,
    updated_at datetime(6)           NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE user_has_roles
    ADD CONSTRAINT fk_usehasrol_on_role_entity FOREIGN KEY (role_id) REFERENCES roles (id) on delete restrict;

ALTER TABLE user_has_roles
    ADD CONSTRAINT fk_usehasrol_on_user_entity FOREIGN KEY (user_id) REFERENCES users (id) on delete cascade;