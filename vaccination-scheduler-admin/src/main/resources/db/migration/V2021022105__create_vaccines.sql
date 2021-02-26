create table vaccines(
  id uuid primary key,
  expected_delivery_date date,
  quantity integer not null,
  room_id uuid not null,
  
  created_at timestamp not null,
  updated_at timestamp not null
);
