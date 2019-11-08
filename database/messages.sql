create table messages
(
    id          bigserial    not null
        constraint messages_pkey
            primary key,
    content     varchar(255) not null,
    created_at  timestamp    not null,
    read        boolean      not null,
    receiver_id bigint
        constraint fkt05r0b6n0iis8u7dfna4xdh73
            references users,
    sender_id   bigint
        constraint fk4ui4nnwntodh6wjvck53dbk9m
            references users
);

alter table messages
    owner to postgres;

