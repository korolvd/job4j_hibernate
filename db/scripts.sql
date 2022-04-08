CREATE TABLE IF NOT EXISTS candidate
(
    id SERIAL PRIMARY KEY,
    name TEXT,
    experience DOUBLE PRECISION,
    salary INT
);

CREATE TABLE IF NOT EXISTS vacancy
(
    id SERIAL PRIMARY KEY,
    name TEXT
);

CREATE TABLE IF NOT EXISTS vacancy_store
(
    id SERIAL PRIMARY KEY,
    category TEXT
);