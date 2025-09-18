INSERT INTO authors (name)
SELECT 'Ryan Holiday'
    WHERE NOT EXISTS (SELECT 1 FROM authors WHERE name = 'Ryan Holiday');

INSERT INTO categories (name)
SELECT 'Stoicism'
    WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Stoicism');

INSERT INTO categories (name)
SELECT 'Productivity'
    WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Productivity');

INSERT INTO users (id, email, password_hash, display_name, role)
SELECT 1, 'laura@example.com', 'secret', 'Laura', 'USER'
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE id = 1);

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
       'Deep Work',
       'Gefocust, ongestoord werken voor topresultaat.',
       2016,
       (SELECT id FROM authors    WHERE name = 'Ryan Holiday'),
       (SELECT id FROM categories WHERE name = 'Productivity')
    WHERE NOT EXISTS (SELECT 1 FROM books WHERE id = 5);
