create table member
(
    id       number primary key,
    email    varchar2(100) not null unique,
    nickname varchar2(30)  not null,
    hashtag  number(4)     not null,
    status   varchar2(100) null
);

create table channel
(
    id          number primary key,
    name        varchar2(100) not null,
    thumbnail   clob          null,
    invite_code varchar2(100) not null
);

create table channel_member
(
    id         number primary key,
    channel_id number not null,
    member_id  number not null,
    role       varchar2(20) default 'guest' check (role in ('owner', 'manager', 'guest')),
    constraint channel_member_channel_id_fk foreign key (channel_id) references channel (id),
    constraint channel_member_member_id_fk foreign key (member_id) references member (id)
);

create table topic
(
    id         number primary key,
    title      varchar2(100) not null,
--     topic_group varchar2(100),
    channel_id number        not null, -- id of the channel that references topic
    constraint topic_channel_id_fk foreign key (channel_id) references channel (id)
);

create table refresh_token
(
    id          number primary key,
    email       varchar2(100) not null unique,
    token       varchar2(255) not null,
    expire_date date          not null
);

create table message
(
    id         number primary key,
    author_id  number         not null,
    content    varchar2(1000) not null,

    channel_id number         not null, -- id of the channel that references message
    topic_id   number         not null, -- id of the topic that references message

    created_at date           not null,
    updated_at date           not null,
    constraint message_author_id_fk foreign key (author_id) references member (id),
    constraint message_channel_id_fk foreign key (channel_id) references channel (id),
    constraint message_topic_id_fk foreign key (topic_id) references topic (id)
);


create table attachment
(
    id      number primary key,
    content blob
);


create sequence member_seq start with 10000;
create sequence channel_seq start with 10000;
create sequence channel_member_seq start with 10000;
create sequence topic_seq start with 10000;
create sequence message_seq start with 10000;
create sequence attachment_seq start with 10000;
create sequence token_seq start with 10000;
