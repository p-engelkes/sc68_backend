CREATE TABLE old_classes (
  id           SERIAL4 PRIMARY KEY,
  name         TEXT,
  order_number INT
);

INSERT INTO old_classes (name, order_number) VALUES
  ('Herren', 0),
  ('A-Junioren', 1),
  ('B-Junioren', 2),
  ('C-Junioren', 3),
  ('D-Junioren', 4),
  ('E-Junioren', 5),
  ('F-Junioren', 6),
  ('G-Junioren', 7);

ALTER TABLE team
  ADD COLUMN order_number INT;
ALTER TABLE team
  ADD COLUMN old_class_id INT;
ALTER TABLE team
  ADD CONSTRAINT fk_old_class
FOREIGN KEY (old_class_id)
REFERENCES old_classes (id);