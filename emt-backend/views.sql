-- Materialized view for books by author count
CREATE MATERIALIZED VIEW book_counts_by_author AS
SELECT a.id as author_id,
       a.name as author_name,
       a.surname as author_surname,
       COUNT(b.id) as book_count
FROM authors a
LEFT JOIN books b ON a.id = b.author_id
WHERE b.deleted = FALSE OR b.deleted IS NULL
GROUP BY a.id, a.name, a.surname;


-- Materialized view for authors by country count
CREATE MATERIALIZED VIEW author_counts_by_country AS
SELECT c.id as country_id,
       c.name as country_name,
       c.continent,
       COUNT(a.id) as author_count
FROM countries c
LEFT JOIN authors a ON c.id = a.country_id
GROUP BY c.id, c.name, c.continent;


CREATE UNIQUE INDEX book_counts_by_author_idx ON book_counts_by_author (author_id);
CREATE UNIQUE INDEX author_counts_by_country_idx ON author_counts_by_country (country_id);