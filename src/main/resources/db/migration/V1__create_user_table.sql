CREATE TABLE IF NOT EXISTS `user` (
    id BINARY(16) NOT NULL,
    username VARCHAR(24) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password CHAR(60) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT username_unique UNIQUE (username),
    CONSTRAINT email_unique UNIQUE (email)
) ENGINE = InnoDB;