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
  max_number_of_people integer not null,
  
  created_at timestamp not null,
  updated_at timestamp not null,
  
  foreign key (room_id) references rooms (id)
);

create index cells_room_id on cells (room_id);


create table public_users (
  id uuid primary key,
  login_name varchar not null,
  password varchar not null,
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

create unique index public_users_coupon on public_users (coupon);
create unique index public_users_login_name on public_users(login_name);


create table reservations (
  id uuid primary key,
  cell_id uuid not null,
  public_user_id  uuid not null,
  coupon varchar not null,
  reservation_number varchar not null,
 
  created_at timestamp not null,
  
  foreign key (cell_id) references cells (id),
  foreign key (public_user_id) references public_users (id)
);


create table vaccines(
  id uuid primary key,
  expected_delivery_date date,
  quantity integer not null,
  room_id uuid not null,
  
  created_at timestamp not null,
  updated_at timestamp not null
);


create table vaccination_histories (
  id uuid primary key,
  public_user_id uuid not null,
  vaccinated_at date not null,
  
  created_at timestamp not null
);

