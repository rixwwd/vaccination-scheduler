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
