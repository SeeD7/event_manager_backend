DROP TABLE IF EXISTS em_waiting_list;

CREATE TABLE em_waiting_list
(
    user_id   BIGINT NOT NULL,
    event_id  BIGINT NOT NULL,
    joined_at TIMESTAMP DEFAULT NOW(),
    CONSTRAINT pk_waiting_list PRIMARY KEY (user_id, event_id)
);

ALTER TABLE em_waiting_list
    ADD CONSTRAINT fk_waiting_list_on_event FOREIGN KEY (event_id) REFERENCES em_events (id);

ALTER TABLE em_waiting_list
    ADD CONSTRAINT fk_waiting_list_on_user FOREIGN KEY (user_id) REFERENCES em_users (id);