CREATE TABLE app_role (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE app_user (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    account_locked BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE app_user_roles (
    user_id BIGINT NOT NULL REFERENCES app_user(id),
    role_id BIGINT NOT NULL REFERENCES app_role(id),
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE refresh_token (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL,
    expiry_date TIMESTAMP NOT NULL
);
