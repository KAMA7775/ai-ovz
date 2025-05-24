-- Создание таблицы пользователей
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       telegram_id BIGINT UNIQUE NOT NULL,
                       name VARCHAR(100),
                       created_at TIMESTAMP DEFAULT now()
);

-- Таблица напоминаний
CREATE TABLE reminders (
                           id SERIAL PRIMARY KEY,
                           user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
                           text TEXT,
                           remind_at TIMESTAMP,
                           created_at TIMESTAMP DEFAULT now()
);
