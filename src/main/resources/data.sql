-- add dummy user
insert into member (id, email, nickname, hashtag)
values (member_seq.nextval, 'admin@admin.com', 'admin', '7777');
