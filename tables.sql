CREATE TABLE users (
    id int PRIMARY KEY,
    login varchar(255),
    email varchar(255),
    password varchar(255),
    type varchar(255)
);

CREATE TABLE cities (
    id int PRIMARY KEY,
    user_id int,
    city varchar(255),
    country varchar(255),
    FOREIGN KEY (user_id) REFERENCES users(id),
    UNIQUE (city, country)
);