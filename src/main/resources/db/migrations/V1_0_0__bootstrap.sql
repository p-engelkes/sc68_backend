CREATE EXTENSION IF NOT EXISTS hstore;

DROP TABLE IF EXISTS team;
CREATE TABLE team (
  id             SERIAL4 PRIMARY KEY,
  name           VARCHAR(100) CONSTRAINT unique_team_name UNIQUE,
  training_times hstore,
  created_at     DATE DEFAULT CURRENT_DATE
);

DROP TABLE IF EXISTS user_account;
CREATE TABLE user_account (
  id              SERIAL4 PRIMARY KEY,
  user_name       VARCHAR(100) CONSTRAINT unique_user_name UNIQUE,
  first_name      VARCHAR(100),
  last_name       VARCHAR(100),
  password        VARCHAR(200),
  email           VARCHAR(200) CONSTRAINT unique_email_address UNIQUE,
  position        VARCHAR(100),
  backnumber      INTEGER,
  team_id         INTEGER REFERENCES team,
  profile_picture TEXT,
  created_at      DATE DEFAULT CURRENT_DATE
);

DROP TABLE IF EXISTS article;
CREATE TABLE article (
  id         SERIAL4 PRIMARY KEY,
  title      TEXT,
  content    TEXT,
  author_id  INTEGER REFERENCES user_account,
  team_id    INTEGER REFERENCES team,
  created_at DATE DEFAULT CURRENT_DATE
);