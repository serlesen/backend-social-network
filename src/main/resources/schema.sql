create table if not exists social_network_user (
    id bigserial primary key ,
    first_name varchar(100),
    last_name varchar(100),
    login varchar(100),
    password varchar(100),
    created_date timestamp
);
create sequence if not exists social_network_user_sequence start 1000 increment 1;

create table if not exists message (
    id bigserial,
    content text,
    user_id bigint references social_network_user(id),
    created_date timestamp
);

create sequence if not exists message_sequence start 1000 increment 1;

create table if not exists friends (
    user_id bigint not null references social_network_user(id),
    friend_id bigint not null references social_network_user(id)
)