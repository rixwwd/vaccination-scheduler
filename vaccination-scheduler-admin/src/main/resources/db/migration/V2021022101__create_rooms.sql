create table rooms (
  id uuid primary key,
  name varchar not null unique,
  
  created_at timestamp not null,
  updated_at timestamp not null
);
