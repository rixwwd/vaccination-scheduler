create table waiting_list (
  cell_id uuid not null,
  public_user_id uuid not null,
  created_at timestamp not null,
  
  foreign key (public_user_id) references public_users (id),
  foreign key (cell_id) references cells (id)
);

create index waiting_list_cell_id on waiting_list (cell_id);
