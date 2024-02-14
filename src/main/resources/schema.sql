create table member (
    id number primary key,
    email varchar2(100) not null unique,
    nickname varchar2(30) not null,
    hashtag number(4) not null
);

create table channel (
    id number primary key,
    channel_name varchar2(10) not null,
    channel_thumbnail clob null
);

create table channel_member (
    id number primary key,
    channel_id number not null,
    member_id number not null
);

create table topic (
    id number primary key,
    title varchar2(100) not null,

    channel_id number not null -- id of the channel that references topic
);

create table message (
    id number primary key,
    author_id number not null,
    content varchar2(1000) not null,
    send_date date not null,

    channel_id number not null, -- id of the channel that references message
    topic_id number not null -- id of the topic that references message
);

create sequence member_seq;
create sequence channel_seq start with 1000 increment by 7;
create sequence channel_member_seq;
create sequence topic_seq start with 10000 increment by 13;
create sequence message_seq start with 100 increment by 17;

