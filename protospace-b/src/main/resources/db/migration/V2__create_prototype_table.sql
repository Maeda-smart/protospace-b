CREATE TABLE prototype (
  id          SERIAL       NOT NULL,
  name        VARCHAR(128) NOT NULL,
  catchphrase VARCHAR(128) NOT NULL,
  concept     VARCHAR(128) NOT NULL,
  img         VARCHAR(512) NOT NULL,
  PRIMARY KEY (id)
);