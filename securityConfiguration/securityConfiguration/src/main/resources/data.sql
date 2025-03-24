-- Insert into users
INSERT INTO users (email, password) VALUES
                                           ('admin1@gmail.com', '$2a$12$RTNq7JGW68FJDTsc3bGxmeyQueGFqAOh5NByss/JyvCglxKYZQ6zi'),
                                           ('client1@gmail.com', '$2a$12$RTNq7JGW68FJDTsc3bGxmeyQueGFqAOh5NByss/JyvCglxKYZQ6zi');

-- Insert roles
INSERT INTO roles(name, admin, client)
VALUES
    ('ROLE_ADD_SOMETHING', 1, 0),
    ('ROLE_FIND_SOMETHING', 1, 1);

-- Assign roles automatically based on user type
INSERT INTO user_roles (user_id, role_id)
select 1,id from roles where admin=1;

INSERT INTO user_roles (user_id, role_id)
select 2,id from roles where client=1;
