create table rooms (
  id uuid primary key,
  name varchar not null unique,
  
  created_at timestamp not null,
  updated_at timestamp not null
);

create table cells (
  id uuid primary key,
  room_id uuid not null,
  begin_time timestamp not null,
  end_time timestamp not null,
  capacity integer not null,
  reservation_count integer not null default 0,
  accepted_count integer not null default 0,
  
  created_at timestamp not null,
  updated_at timestamp not null,
  
  foreign key (room_id) references rooms (id)
);

create index cells_room_id on cells (room_id);


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


create table reservations (
  id uuid primary key,
  cell_id uuid not null,
  public_user_id  uuid not null,
  coupon varchar not null,
  reservation_number varchar not null,
  accepted boolean not null,
 
  created_at timestamp not null,
  updated_at timestamp not null,
  
  foreign key (cell_id) references cells (id),
  foreign key (public_user_id) references public_users (id),
  
  unique (public_user_id, coupon)
);



create table vaccine_stocks (
  id uuid primary key,
  expected_delivery_date date not null,
  quantity integer not null,
  room_id uuid not null,
  reservation_count integer not null default 0,
  vaccinated_count integer not null default 0,
  
  created_at timestamp not null,
  updated_at timestamp not null
);
create index vaccine_stocks_room_id_expected_delivery_date on vaccine_stocks (room_id, expected_delivery_date);


create table vaccination_histories (
  id uuid primary key,
  public_user_id uuid not null,
  vaccinated_at date not null,
  
  created_at timestamp not null
);

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
