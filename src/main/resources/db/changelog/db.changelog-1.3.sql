
--liquibase formatted sql

--changeset burgasvv:1
create table if not exists task_employee (
    task_id bigint references task(id) on delete cascade on update cascade ,
    employee_id bigint references employee(id) on delete cascade on update cascade ,
    primary key (task_id, employee_id)
);

--changeset burgasvv:2
begin ;
insert into task_employee(task_id, employee_id) values (1, 1);
insert into task_employee(task_id, employee_id) values (1, 3);
insert into task_employee(task_id, employee_id) values (1, 5);
insert into task_employee(task_id, employee_id) values (2, 2);
insert into task_employee(task_id, employee_id) values (2, 4);
insert into task_employee(task_id, employee_id) values (2, 5);
insert into task_employee(task_id, employee_id) values (3, 3);
insert into task_employee(task_id, employee_id) values (3, 5);
insert into task_employee(task_id, employee_id) values (4, 1);
insert into task_employee(task_id, employee_id) values (4, 2);
insert into task_employee(task_id, employee_id) values (4, 4);
insert into task_employee(task_id, employee_id) values (5, 2);
insert into task_employee(task_id, employee_id) values (5, 3);
insert into task_employee(task_id, employee_id) values (5, 4);
insert into task_employee(task_id, employee_id) values (5, 5);
insert into task_employee(task_id, employee_id) values (6, 1);
insert into task_employee(task_id, employee_id) values (6, 3);
insert into task_employee(task_id, employee_id) values (7, 2);
insert into task_employee(task_id, employee_id) values (7, 4);
insert into task_employee(task_id, employee_id) values (7, 5);
commit ;