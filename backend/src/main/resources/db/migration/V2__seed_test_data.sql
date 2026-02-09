-- Roles
INSERT INTO app_role (name, description) VALUES
    ('USER', 'Standard user role'),
    ('ADMIN', 'Administrator role with full access'),
    ('MANAGER', 'Manager role with elevated access');

-- Users (passwords are BCrypt-encoded)
-- user/password
INSERT INTO app_user (username, password, email, enabled, account_locked) VALUES
    ('user', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'user@example.com', TRUE, FALSE);
-- admin/admin
INSERT INTO app_user (username, password, email, enabled, account_locked) VALUES
    ('admin', '$2a$10$dXJ3SW6G7P50lGmMQgel2uHXBc6pPZQggiZMHHLShIWMhfEwMBJIq', 'admin@example.com', TRUE, FALSE);
-- manager/manager
INSERT INTO app_user (username, password, email, enabled, account_locked) VALUES
    ('manager', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.', 'manager@example.com', TRUE, FALSE);
-- locked/locked (account locked)
INSERT INTO app_user (username, password, email, enabled, account_locked) VALUES
    ('locked', '$2a$10$KbQiHKTa1YJBEP2nhZCiB.H8RnNMYaplqv./tbk20eRgBQ3n26Xyq', 'locked@example.com', TRUE, TRUE);
-- disabled/disabled (account disabled)
INSERT INTO app_user (username, password, email, enabled, account_locked) VALUES
    ('disabled', '$2a$10$d.CsXFfOYg.0JGDhSjZ6fuFBp.UtzsOcGu1Hx/QLQO6kVf0EYbwRG', 'disabled@example.com', FALSE, FALSE);

-- Role assignments
INSERT INTO app_user_roles (user_id, role_id) VALUES
    (1, 1),  -- user -> USER
    (2, 1),  -- admin -> USER
    (2, 2),  -- admin -> ADMIN
    (3, 1),  -- manager -> USER
    (3, 3),  -- manager -> MANAGER
    (4, 1),  -- locked -> USER
    (5, 1);  -- disabled -> USER
