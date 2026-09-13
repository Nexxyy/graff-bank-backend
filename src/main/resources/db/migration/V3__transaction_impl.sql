CREATE TABLE transfer_idempotency
(
    id          UUID PRIMARY KEY,
    key         VARCHAR(255)             NOT NULL UNIQUE,
    hash        VARCHAR(64)              NOT NULL,
    transfer_id UUID,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE transfers
(
    id            UUID PRIMARY KEY,
    source        UUID           NOT NULL,
    destination   UUID           NOT NULL,
    amount        NUMERIC(19, 4) NOT NULL,
    description   VARCHAR(255),
    signature     VARCHAR(255)   NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
)