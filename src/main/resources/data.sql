INSERT INTO authors (name) VALUES
        ('Ryan Holiday'),
        ('Marcus Aurelius'),
        ('James Clear'),
        ('Cal Newport');

INSERT INTO categories (name) VALUES
        ('Stoicism'),
        ('Productivity'),
        ('Philosophy');

INSERT INTO books (title, description, publication_year, author_id, category_id) VALUES
        ('Ego Is the Enemy',        '—', 2016,
        (SELECT id FROM authors    WHERE name = 'Ryan Holiday'),
        (SELECT id FROM categories WHERE name = 'Stoicism')),

        ('The Obstacle Is the Way',  '—', 2014,
        (SELECT id FROM authors    WHERE name = 'Ryan Holiday'),
        (SELECT id FROM categories WHERE name = 'Stoicism')),

        ('Meditations',              '—', 2002,
        (SELECT id FROM authors    WHERE name = 'Marcus Aurelius'),
        (SELECT id FROM categories WHERE name = 'Stoicism')),

        ('Atomic Habits',            '—', 2018,
        (SELECT id FROM authors    WHERE name = 'James Clear'),
        (SELECT id FROM categories WHERE name = 'Productivity')),

        ('Deep Work',                '—', 2016,
        (SELECT id FROM authors    WHERE name = 'Cal Newport'),
        (SELECT id FROM categories WHERE name = 'Productivity')),

        ('Digital Minimalism',       '—', 2019,
        (SELECT id FROM authors    WHERE name = 'Cal Newport'),
        (SELECT id FROM categories WHERE name = 'Productivity'));