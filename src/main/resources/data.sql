-- AUTHORS
INSERT INTO authors (name)
SELECT 'Ryan Holiday'
    WHERE NOT EXISTS (SELECT 1 FROM authors WHERE name = 'Ryan Holiday');

-- CATEGORIES
INSERT INTO categories (name) SELECT 'Stoicism'
    WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Stoicism');
INSERT INTO categories (name) SELECT 'Productivity'
    WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Productivity');
INSERT INTO categories (name) SELECT 'Management'
    WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Management');

-- USERS
INSERT INTO users (email, password_hash, display_name, role)
SELECT 'laura@example.com',
       '$2a$10$VT1AQkjtOJmc2TPFdH820upX0m3LslmpHzmWUzDj7OxYKpo9NDaWy',
       'Laura',
       'USER'
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'laura@example.com');

INSERT INTO users (email, password_hash, display_name, role)
SELECT 'admin@example.com',
       '$2a$10$VT1AQkjtOJmc2TPFdH820upX0m3LslmpHzmWUzDj7OxYKpo9NDaWy',
       'Admin',
       'ADMIN'
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@example.com');

-- let op: verwijderde het per ongeluk geplaatste '<' teken in het hashveld
INSERT INTO users (email, password_hash, display_name, role)
VALUES ('testuser@example.com',
        '$2a$10$VT1AQkjtOJmc2TPFdH820upX0m3LslmpHzmWUzDj7OxYKpo9NDaWy',
        'Test User',
        'USER')
    ON CONFLICT (email) DO NOTHING;

UPDATE users
SET password_hash = '$2a$10$VT1AQkjtOJmc2TPFdH820upX0m3LslmpHzmWUzDj7OxYKpo9NDaWy'
WHERE email IN ('laura@example.com','admin@example.com');

-- BOOKS
INSERT INTO books (title, description, publication_year, author_id, category_id)
SELECT 'Ego Is the Enemy',
       'Hoe je je ego herkent en temt om beter te presteren.',
       2016,
       (SELECT id FROM authors WHERE name = 'Ryan Holiday'),
       (SELECT id FROM categories WHERE name = 'Stoicism')
    WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Ego Is the Enemy');

INSERT INTO books (title, description, publication_year, author_id, category_id)
SELECT 'The Obstacle Is the Way',
       'Stoïcijnse strategie: draai obstakels om in kansen.',
       2014,
       (SELECT id FROM authors WHERE name = 'Ryan Holiday'),
       (SELECT id FROM categories WHERE name = 'Stoicism')
    WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'The Obstacle Is the Way');

INSERT INTO books (title, description, publication_year, author_id, category_id)
SELECT 'Meditations',
       'Klassieke stoïcijnse overpeinzingen (moderne uitgave).',
       2002,
       (SELECT id FROM authors WHERE name = 'Ryan Holiday'),
       (SELECT id FROM categories WHERE name = 'Stoicism')
    WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Meditations');

INSERT INTO books (title, description, publication_year, author_id, category_id)
SELECT 'Atomic Habits',
       'Kleine gewoontes, groot effect.',
       2018,
       (SELECT id FROM authors WHERE name = 'Ryan Holiday'),
       (SELECT id FROM categories WHERE name = 'Productivity')
    WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Atomic Habits');

INSERT INTO books (title, description, publication_year, author_id, category_id)
SELECT 'The Daily Stoic',
       '366 meditaties over wijsheid, volharding en levenskunst.',
       2016,
       (SELECT id FROM authors WHERE name = 'Ryan Holiday'),
       (SELECT id FROM categories WHERE name = 'Management')
    WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'The Daily Stoic');

-- FILE ASSETS
INSERT INTO file_assets (filename, content_type, size)
SELECT 'ego-is-the-enemy.pdf', 'application/pdf', 0
    WHERE NOT EXISTS (SELECT 1 FROM file_assets WHERE filename = 'ego-is-the-enemy.pdf');

INSERT INTO file_assets (filename, content_type, size)
SELECT 'the-obstacle-is-the-way.pdf', 'application/pdf', 0
    WHERE NOT EXISTS (SELECT 1 FROM file_assets WHERE filename = 'the-obstacle-is-the-way.pdf');

INSERT INTO file_assets (filename, content_type, size)
SELECT 'meditations.pdf', 'application/pdf', 0
    WHERE NOT EXISTS (SELECT 1 FROM file_assets WHERE filename = 'meditations.pdf');

INSERT INTO file_assets (filename, content_type, size)
SELECT 'atomic-habits.pdf', 'application/pdf', 0
    WHERE NOT EXISTS (SELECT 1 FROM file_assets WHERE filename = 'atomic-habits.pdf');

INSERT INTO file_assets (filename, content_type, size)
SELECT 'the-daily-stoic.pdf', 'application/pdf', 0
    WHERE NOT EXISTS (SELECT 1 FROM file_assets WHERE filename = 'the-daily-stoic.pdf');

-- KOPPEL BOOKS -> FILE_ASSETS
-- Gebruik LIMIT 1 in een subselect zodat duplicaten geen error geven.
UPDATE books b
SET file_asset_id = fa.id
    FROM (
    SELECT id FROM file_assets
    WHERE filename = 'ego-is-the-enemy.pdf'
    ORDER BY id DESC
    LIMIT 1
) fa
WHERE b.title = 'Ego Is the Enemy';

UPDATE books b
SET file_asset_id = fa.id
    FROM (
    SELECT id FROM file_assets
    WHERE filename = 'the-obstacle-is-the-way.pdf'
    ORDER BY id DESC
    LIMIT 1
) fa
WHERE b.title = 'The Obstacle Is the Way';

UPDATE books b
SET file_asset_id = fa.id
    FROM (
    SELECT id FROM file_assets
    WHERE filename = 'meditations.pdf'
    ORDER BY id DESC
    LIMIT 1
) fa
WHERE b.title = 'Meditations';

UPDATE books b
SET file_asset_id = fa.id
    FROM (
    SELECT id FROM file_assets
    WHERE filename = 'atomic-habits.pdf'
    ORDER BY id DESC
    LIMIT 1
) fa
WHERE b.title = 'Atomic Habits';

UPDATE books b
SET file_asset_id = fa.id
    FROM (
    SELECT id FROM file_assets
    WHERE filename = 'the-daily-stoic.pdf'
    ORDER BY id DESC
    LIMIT 1
) fa
WHERE b.title = 'The Daily Stoic';
