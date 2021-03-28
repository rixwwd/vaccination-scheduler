create table reservations (
  id uuid primary key,
  cell_id uuid not null,
  public_user_id uuid not null,
  coupon varchar not null,
  reservation_number varchar not null,
  accepted boolean not null,
 
  created_at timestamp not null,
  updated_at timestamp not null,
  
  foreign key (cell_id) references cells (id),
  foreign key (public_user_id) references public_users (id),
  
  unique (public_user_id, coupon)
);
