CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    login VARCHAR(250) UNIQUE NOT NULL,
    email TEXT UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    full_name TEXT,
    timezone TEXT NOT NULL DEFAULT 'UTC',
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    enabled BOOLEAN NOT NULL DEFAULT true
);

CREATE TABLE IF NOT EXISTS roles (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_roles(
    id_user UUID NOT NULL,
    id_role BIGINT NOT NULL,
    FOREIGN KEY (id_user) REFERENCES users(id),
    FOREIGN KEY (id_role) REFERENCES roles(id)
);