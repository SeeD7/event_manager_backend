DROP TABLE IF EXISTS em_participate;

CREATE TABLE em_participate
(
    user_id   BIGINT NOT NULL,
    event_id  BIGINT NOT NULL,
    CONSTRAINT pk_participate PRIMARY KEY (user_id, event_id)
);

ALTER TABLE em_participate
    ADD CONSTRAINT fk_participate_on_event FOREIGN KEY (event_id) REFERENCES em_events (id);

ALTER TABLE em_participate
    ADD CONSTRAINT fk_participate_on_user FOREIGN KEY (user_id) REFERENCES em_users (id);

ALTER TABLE em_events
    ADD spots_available BIGINT;

ALTER TABLE em_events
    ADD created_date TIMESTAMP;

ALTER TABLE em_events
    ADD last_updated_date TIMESTAMP;

ALTER TABLE em_events
    ADD creator BIGINT;

ALTER TABLE em_events
    ADD last_updater BIGINT;

ALTER TABLE em_events
    ADD CONSTRAINT fk_creator FOREIGN KEY (creator) REFERENCES em_users (id);

ALTER TABLE em_events
    ADD CONSTRAINT fk_last_updater FOREIGN KEY (last_updater) REFERENCES em_users (id);

ALTER TABLE em_event_category
    ADD icon VARCHAR(25);

ALTER TABLE em_event_category
    ADD created_date TIMESTAMP;

ALTER TABLE em_event_category
    ADD last_updated_date TIMESTAMP;

ALTER TABLE em_event_category
    ADD creator BIGINT;

ALTER TABLE em_event_category
    ADD last_updater BIGINT;

ALTER TABLE em_event_category
    ADD CONSTRAINT fk_creator FOREIGN KEY (creator) REFERENCES em_users (id);

ALTER TABLE em_event_category
    ADD CONSTRAINT fk_last_updater FOREIGN KEY (last_updater) REFERENCES em_users (id);