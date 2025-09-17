INSERT INTO users (id, email, password_hash, display_name, role)
VALUES (1, 'laura@example.com', 'secret', 'Laura', 'USER');

INSERT INTO books (id, title, description, publication_year, author_id, category_id)
VALUES (1, 'Ego Is the Enemy',
        'Hoe je je ego herkent en temt om beter te presteren.',
        2016,
        (SELECT id FROM authors WHERE name = 'Ryan Holiday'),
        (SELECT id FROM categories WHERE name = 'Stoicism')),

       (2, 'The Obstacle Is the Way',
        'Stoïcijnse strategie: draai obstakels om in kansen.',
        2014,
        (SELECT id FROM authors WHERE name = 'Ryan Holiday'),
        (SELECT id FROM categories WHERE name = 'Stoicism')),

       (3, 'Meditations',
        'Klassieke stoïcijnse overpeinzingen (moderne uitgave).',
        2002,
        (SELECT id FROM authors WHERE name = 'Ryan Holiday'),
        (SELECT id FROM categories WHERE name = 'Stoicism')),

       (4, 'Atomic Habits',
        'Kleine gewoontes, groot effect.',
        2018,
        (SELECT id FROM authors WHERE name = 'Ryan Holiday'),
        (SELECT id FROM categories WHERE name = 'Productivity')),

       (5, 'Deep Work',
        'Gefocust, ongestoord werken voor topresultaat.',
        2016,
        (SELECT id FROM authors WHERE name = 'Ryan Holiday'),
        (SELECT id FROM categories WHERE name = 'Productivity'));



-- INSERT INTO books (id, title, description, publication_year, author_id, category_id) VALUES
--         ('Ego Is the Enemy',        '—', 2016,
--         (SELECT id FROM authors    WHERE name = 'Ryan Holiday'),
--         (SELECT id FROM categories WHERE name = 'Stoicism')),
--
--         ('The Obstacle Is the Way',  '—', 2014,
--         (SELECT id FROM authors    WHERE name = 'Ryan Holiday'),
--         (SELECT id FROM categories WHERE name = 'Stoicism')),
--
--         ('Meditations',              '—', 2002,
--         (SELECT id FROM authors    WHERE name = 'Ryan Holiday'),
--         (SELECT id FROM categories WHERE name = 'Stoicism')),
--
--         ('Atomic Habits',            '—', 2018,
--         (SELECT id FROM authors    WHERE name = 'Ryan Holiday'),
--         (SELECT id FROM categories WHERE name = 'Productivity')),
--
--         ('Deep Work',                '—', 2016,
--         (SELECT id FROM authors    WHERE name = 'Ryan Holiday'),
--         (SELECT id FROM categories WHERE name = 'Productivity')),
--
--         ('Digital Minimalism',       '—', 2019,
--         (SELECT id FROM authors    WHERE name = 'Ryan Holiday'),
--         (SELECT id FROM categories WHERE name = 'Productivity'));