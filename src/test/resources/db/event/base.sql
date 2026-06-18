DELETE FROM em_event_category_join;
DELETE FROM em_events;
DELETE FROM em_event_category;

INSERT INTO em_event_category (id, name) VALUES
    (1, 'Sport'),
    (2, 'Perso'),
    (3, 'Musique');

INSERT INTO em_events (id, name, description, state, all_day, start_date, end_date) VALUES
    (1, 'Lancement de l''Event-Manager', 'Grande réunion de lancement avec toute l''équipe au grand complet.', 'PUBLISHED', false, '2026-06-20T10:00:00+02:00', '2026-06-20T12:30:00+02:00'),
    (2, 'Entraînement de Parkour & Acrobaties', 'Session intensive de sauts et de Street Workout au parc.', 'PUBLISHED', false, '2026-06-21T18:00:00+02:00', '2026-06-21T20:00:00+02:00'),
    (3, 'Foire Médiévale et Reconstitution Viking', 'Journée complète d''immersion historique, combats et artisanat.', 'DRAFT', true, '2026-06-24T00:00:00+02:00', '2026-06-24T23:59:59+02:00'),
    (4, 'Concert Privé - Electric Callboy', 'Session d''écoute exclusive et balances avant le show.', 'PUBLISHED', false, '2026-06-26T19:30:00+02:00', '2026-06-26T23:00:00+02:00'),
    (5, 'Séance de papouilles & Câlin avec ma chérie', 'Session obligatoire de détente avec les minets, Baphomette, et un gros câlin pour recharger les batteries.', 'DELETED', false, '2026-06-18T18:30:00+02:00', '2026-06-18T23:00:00+02:00');

INSERT INTO em_event_category_join (event_id, event_category_id) VALUES
    (1, 2),
    (2, 1),
    (3, 1),
    (4, 3),
    (5, 2);
