CREATE TABLE users
(
    id           UUID PRIMARY KEY,
    name         VARCHAR(100)             NOT NULL,
    email        VARCHAR(255)             NOT NULL UNIQUE,
    document     VARCHAR(255)             NOT NULL UNIQUE,
    password     VARCHAR(255)             NOT NULL,
    credit_limit NUMERIC(15, 2)           NOT NULL DEFAULT 100,
    closing_day  SMALLINT                 NOT NULL DEFAULT 25,
    due_day      SMALLINT                 NOT NULL DEFAULT 7,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT users_credit_limit_check CHECK (credit_limit > 0),
    CONSTRAINT users_closing_day_check CHECK (closing_day BETWEEN 1 AND 31),
    CONSTRAINT users_due_day_check CHECK (due_day BETWEEN 1 AND 31)
);

CREATE TABLE account
(
    id         UUID PRIMARY KEY,
    owner_id   UUID                     NOT NULL,
    balance    NUMERIC(19, 4)           NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT account_owner_fk FOREIGN KEY (owner_id) REFERENCES users (id)
);

CREATE INDEX index_account_owner ON account (owner_id);

