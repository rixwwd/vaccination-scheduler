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
