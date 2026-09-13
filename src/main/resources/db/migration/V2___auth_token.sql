CREATE TABLE refresh_token
(
    id         UUID PRIMARY KEY,
    user_id    UUID                     NOT NULL,
    hash       CHAR(43)                 NOT NULL UNIQUE,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    revoked    BOOLEAN                  NOT NULL DEFAULT FALSE
)