CREATE TABLE refresh_token
(
    id         UUID PRIMARY KEY,
    user_id    UUID                     NOT NULL,
    hash       CHAR(43)                 NOT NULL UNIQUE,
    revoked    BOOLEAN                  NOT NULL DEFAULT FALSE,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT refresh_token_user_fk FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX index_refresh_token_user ON refresh_token (user_id);