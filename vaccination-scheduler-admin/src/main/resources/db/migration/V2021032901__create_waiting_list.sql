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
