CREATE TABLE prototype_tags (
  prototype_id  INT          NOT NULL,
  tags_id       INT          NOT NULL,
  PRIMARY KEY (prototype_id, tags_id),
  FOREIGN KEY (prototype_id) REFERENCES prototype(id) ON DELETE CASCADE,
  FOREIGN KEY (tags_id) REFERENCES tags(id) ON DELETE CASCADE
);
CREATE INDEX idx_tags_id ON prototype_tags (tags_id);