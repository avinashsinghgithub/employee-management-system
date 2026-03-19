CREATE TABLE employee
(
    id           binary(16) NOT NULL,
    first_name   VARCHAR(255),
    joining_date DATE,
    last_name    VARCHAR(255),
    address_id   binary(16),
    CONSTRAINT pk_employee PRIMARY KEY (id)
);
