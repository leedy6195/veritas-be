insert into admin (id, login_id, name, password) values (1, 'admin', '이현태', '$2a$10$rO7ZveHMgvNyuF/18HjtteflqFXc3KTY7BTWB8VWVoVYVqnabTdme');

insert into reading_room (id, name, width, height) values (1, '독서실 A', 10, 10);

insert into seat (id, reading_room_id, name, x, y, status) values (1, 1, '1', 2, 3, 'IDLE');

insert into student (id, serial, name, school, email, birth_date, tel, parent_tel, course_type, created_at)
 values (1, '20240412102500', '이동윤', '대치고', 'leedy6195@hanmail.net', '900101', '010-1234-1234', '010-2345-2345', 'GENERAL', NOW());

insert into student (id, serial, name, school, email, birth_date, tel, parent_tel, course_type, created_at)
values (2, '20240412102501', '최민용', '구일고', 'minidragon81@naver.com', '880101', '010-1234-1234', '010-2345-2345', 'GENERAL', NOW());


insert into kiosk (id, name, reading_room_id) values (1, '독서실A 키오스크', 1);

insert into entry_device (id, name, kiosk_id, access_type) values (1, '독서실 A 퇴실', 1, 'OUT');

insert into lecture_room (id, name) values (1, '강의실 A');

insert into entry_device (id, name, lecture_room_id, access_type) values (2, '강의실 A 입실', 1, 'IN');
insert into entry_device (id, name, lecture_room_id, access_type) values (3, '강의실 A 퇴실', 1, 'OUT');

insert into reading_room_access (id, reading_room_id, seat_id, student_id, enter_time, exit_time)
 values (1, 1, 1, 1, '2024-04-21 10:17:29', '2024-04-21 11:00:15');

insert into lecture_room_access (id, lecture_room_id, student_id, enter_time, exit_time)
values (1, 1, 1, '2024-04-22 14:47:29', '2024-04-22 15:00:15');

    insert into lecture_room_access (id, lecture_room_id, student_id, enter_time)
values (2, 1, 1, '2024-04-20 15:47:29');