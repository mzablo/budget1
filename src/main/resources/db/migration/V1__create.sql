CREATE TABLE income(id BIGINT identity NOT NULL,
 amount decimal NULL,
 date date NULL,
 year int null,
 month int null,
 description VARCHAR(255) NULL,
 operation_id BIGINT NULL,
 CONSTRAINT pk_income PRIMARY KEY (id));

insert into income(id, amount, date, year, month, description) values (default, 10.0, now(),1, 2, 'descr');

insert into income(id, amount, date, year, month, description) values (default, 1.5, now(),1, 2, 'descr2');