ALTER TABLE article
  ALTER COLUMN created_at TYPE TIMESTAMPTZ;
ALTER TABLE article
  ALTER COLUMN created_at SET DEFAULT current_timestamp;

ALTER TABLE team
  ALTER COLUMN created_at TYPE TIMESTAMPTZ;
ALTER TABLE team
  ALTER COLUMN created_at SET DEFAULT current_timestamp;

ALTER TABLE user_account
  ALTER COLUMN created_at TYPE TIMESTAMPTZ;
ALTER TABLE user_account
  ALTER COLUMN created_at SET DEFAULT current_timestamp;

ALTER TABLE user_profile_picture
  ALTER COLUMN created_at TYPE TIMESTAMPTZ;
ALTER TABLE user_profile_picture
  ALTER COLUMN created_at SET DEFAULT current_timestamp;