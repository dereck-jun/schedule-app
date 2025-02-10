create table if not exists users
(
    user_id           bigint auto_increment
        primary key,
    email             varchar(255) not null,
    username          varchar(12)  not null,
    password          varchar(255) not null,
    created_date_time datetime(6)  not null,
    updated_date_time datetime(6)  not null,
    deleted_date_time datetime(6)  null,
    constraint uk_users_email
        unique (email),
    constraint uk_users_username
        unique (username)
);

create table if not exists schedules
(
    schedule_id       bigint auto_increment
        primary key,
    title             varchar(60)  not null,
    body              varchar(255) not null,
    created_date_time datetime(6)  not null,
    updated_date_time datetime(6)  not null,
    deleted_date_time datetime(6)  null,
    user_id           bigint       not null,
    constraint fk_schedules_users
        foreign key (user_id) references users (user_id)
);

create table if not exists comments
(
    comment_id        bigint auto_increment
        primary key,
    content           varchar(255) not null,
    created_date_time datetime(6)  not null,
    updated_date_time datetime(6)  not null,
    deleted_date_time datetime(6)  null,
    user_id           bigint       not null,
    schedule_id       bigint       not null,
    constraint fk_comments_users
        foreign key (user_id) references users (user_id),
    constraint fk_comments_schedules
        foreign key (schedule_id) references schedules (schedule_id)
);