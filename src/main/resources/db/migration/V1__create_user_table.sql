CREATE TABLE IF NOT EXISTS `user` (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    username varchar(24) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password_hash CHAR(60) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT username_unique UNIQUE (username),
    CONSTRAINT email_unique UNIQUE (email)
) ENGINE = InnoDB;