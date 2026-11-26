CREATE TABLE blog_categories
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    name       VARCHAR(255) NOT NULL,
    created_at datetime(6)           NULL,
    updated_at datetime(6)           NULL,
    CONSTRAINT pk_blog_categories PRIMARY KEY (id)
);

ALTER TABLE blog_categories
    ADD CONSTRAINT uc_blog_categories_name UNIQUE (name);