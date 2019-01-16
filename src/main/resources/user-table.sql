create table usr
(
  id           varchar(255) not null
    constraint usr_pkey
      primary key,
  email        varchar(255),
  gender       varchar(255),
  last_visit   timestamp,
  locale       varchar(255),
  name         varchar(255),
  user_picture varchar(255)
);

alter table usr
  owner to postgres;