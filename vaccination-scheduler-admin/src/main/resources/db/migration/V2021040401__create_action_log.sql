create table action_log (
  id uuid primary key,
  public_user_id uuid not null,
  action_type varchar not null,
  ip_address varchar not null,
  created_at timestamp not null
);

create index action_log_public_user_id on action_log(public_user_id);
