CREATE TABLE nice (
    prototype_id INT NOT NULL,
    user_id INT NOT NULL,

    PRIMARY KEY (prototype_id, user_id),
    FOREIGN KEY (prototype_id) REFERENCES prototype(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE

);