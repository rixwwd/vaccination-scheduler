create table public_users (
  id uuid primary key,
  loginname varchar not null,
  coupon varchar not null,
  name varchar not null,
  hurigana varchar,
  birthday date,
  address varchar,
  telephone_number varchar,
  email varchar,
  sms varchar,
  
  created_at timestamp not null,
  updated_at timestamp not null
);

create index public_users_coupon on public_users (coupon);
create unique index public_users_loginname on public_users(loginname);
