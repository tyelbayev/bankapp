CREATE TABLE IF NOT EXISTS users (
    login VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    birthdate DATE NOT NULL
    );

CREATE TABLE IF NOT EXISTS accounts (
                                        id SERIAL PRIMARY KEY,
                                        currency VARCHAR(10) NOT NULL,
    value NUMERIC(15, 2) NOT NULL,
    user_login VARCHAR(50) NOT NULL REFERENCES users(login)
    );

INSERT INTO users (login, password, name, birthdate)
SELECT 'user1', 'user1', 'user1', '2000-01-01'
    WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE login = 'user1'
);

INSERT INTO accounts (currency, value, user_login)
SELECT 'USD', 1000.00, 'user1'
    WHERE NOT EXISTS (
    SELECT 1 FROM accounts
    WHERE user_login = 'user1' AND currency = 'USD'
);
