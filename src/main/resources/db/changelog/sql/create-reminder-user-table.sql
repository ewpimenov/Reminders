CREATE TABLE reminder
(
    id          BIGINT PRIMARY KEY,
    title       VARCHAR (50),
    description VARCHAR (50),
    remind      DATE,
    user_id     BIGINT
);

CREATE TABLE users(
    id          BIGINT PRIMARY KEY,
    username    VARCHAR (50),
    password    VARCHAR (250)
);

INSERT INTO reminder (id, title, description, remind, user_id)
VALUES  ('1', 'Земля', 'Круглая', '2024-10-18 16:04:04.000000', '1'),
        ('2', 'Луна', 'Серая', '2024-10-19 16:05:05.00000', '2' ),
        ('3', 'Марс', 'Красный', '2024-10-20 16:06:06.00000', '3' ),
        ('4', 'Сатурн', 'С кольцами', '2024-10-21 16:07:07.00000', '4' );

INSERT INTO users (id, username, password)
VALUES ('1', '1', '$2y$10$3MCoDR3xbZLJ16Z2KbqUUuCnU7d/rGG4WVZckVNCTAqckOZgTstW.');