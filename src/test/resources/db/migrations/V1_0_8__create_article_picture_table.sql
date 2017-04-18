DROP TABLE IF EXISTS article_picture;
CREATE TABLE article_picture (
  id         SERIAL4 PRIMARY KEY,
  picture    TEXT,
  width      INTEGER,
  height     INTEGER,
  ratio      REAL,
  article_id INTEGER REFERENCES article,
  created_at TIMESTAMPTZ DEFAULT current_timestamp
)