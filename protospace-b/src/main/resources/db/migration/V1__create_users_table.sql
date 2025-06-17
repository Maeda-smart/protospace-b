CREATE TABLE users (
   id       SERIAL             NOT NULL,
   nickname VARCHAR(128)    NOT NULL,
   email VARCHAR(128)    NOT NULL UNIQUE,
   password VARCHAR(128)    NOT NULL,
   profile VARCHAR(512)    NOT NULL,
   affiliation VARCHAR(128)    NOT NULL,
   position VARCHAR(128)    NOT NULL,
   PRIMARY KEY (id)
);