INSERT INTO user (id, username, salt, password)
VALUES (1, 'simon', 'c6e964f5f140e118722348e745df140d', '44c535c518e9655043a5e20ca640a5cb');

INSERT INTO application (id, name, username, password, description, user_id)
VALUES (1, 'kochen.com', 'nudelholz', 'asdf', 'password', 1)