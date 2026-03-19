CREATE TABLE address
(
    id        binary(16) NOT NULL,
    street    VARCHAR(255),
    apt_name  VARCHAR(255)  NOT NULL,
    pin_code  VARCHAR(255)  NOT NULL,
    flatName  VARCHAR(255),
    CONSTRAINT pk_address PRIMARY KEY (id)
);