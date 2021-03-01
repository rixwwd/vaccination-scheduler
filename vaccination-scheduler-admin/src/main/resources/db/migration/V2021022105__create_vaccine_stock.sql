create table vaccine_stocks(
  id uuid primary key,
  expected_delivery_date date,
  quantity integer not null,
  room_id uuid not null,
  
  created_at timestamp not null,
  updated_at timestamp not null
);

create index vaccine_stocks_room_id_expected_delivery_date on vaccine_stocks (room_id, expected_delivery_date);
