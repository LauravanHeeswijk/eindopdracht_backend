INSERT INTO authors (name)
SELECT 'Ryan Holiday'
    WHERE NOT EXISTS (SELECT 1 FROM authors WHERE name = 'Ryan Holiday');

INSERT INTO categories (name)
SELECT 'Stoicism'
    WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Stoicism');

INSERT INTO categories (name)
SELECT 'Productivity'
    WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Productivity');

INSERT INTO categories (name)
SELECT 'Management'
    WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Management');

INSERT INTO users (id, email, password_hash, display_name, role)
SELECT 1,
       'laura@example.com',
       '$2a$10$fOEdjJNKKpMRF.dfkBUgqOcc/8AUYrnXNWsWWs11cX3c1Cg0/PCC.',
       'Laura',
       'USER'
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE id = 1);

INSERT INTO users (id, email, password_hash, display_name, role)
SELECT 2,
       'admin@example.com',
       '$2a$10$l81TtEkVCrMTn/raiK92k.QF0LwXHqimFgFMxwoMteZqIuGPP97A6', -- "secret"
       'Admin',
       'ADMIN'
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE id = 2);

INSERT INTO books (id, title, description, publication_year, author_id, category_id)
SELECT 1,
       'Ego Is the Enemy',
       'Hoe je je ego herkent en temt om beter te presteren.',
       2016,
       (SELECT id FROM authors    WHERE name = 'Ryan Holiday'),
       (SELECT id FROM categories WHERE name = 'Stoicism')
    WHERE NOT EXISTS (SELECT 1 FROM books WHERE id = 1);

INSERT INTO books (id, title, description, publication_year, author_id, category_id)
SELECT 2,
       'The Obstacle Is the Way',
       'Stoïcijnse strategie: draai obstakels om in kansen.',
       2014,
       (SELECT id FROM authors    WHERE name = 'Ryan Holiday'),
       (SELECT id FROM categories WHERE name = 'Stoicism')
    WHERE NOT EXISTS (SELECT 1 FROM books WHERE id = 2);

INSERT INTO books (id, title, description, publication_year, author_id, category_id)
SELECT 3,
       'Meditations',
       'Klassieke stoïcijnse overpeinzingen (moderne uitgave).',
       2002,
       (SELECT id FROM authors    WHERE name = 'Ryan Holiday'),
       (SELECT id FROM categories WHERE name = 'Stoicism')
    WHERE NOT EXISTS (SELECT 1 FROM books WHERE id = 3);

INSERT INTO books (id, title, description, publication_year, author_id, category_id)
SELECT 4,
       'Atomic Habits',
       'Kleine gewoontes, groot effect.',
       2018,
       (SELECT id FROM authors    WHERE name = 'Ryan Holiday'),
       (SELECT id FROM categories WHERE name = 'Productivity')
    WHERE NOT EXISTS (SELECT 1 FROM books WHERE id = 4);

INSERT INTO books (id, title, description, publication_year, author_id, category_id)
SELECT 5,
       'The Daily Stoic',
       '366 meditaties over wijsheid, volharding en levenskunst.',
       2016,
       (SELECT id FROM authors    WHERE name = 'Ryan Holiday'),
       (SELECT id FROM categories WHERE name = 'Management')
    WHERE NOT EXISTS (SELECT 1 FROM books WHERE id = 5);

/* ==== FIXED: kolomnamen matchen nu je entity (filename, content_type, size) ==== */
INSERT INTO file_assets (id, filename, content_type, size)
SELECT 101, 'atomic-habits.pdf', 'application/pdf', 0
    WHERE NOT EXISTS (SELECT 1 FROM file_assets WHERE id = 101);

INSERT INTO file_assets (id, filename, content_type, size)
SELECT 102, 'ego-is-the-enemy.pdf', 'application/pdf', 0
    WHERE NOT EXISTS (SELECT 1 FROM file_assets WHERE id = 102);

INSERT INTO file_assets (id, filename, content_type, size)
SELECT 103, 'meditations.pdf', 'application/pdf', 0
    WHERE NOT EXISTS (SELECT 1 FROM file_assets WHERE id = 103);

INSERT INTO file_assets (id, filename, content_type, size)
SELECT 104, 'the-obstacle-is-the-way.pdf', 'application/pdf', 0
    WHERE NOT EXISTS (SELECT 1 FROM file_assets WHERE id = 104);

INSERT INTO file_assets (id, filename, content_type, size)
SELECT 105, 'the-daily-stoic.pdf', 'application/pdf', 0
    WHERE NOT EXISTS (SELECT 1 FROM file_assets WHERE id = 105);

/* Koppel files aan boeken */
UPDATE books SET file_asset_id = 101 WHERE id = 1;
UPDATE books SET file_asset_id = 102 WHERE id = 2;
UPDATE books SET file_asset_id = 103 WHERE id = 3;
UPDATE books SET file_asset_id = 104 WHERE id = 4;
UPDATE books SET file_asset_id = 105 WHERE id = 5;




