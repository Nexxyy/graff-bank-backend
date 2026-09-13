ALTER TABLE users
    ADD COLUMN credit_limit NUMERIC(15, 2) NOT NULL DEFAULT 100,
    ADD COLUMN closing_day SMALLINT NOT NULL DEFAULT 25,
    ADD COLUMN due_day SMALLINT NOT NULL DEFAULT 7,
    ADD CONSTRAINT users_credit_limit_check CHECK (credit_limit > 0),
    ADD CONSTRAINT users_closing_day_check CHECK (closing_day BETWEEN 1 AND 31),
    ADD CONSTRAINT users_due_day_check CHECK (due_day BETWEEN 1 AND 31);

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

CREATE TYPE invoice_status AS ENUM (
    'OPEN',
    'CLOSED',
    'PAID',
    'OVERDUE',
    'CANCELLED'
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
    processed_at TIMESTAMP WITH TIME ZONE
);

-- The cards will be for demonstration purposes only, so there will be no PCI DSS security compliance.

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
    expires_at  TIMESTAMP WITH TIME ZONE NOT NULL
)