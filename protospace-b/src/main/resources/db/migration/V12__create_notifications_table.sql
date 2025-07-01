CREATE TABLE notifications (
  id                 SERIAL        NOT NULL,
  comment_id         INT           NOT NULL,
  prototype_id       INT           NOT NULL,
  recipient_user_id  INT           NOT NULL,
  commenter_user_id  INT           NOT NULL,
  is_read            BOOLEAN       NOT NULL DEFAULT FALSE,
  created_at         TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (comment_id) REFERENCES comments(id) ON DELETE CASCADE,
  FOREIGN KEY (prototype_id) REFERENCES prototype(id) ON DELETE CASCADE,
  FOREIGN KEY (recipient_user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (commenter_user_id) REFERENCES users(id) ON DELETE CASCADE
);