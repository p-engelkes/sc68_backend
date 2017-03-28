DROP TABLE IF EXISTS team_picture;
CREATE TABLE team_picture (
  id         SERIAL4 PRIMARY KEY,
  picture    TEXT,
  width      INTEGER,
  height     INTEGER,
  ratio      REAL,
  team_id    INTEGER REFERENCES team,
  created_at TIMESTAMPTZ DEFAULT current_timestamp
)