DELETE FROM meals;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (user_id, date_time, description, calories) VALUES
(100001, '2019-02-25 08:05', 'admin meal', 1500),
(100000, '2019-02-25 08:00', 'user meal 1', 1000),
(100000, '2019-02-25 12:00', 'user meal 2', 250),
(100000, '2019-02-25 21:00', 'user meal 3', 750),
(100000, '2019-02-26 08:00', 'user meal 4', 1000),
(100000, '2019-02-26 12:00', 'user meal 5', 250),
(100000, '2019-02-26 21:00', 'user meal 6', 750);
