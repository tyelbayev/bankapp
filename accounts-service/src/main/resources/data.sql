INSERT INTO users (login, password, name, birthdate)
VALUES ('demo', 'password', 'Demo User', '2000-01-01');

INSERT INTO accounts (currency, value, user_login)
VALUES ('USD', 1000.00, 'demo');
