CREATE TABLE "table" (
  id              SERIAL4 PRIMARY KEY,
  position        INT,
  name            TEXT,
  icon            TEXT,
  games           INT,
  won_games       INT,
  tied_games      INT,
  lost_games      INT,
  goal_ratio      TEXT,
  goal_difference INT,
  points          INT,
  team_id         INT REFERENCES team
);