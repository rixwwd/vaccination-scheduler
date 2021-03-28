create table public_users (
  id uuid primary key,
  login_name varchar not null,
  password varchar not null,
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

create unique index public_users_login_name on public_users(login_name);
