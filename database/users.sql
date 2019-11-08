create table users
(
    id         bigserial    not null
        constraint users_pkey
            primary key,
    bot        boolean      not null,
    created_at timestamp    not null,
    email      varchar(255) not null
        constraint uk_6dotkott2kjsp8vw4d0m25fb7
            unique,
    first_name varchar(64)  not null,
    last_name  varchar(64)  not null,
    password   varchar(255),
    expired_at timestamp,
    token      varchar(255)
        constraint uk_af44yue4uh61eqwe6jjyqwvla
            unique
);

alter table users
    owner to postgres;

INSERT INTO public.users (id, bot, created_at, email, first_name, last_name, password, expired_at, token) VALUES (6, true, '2019-11-07 23:48:31.190000', 'bot@admin.com', 'Bot', 'Father', '{bcrypt}$2a$04$YjRmw6gg/Z5tFJKn.fkwx.E/H2NxBl8HOLaatIQ8VhCVtWXvTdou2', '2019-11-15 00:49:41.879000', 'a8743ff9-d707-4bc2-9344-33c6cb5a25a1');