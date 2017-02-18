DROP TABLE IF EXISTS game;
CREATE TABLE game (
  id              SERIAL4 PRIMARY KEY,
  game_time       TEXT,
  home_team_name  TEXT,
  away_team_name  TEXT,
  home_team_goals INT,
  away_team_goals INT,
  game_type       TEXT,
  team_id         INTEGER REFERENCES team
);