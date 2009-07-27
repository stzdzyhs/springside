drop table USERS_ROLES;
drop table USERS;
drop table ROLES;

create table USERS (
ID integer primary key GENERATED BY DEFAULT as IDENTITY (START WITH 1000, INCREMENT BY 1),
LOGIN_NAME varchar(20) not null unique,
PASSWORD varchar(20),
NAME varchar(20),
EMAIL varchar(30)
);
create unique index USERS_LOGIN_NAME_INDEX on USERS(LOGIN_NAME);

create table ROLES (
ID integer primary key GENERATED BY DEFAULT as IDENTITY (START WITH 1000, INCREMENT BY 1),
NAME varchar(20) not null unique
);

create table USERS_ROLES (
USER_ID integer not null,
ROLE_ID integer not null,
FOREIGN KEY (ROLE_ID) references ROLES(ID),
FOREIGN KEY (USER_ID) references USERS(ID)
);