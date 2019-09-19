CREATE SEQUENCE message_seq START 100000;
CREATE SEQUENCE comment_seq START 100000;

create table usr
(
    id           varchar(255) not null
        constraint usr_pkey primary key,
    email        varchar(255),
    gender       varchar(255),
    last_visit   timestamp,
    locale       varchar(255),
    name         varchar(255),
    user_picture varchar(255)
);

create table message
(
    id               bigint not null
        constraint message_pkey
            primary key DEFAULT nextval('message_seq'),
    creation_date    timestamp,
    link             varchar(255),
    link_cover       varchar(400),
    link_description varchar(400),
    link_title       varchar(400),
    text             varchar(255),
    author           varchar(255)
        constraint fkqpqfp2vpxyq8q0ahkyyh7tl1u
            references usr
);
create table comment
(
    id         bigint       not null
        constraint comment_pkey
            primary key DEFAULT nextval('comment_seq'),
    text       varchar(255),
    user_id    varchar(255) not null
        constraint fkgcgdcgly6u49hf4g8y2di3g4p
            references usr,
    message_id bigint
        constraint fkatlrxw2dnvma9h401t2ql2ri8
            references message
);
/*
create index SPRING_SESSION_IX2
    on spring_session (EXPIRY_TIME);

create table spring_session
(
    PRIMARY_ID            char(36)     not null
        primary key,
    SESSION_ID            char(36)     not null,
    CREATION_TIME         bigint       not null,
    LAST_ACCESS_TIME      bigint       not null,
    MAX_INACTIVE_INTERVAL int          not null,
    EXPIRY_TIME           bigint       not null,
    PRINCIPAL_NAME        varchar(300) null,
    constraint SPRING_SESSION_IX1
        unique (SESSION_ID)
);

create index SPRING_SESSION_IX3
    on spring_session (PRINCIPAL_NAME);

create table spring_session_attributes
(
    SESSION_PRIMARY_ID char(36)     not null,
    ATTRIBUTE_NAME     varchar(200) not null,
    ATTRIBUTE_BYTES    bytea        not null,
    primary key (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
    constraint SPRING_SESSION_ATTRIBUTES_FK
        foreign key (SESSION_PRIMARY_ID) references spring_session (primary_id)
            on delete cascade
);

*/