create table posts
(
    id     serial       not null
        constraint posts_pk
            primary key,
    "user" varchar(255) not null,
    post   integer      not null
);

alter table posts
    owner to postgres;

