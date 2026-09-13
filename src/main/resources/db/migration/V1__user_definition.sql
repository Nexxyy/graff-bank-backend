CREATE TABLE users
(
    id         UUID PRIMARY KEY,
    name       VARCHAR(100)             NOT NULL,
    email      VARCHAR(255)             NOT NULL UNIQUE,
    document   VARCHAR(255)             NOT NULL UNIQUE,
    password   VARCHAR(255)             NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE account
(
    id         UUID PRIMARY KEY,
    owner_id   UUID                     NOT NULL,
    balance    NUMERIC(19, 4)           NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

