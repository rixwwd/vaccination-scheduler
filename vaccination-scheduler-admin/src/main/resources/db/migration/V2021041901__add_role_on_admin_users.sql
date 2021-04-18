alter table admin_users add column role varchar;
update admin_users set role = 'ADMIN';
alter table admin_users alter column role set not null;
