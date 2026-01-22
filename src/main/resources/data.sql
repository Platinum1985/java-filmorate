-- Для таблицы mpa
INSERT INTO mpa (rating)
SELECT 'G'
WHERE NOT EXISTS (SELECT 1 FROM mpa WHERE rating = 'G');

INSERT INTO mpa (rating)
SELECT 'PG'
WHERE NOT EXISTS (SELECT 1 FROM mpa WHERE rating = 'PG');

INSERT INTO mpa (rating)
SELECT 'PG-13'
WHERE NOT EXISTS (SELECT 1 FROM mpa WHERE rating = 'PG-13');

INSERT INTO mpa (rating)
SELECT 'R'
WHERE NOT EXISTS (SELECT 1 FROM mpa WHERE rating = 'R');

INSERT INTO mpa (rating)
SELECT 'NC-17'
WHERE NOT EXISTS (SELECT 1 FROM mpa WHERE rating = 'NC-17');

-- Для таблицы genres
INSERT INTO genres (name)
SELECT 'Комедия'
WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Комедия');

INSERT INTO genres (name)
SELECT 'Драма'
WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Драма');

INSERT INTO genres (name)
SELECT 'Мультфильм'
WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Мультфильм');

INSERT INTO genres (name)
SELECT 'Триллер'
WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Триллер');

INSERT INTO genres (name)
SELECT 'Документальный'
WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Документальный');

INSERT INTO genres (name)
SELECT 'Боевик'
WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Боевик');
