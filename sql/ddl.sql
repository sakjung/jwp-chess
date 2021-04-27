USE test;

CREATE TABLE rooms (
    room_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(12) NOT NULL,
    turn VARCHAR(6) NOT NULL,
    state JSON NOT NULL,
    PRIMARY KEY (room_id),
    UNIQUE KEY (name)
);

CREATE TABLE players (
    id VARCHAR(20) NOT NULL,
    password VARCHAR(20) NOT NULL,
    room_id INT,
    color VARCHAR(6),
    PRIMARY KEY (id),
    FOREIGN KEY (room_id)
    REFERENCES rooms(room_id) ON UPDATE CASCADE
);
