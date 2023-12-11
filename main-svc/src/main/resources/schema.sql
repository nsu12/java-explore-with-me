CREATE TABLE IF NOT EXISTS category (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    CONSTRAINT uniq_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(250) NOT NULL,
    email VARCHAR(254) NOT NULL,
    CONSTRAINT uniq_name_email UNIQUE (name, email)
);

CREATE TABLE IF NOT EXISTS event (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(120) NOT NULL,
    annotation VARCHAR(2000) NOT NULL,
    description VARCHAR(7000) NOT NULL,
    category_id BIGINT REFERENCES category(id) ON DELETE RESTRICT,
    event_date TIMESTAMP WITHOUT TIME ZONE,
    initiator_id BIGINT REFERENCES users(id) ON DELETE RESTRICT,
    location_lat FLOAT,
    location_lon FLOAT,
    paid BOOLEAN,
    participant_limit INTEGER DEFAULT 0,
    request_moderation BOOLEAN DEFAULT TRUE,
    created_on TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    published_on TIMESTAMP WITHOUT TIME ZONE,
    state VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS participation_request (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id BIGINT REFERENCES event(id) ON DELETE RESTRICT,
    requester_id BIGINT REFERENCES users(id) ON DELETE RESTRICT,
    created_on TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    status VARCHAR(10),
    CONSTRAINT uniq_event_requester UNIQUE (event_id, requester_id)
);

CREATE TABLE IF NOT EXISTS compilation (
    id  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    pinned BOOLEAN,
    CONSTRAINT uniq_title UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS compilation_event
(
    compilation_id BIGINT REFERENCES compilation(id),
    event_id BIGINT REFERENCES event(id),
    PRIMARY KEY (compilation_id, event_id)
);

CREATE TABLE IF NOT EXISTS comment
(
    id  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    text VARCHAR(1000),
    event_id BIGINT REFERENCES event(id) ON DELETE CASCADE,
    author_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    created_on TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    edited BOOLEAN DEFAULT FALSE
);

