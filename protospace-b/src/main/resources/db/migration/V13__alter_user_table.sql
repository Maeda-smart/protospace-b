ALTER TABLE users ADD COLUMN role_name VARCHAR(64) NOT NULL DEFAULT 'ROLE_USER';
ALTER TABLE users ADD COLUMN enable BOOLEAN NOT NULL DEFAULT true;