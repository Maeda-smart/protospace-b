CREATE TABLE prototype (
  id            SERIAL       NOT NULL,
  prototypeName VARCHAR(128) NOT NULL,
  catchCopy     VARCHAR(128) NOT NULL,
  concept       VARCHAR(128) NOT NULL,
  img           VARCHAR(512) NOT NULL,
  user_id       INT          NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);