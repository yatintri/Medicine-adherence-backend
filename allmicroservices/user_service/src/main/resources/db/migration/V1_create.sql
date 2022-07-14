CREATE TABLE user(

  user_id VARCHAR(40) primary key,
  user_name varchar(100) not null,
  email varchar(100) not null,
  last_login varchar(40),
  created_at date

  );