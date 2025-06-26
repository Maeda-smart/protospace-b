CREATE TABLE prototype_read_status (
    prototype_id INT NOT NULL,
    user_id INT NOT NULL,
    read_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (prototype_id, user_id),
    FOREIGN KEY (prototype_id) REFERENCES prototype(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
