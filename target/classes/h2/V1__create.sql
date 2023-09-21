CREATE TABLE income(id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
 amount decimal NULL,
-- "date" date NULL,
 income_date date NULL,
 income_year int null,
 income_month int null,
 description VARCHAR(255) NULL,
 operation_id BIGINT NULL,
 CONSTRAINT pk_income PRIMARY KEY (id));

insert into income(id, amount, income_date, income_year, income_month, description) values (default, 10.0, now(),1, 2, 'descr');

insert into income(id, amount, income_date, income_year, income_month, description) values (default, 1.5, now(),1, 2, 'descr2');