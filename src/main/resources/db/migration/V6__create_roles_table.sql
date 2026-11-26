CREATE TABLE role_has_permissions
(
    permission_id BIGINT NOT NULL,
    role_id       BIGINT NOT NULL,
    CONSTRAINT pk_role_has_permissions PRIMARY KEY (permission_id,role_id)
);

CREATE TABLE roles
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    name       VARCHAR(255) NOT NULL,
    created_at datetime(6)           NULL,
    updated_at datetime(6)           NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

ALTER TABLE roles
    ADD CONSTRAINT uc_roles_name UNIQUE (name);

ALTER TABLE role_has_permissions
    ADD CONSTRAINT fk_rolhasper_on_permission_entity FOREIGN KEY (permission_id) REFERENCES permissions (id) on delete restrict;

ALTER TABLE role_has_permissions
    ADD CONSTRAINT fk_rolhasper_on_role_entity FOREIGN KEY (role_id) REFERENCES roles (id) on delete cascade;