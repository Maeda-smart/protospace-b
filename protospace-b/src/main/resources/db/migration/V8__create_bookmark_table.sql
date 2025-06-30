CREATE TABLE bookmark (
    user_id INT NOT NULL,
    prototype_id INT NOT NULL,
    
    PRIMARY KEY (user_id, prototype_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (prototype_id) REFERENCES prototype(id) ON DELETE CASCADE

);