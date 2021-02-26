create table admin_users (
  id uuid primary key,
  username varchar not null,
  password varchar not null,
  enabled boolean not null,
  name varchar not null,
  
  created_at timestamp not null,
  updated_at timestamp not null
);

create unique index admin_users_username on admin_users (username);

insert into admin_users (id, username, password, enabled, name, created_at, updated_at) values (random_uuid(), 'admin', '{noop}admin', true, 'Admin', current_timestamp, current_timestamp);
