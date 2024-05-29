use user_auth;

-- Initialize @row
SET @row = (SELECT COALESCE(MAX(user_id), 0) FROM users);

-- Insert new rows using @row
INSERT INTO users (date_created, date_updated, user_id, email, password, phone_number, username, role)
SELECT
    TIMESTAMPADD(SECOND, -FLOOR(RAND() * TIMESTAMPDIFF(SECOND, '2024-01-01', NOW())), NOW()),
    TIMESTAMPADD(SECOND, -FLOOR(RAND() * TIMESTAMPDIFF(SECOND, '2024-01-01', NOW())), NOW()),
    @row := @row + 1,
    CONCAT('user', @row, '@example.com'),
    TO_BASE64(RANDOM_BYTES(16)),
    '123-456-7890',
    CONCAT('user', @row),
    'ADMIN'
FROM (
    SELECT a.a FROM 
    (SELECT 1 AS a UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) a
) t;