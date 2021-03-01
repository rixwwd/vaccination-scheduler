insert into rooms (id, name, created_at, updated_at) values ('107c201a-790c-4fcf-bd86-ad60d5f15d39', '市役所 会議室', current_timestamp, current_timestamp);

insert into cells (id, room_id, begin_time, end_time, capacity, created_at, updated_at) values 
('0030ef90-cab9-408f-bb4a-83967fb1555e', '107c201a-790c-4fcf-bd86-ad60d5f15d39', '2021-04-01 10:00:00', '2021-04-01 11:00:00', 10, current_timestamp, current_timestamp),
('f3253ef8-606a-4a9e-901d-7eefbe2e4f22', '107c201a-790c-4fcf-bd86-ad60d5f15d39', '2021-04-01 11:00:00', '2021-04-01 12:00:00', 10, current_timestamp, current_timestamp),
('6cbd671c-2051-4228-87eb-5831e1c73d61', '107c201a-790c-4fcf-bd86-ad60d5f15d39', '2021-04-01 13:00:00', '2021-04-01 14:00:00', 10, current_timestamp, current_timestamp);

insert into vaccine_stocks (id, expected_delivery_date, quantity, room_id, created_at, updated_at) values ('711afbe5-ee16-455c-879f-e389e7d44735', '2021-03-01', 10, '107c201a-790c-4fcf-bd86-ad60d5f15d39', current_timestamp, current_timestamp);

insert into public_users (id, login_name, password, coupon, name, hurigana, birthday, address, telephone_number, email, sms, created_at, updated_at) values ('5c8ae3ed-faba-45be-b1e2-7b766e791305', '1234567890', '{noop}password', '11111', 'テスト氏名', 'てすとしめい', null, null, null, null, null, current_timestamp, current_timestamp);
