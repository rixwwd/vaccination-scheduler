-- V2021022101__create_rooms.sql
create table rooms (
  id uuid primary key,
  name varchar not null unique,
  vaccine varchar not null,

  created_at timestamp not null,
  updated_at timestamp not null
);

-- V2021022102__create_cells.sql
create table cells (
  id uuid primary key,
  room_id uuid not null,
  begin_time timestamp not null,
  end_time timestamp not null,
  capacity integer not null,
  accepted_count integer not null,
  reservation_count integer not null,

  created_at timestamp not null,
  updated_at timestamp not null,

  foreign key (room_id) references rooms (id)
);

create index cells_room_id on cells (room_id);

-- V2021022103__create_public_users.sql
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

-- V2021022104__create_reservations.sql
create table reservations (
  id uuid primary key,
  cell_id uuid not null,
  public_user_id uuid not null,
  coupon varchar not null,
  reservation_number varchar not null,
  accepted boolean not null,
  vaccinated boolean not null,

  created_at timestamp not null,
  updated_at timestamp not null,

  foreign key (cell_id) references cells (id),
  foreign key (public_user_id) references public_users (id),

  unique (public_user_id, coupon)
);

-- V2021022105__create_vaccine_stock.sql
create table vaccine_stocks(
  id uuid primary key,
  expected_delivery_date date not null,
  quantity integer not null,
  room_id uuid not null,
  reservation_count integer not null,
  vaccinated_count integer not null,
  vaccine varchar not null,

  created_at timestamp not null,
  updated_at timestamp not null
);

create index vaccine_stocks_room_id_expected_delivery_date on vaccine_stocks (room_id, expected_delivery_date);

-- V2021022106__create_vaccination_histories.sql
create table vaccination_histories (
  id uuid primary key,
  public_user_id uuid not null,
  vaccinated_at date not null,
  room_id uuid not null,
  vaccine varchar not null,

  created_at timestamp not null
);

-- V2021022501__create_admin_users.sql
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

insert into admin_users (id, username, password, enabled, name, created_at, updated_at) values ('057f57a6-c2f4-4dea-8444-1bdb769253b7', 'admin', '{noop}admin', true, 'Admin', current_timestamp, current_timestamp);

-- V2021030901__create_coupons.sql
create table coupons (
  public_user_id uuid not null,
  coupon varchar not null,
  name varchar not null,
  used boolean not null,
  used_at timestamp,
  created_at timestamp not null,
  updated_at timestamp not null,

  primary key (public_user_id, coupon),
  foreign key (public_user_id) references public_users (id),
  unique (public_user_id, name)

);

create index coupons_public_user_id on coupons (public_user_id);

-- V2021032901__create_waiting_list.sql
create table waiting_list (
  cell_id uuid,
  public_user_id uuid,
  created_at timestamp not null,

  primary key(cell_id, public_user_id),
  foreign key (public_user_id) references public_users (id),
  foreign key (cell_id) references cells (id)
);

create index waiting_list_cell_id on waiting_list (cell_id);
create index waiting_list_public_user_id on waiting_list (public_user_id);

-- V2021040401__create_action_log.sql
create table action_log (
  id uuid primary key,
  public_user_id uuid not null,
  action_type varchar not null,
  ip_address varchar not null,
  created_at timestamp not null
);

create index action_log_public_user_id on action_log(public_user_id);

-- V2021041901__add_role_on_admin_users.sql
alter table admin_users add column role varchar;
update admin_users set role = 'ADMIN';
alter table admin_users alter column role set not null;

-- V2021042101__add_failed_login_count_on_admin_users.sql
alter table admin_users add column failed_login_count integer not null default 0;

-- V2021042102__add_last_failed_login_on_admin_users.sql
alter table admin_users add column last_failed_login timestamp;

