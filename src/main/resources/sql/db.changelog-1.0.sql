--liquibase formatted sql

--changeset sergio:1
create table if not exists social_network_user (
                                                   id bigserial primary key ,
                                                   first_name varchar(100),
                                                   last_name varchar(100),
                                                   login varchar(100),
                                                   password varchar(100),
                                                   created_date timestamp
);
create sequence if not exists social_network_user_sequence start 1000 increment 1;
--rollback drop table social_network_user;
--rollback drop sequence social_network_user_sequence;

--changeset sergio:2
create table if not exists message (
                                       id bigserial,
                                       content text,
                                       user_id bigint references social_network_user(id),
                                       created_date timestamp
);

create sequence if not exists message_sequence start 1000 increment 1;
--rollback drop table message;
--rollback drop sequence message_sequence;

--changeset sergio:3
create table if not exists friends (
                                       user_id bigint not null references social_network_user(id),
                                       friend_id bigint not null references social_network_user(id)
)
--rollback drop table friends;

--changeset sergio:4
create table image(
    id bigserial primary key,
    title varchar(100) not null,
    path text not null,
    user_id bigint references social_network_user(id),
    created_date timestamp not null
);
create sequence image_sequence start 1000 increment 1;