DELETE FROM em_event_category_join;
DELETE FROM em_events;
DELETE FROM em_event_category;
DELETE FROM em_users;

-- 1. USERS
INSERT INTO em_users (first_name, last_name, username, user_role, email, password)
VALUES ('Havard', 'Naddardson', 'Lulu', 'ADMIN', 'lulu.trutru@mail.com', '$2a$10$encodedPasswordForDoggo'),
       ('Leah', 'Glutenstein', 'Pumpkin', 'ADMIN', 'leatthecountess@mail.com', '$2a$10$encodedPasswordForDoggo'),
       ('Baphomette', 'De l''Acienda Del Matador', 'Bethy', 'ORGANIZER', 'bapho.bark@mail.com', '$2a$10$encodedPasswordForDoggo'),
       ('Sylvanas', 'Coursevent', 'DarkLady', 'ORGANIZER', 'sylvanas@undercity.com', '$2a$10$loktarogar'),
       ('Tonks', 'La Protectrice', 'MamaTonks', 'USER', 'tonks.magic@mail.com', '$2a$10$aurorpowah'),
       ('Tarot', 'Le Chat', 'ChaosKitty', 'USER', 'tarot.griffe@mail.com', '$2a$10$meowmeow'),
       ('October', 'Le Planificateur', 'Toebeans', 'ADMIN', 'october.agenda@mail.com', '$2a$10$perfectschedule');

-- 2. CATEGORIES (On force les IDs 1, 2, 3 pour la jointure et on corrige les dates)
INSERT INTO em_event_category (name, icon, created_date, last_updated_date, creator, last_updater)
VALUES
    ('Sport', 'sports_soccer', '2026-06-01 10:00:00', '2026-06-01 10:00:00', 1, 1),
    ('Musique & Concerts', 'music_note', '2026-06-01 10:00:00', '2026-06-01 10:00:00', 1, 2),
    ('Perso', '', '2026-06-01 10:00:00', '2026-06-01 10:00:00', 1, 2);

-- 3. EVENTS (On force les IDs de 1 à 5 et on nettoie les dates)
INSERT INTO em_events (name, description, state, all_day, start_date, end_date, creator, created_date, spots_available)
VALUES
    ('Lancement de l''Event-Manager', 'Grande réunion de lancement avec toute l''équipe au grand complet.', 'PUBLISHED', false, '2026-06-20 10:00:00', '2026-06-20 12:30:00', 1, '2026-06-19 10:00:00', 0),
    ('Entraînement de Parkour & Acrobaties', 'Session intensive de sauts et de Street Workout au parc.', 'PUBLISHED', false, '2026-06-21 18:00:00', '2026-06-21 20:00:00', 1, '2026-06-19 10:00:00', 1),
    ('Foire Médiévale et Reconstitution Viking', 'Journée complète d''immersion historique, combats et artisanat.', 'PUBLISHED', true, '2026-06-24 00:00:00', '2026-06-24 23:59:59', 2, '2026-06-19 10:00:00', 1),
    ('Concert Privé - Electric Callboy', 'Session d''écoute exclusive et balances avant le show.', 'DRAFT', false, '2026-06-26 19:30:00', '2026-06-26 23:00:00', 2, '2026-06-19 10:00:00', 1),
    ('Séance de papouilles & Câlin avec ma chérie', 'Session obligatoire de détente avec les minets, Baphomette, et un gros câlin pour recharger les batteries.', 'DELETED', false, '2026-06-18 18:30:00', '2026-06-18 23:00:00', 4, '2026-06-19 10:00:00', 0),
    ('Concert spectacle Elden Rin et Warcraft', 'De la musique épique !.', 'PUBLISHED', true, '2026-06-24 00:00:00', '2026-06-24 23:59:59', 2, '2026-06-19 10:00:00', 1);

-- 4. JOINTURES (Maintenant les IDs matchent à 100%)
INSERT INTO em_event_category_join (event_id, event_category_id)
VALUES (1, 2),
       (2, 1),
       (3, 1),
       (4, 3),
       (5, 2);