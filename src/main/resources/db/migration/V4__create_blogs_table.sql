CREATE TABLE blogs
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    title      VARCHAR(255) NOT NULL,
    image      VARCHAR(255) NULL,
    content    VARCHAR(255) NOT NULL,
    cat_id     BIGINT NOT NULL,
    created_at datetime(6)           NULL,
    updated_at datetime(6)           NULL,
    CONSTRAINT pk_blogs PRIMARY KEY (id)
);

ALTER TABLE blogs
    ADD CONSTRAINT uc_blogs_title UNIQUE (title);

ALTER TABLE blogs
    ADD CONSTRAINT FK_BLOGS_ON_CAT FOREIGN KEY (cat_id) REFERENCES blog_categories (id) on delete restrict;