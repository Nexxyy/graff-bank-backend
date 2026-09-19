CREATE TYPE outbox_event_status AS ENUM (
    'PUBLISHED',
    'PENDING',
    'PROCESSING'
);

CREATE TABLE outbox_event
(
    id           BIGSERIAL PRIMARY KEY,
    aggregate_id UUID                     NOT NULL,
    event_type   VARCHAR(255)             NOT NULL,
    status       outbox_event_status      NOT NULL,
    routing_key  VARCHAR(255)             NOT NULL,
    payload      TEXT                     NOT NULL,
    occurred_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    published_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX index_outbox_pending ON outbox_event (id) WHERE status = 'PENDING';