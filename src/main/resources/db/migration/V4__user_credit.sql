CREATE TYPE loan_status as ENUM (
    'APPROVED',
    'DECLINED',
    'PROCESSING',
    'PENDING',
    'PAID'
);

CREATE TYPE card_type as ENUM(
    'CREDIT',
    'DEBIT'
);

CREATE TYPE card_status as ENUM(
    'EXPIRED',
    'VALID',
    'BLOCKED'
);

CREATE TABLE loan
(
    id           UUID PRIMARY KEY,
    name         VARCHAR(255),
    status       loan_status              NOT NULL,
    requester    UUID                     NOT NULL,
    amount       NUMERIC(19, 4)           NOT NULL,
    paid_amount  NUMERIC(19, 4)           NOT NULL DEFAULT 0,
    chance       NUMERIC,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL,
    processed_at TIMESTAMP WITH TIME ZONE,

    CONSTRAINT loan_requester_fk FOREIGN KEY (requester) REFERENCES users (id),
    CONSTRAINT loan_amount_check CHECK (amount > 0),
    CONSTRAINT loan_paid_amount_check CHECK (paid_amount >= 0),
    CONSTRAINT loan_paid_limit_check CHECK (paid_amount <= amount),
    CONSTRAINT loan_chance_check CHECK (chance IS NULL OR chance BETWEEN 0 AND 100)
);

CREATE INDEX index_loan_requester ON loan (requester);

-- DEMONSTRATION ONLY.
-- This table is NOT PCI DSS compliant and must never contain real card data.

CREATE TABLE card
(
    id          UUID PRIMARY KEY,
    owner       UUID                     NOT NULL,
    name        VARCHAR(100)             NOT NULL,
    card_number CHAR(16)                 NOT NULL,
    cvv         CHAR(3),
    type        card_type                NOT NULL,
    status      card_status              NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    expires_at  TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT card_owner_fk FOREIGN KEY (owner) REFERENCES users (id)
)