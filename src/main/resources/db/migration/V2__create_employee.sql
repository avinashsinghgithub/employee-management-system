
CREATE TABLE employee
(
    id           UUID NOT NULL,
    first_name   VARCHAR(255),
    joining_date date,
    last_name    VARCHAR(255),
    address_id   UUID,
    CONSTRAINT pk_employee PRIMARY KEY (id)
);

