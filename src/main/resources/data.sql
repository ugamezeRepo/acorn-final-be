-- add dummy users
insert into MEMBER(ID, EMAIL, NICKNAME, HASHTAG)
values (1, 'admin@admin.com', 'admin', 7777);

insert into MEMBER(ID, EMAIL, NICKNAME, HASHTAG)
values (2, 'hello@world.com', 'user1', 1234);

insert into MEMBER(ID, EMAIL, NICKNAME, HASHTAG)
values (3, 'dummy@dummy.com', 'dummy', 9999);


-- add dummy channels
insert into CHANNEL(ID, NAME, THUMBNAIL, INVITE_CODE)
values (1, 'A 채널', null, 'sadadsadsaad');
insert into CHANNEL(ID, NAME, THUMBNAIL, INVITE_CODE)
values (2, 'B 채널', null, 'sdadsaasasd');
insert into CHANNEL(ID, NAME, THUMBNAIL, invite_code)
values (3, 'C 채널', null, 'sadasdsadsad');

-- add dummy channel member mappings
insert into CHANNEL_MEMBER(ID, CHANNEL_ID, MEMBER_ID)
values (1, 1, 1);
insert into CHANNEL_MEMBER(ID, CHANNEL_ID, MEMBER_ID)
values (2, 2, 1);
insert into CHANNEL_MEMBER(ID, CHANNEL_ID, MEMBER_ID)
values (3, 3, 1);
insert into CHANNEL_MEMBER(ID, CHANNEL_ID, MEMBER_ID)
values (4, 2, 2);
insert into CHANNEL_MEMBER(ID, CHANNEL_ID, MEMBER_ID)
values (5, 3, 2);
insert into CHANNEL_MEMBER(ID, CHANNEL_ID, MEMBER_ID)
values (6, 3, 3);

-- add dummy topics
insert into TOPIC(ID, TITLE, CHANNEL_ID)
values (1, '테스트 제목1', 1);
insert into TOPIC(ID, TITLE, CHANNEL_ID)
values (2, '테스트 제목2', 1);
insert into TOPIC(ID, TITLE, CHANNEL_ID)
values (3, '테스트 제목3', 1);
insert into TOPIC(ID, TITLE, CHANNEL_ID)
values (4, '하하', 2);
insert into TOPIC(ID, TITLE, CHANNEL_ID)
values (5, '호호', 2);
insert into TOPIC(ID, TITLE, CHANNEL_ID)
values (6, '히히', 2);
insert into TOPIC(ID, TITLE, CHANNEL_ID)
values (7, 'ㅇㅅㅇ', 3);
insert into TOPIC(ID, TITLE, CHANNEL_ID)
values (8, 'ㅇㅁㅇ', 3);
insert into TOPIC(ID, TITLE, CHANNEL_ID)
values (9, 'ㅇㅂㅇ', 3);

