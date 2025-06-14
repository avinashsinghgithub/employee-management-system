CREATE TABLE address
(
    id        UUID NOT NULL,
    street    VARCHAR(255),
    apt_name  INT  NOT NULL,
    pin_code  INT  NOT NULL,
    flat_name VARCHAR(255),
    CONSTRAINT pk_address PRIMARY KEY (id)
);