alter table reservations add column (
  accepted boolean not null, 
  updated_at timestamp not null
);
