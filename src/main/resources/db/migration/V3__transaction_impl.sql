CREATE TABLE transfers
(
    id          UUID PRIMARY KEY,
    source      UUID                                               NOT NULL,
    destination UUID                                               NOT NULL,
    amount      NUMERIC(19, 4)                                     NOT NULL,
    description VARCHAR(255),
    signature   VARCHAR(255)                                       NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,

    CONSTRAINT transfers_source_fk FOREIGN KEY (source) REFERENCES account (id),
    CONSTRAINT transfers_destination_fk FOREIGN KEY (destination) REFERENCES account (id),
    CONSTRAINT transfers_amount_check CHECK (amount > 0),
    CONSTRAINT transfers_accounts_check CHECK (source <> destination)
);

CREATE TABLE transfer_idempotency
(
    id          UUID PRIMARY KEY,
    key         VARCHAR(255)             NOT NULL UNIQUE,
    hash        VARCHAR(64)              NOT NULL,
    transfer_id UUID,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT transfer_idempotency_transfer_fk FOREIGN KEY (transfer_id)
        REFERENCES transfers (id)
);

CREATE INDEX index_transfer_idempotency_transfer ON transfer_idempotency (transfer_id);