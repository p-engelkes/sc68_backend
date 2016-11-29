DROP TABLE IF EXISTS user_account;
CREATE TABLE user_account (
  id SERIAL4 PRIMARY KEY,
  user_name VARCHAR(100),
  first_name VARCHAR(100),
  last_name VARCHAR(100),
  password VARCHAR(200),
  created_at DATE DEFAULT CURRENT_DATE
)