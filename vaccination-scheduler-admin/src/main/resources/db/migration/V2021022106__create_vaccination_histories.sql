create table vaccination_histories (
  id uuid primary key,
  public_user_id uuid not null,
  vaccinated_at date not null,
  room_id uuid not null,
  vaccine varchar not null,
  
  created_at timestamp not null
);
