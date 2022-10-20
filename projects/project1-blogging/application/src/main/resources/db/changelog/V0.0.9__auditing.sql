alter table "user" add column created_date timestamp default current_timestamp;
alter table "user" add column last_modified_date timestamp default current_timestamp;
