-- create database "rangiffler-photo" with owner postgres;

create extension if not exists "uuid-ossp";

create table if not exists photos
(
    id          UUID unique not null default uuid_generate_v1() primary key,
    country     varchar(3),
    photo       bytea,
    description varchar(500),
    username    varchar(50) not null
);

alter table photos
    owner to postgres;
