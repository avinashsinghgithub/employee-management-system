CREATE TABLE department
(
    id        UUID NOT NULL,
    department_name VARCHAR(255),
    description VARCHAR(255),
    CONSTRAINT pk_department PRIMARY KEY (id)
);