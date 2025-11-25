CREATE TYPE privacy_setting_enum AS ENUM('public', 'friends', 'private');
CREATE TYPE auth_provider_enum AS ENUM('email', 'google', 'unknown');

CREATE TABLE users (
    id VARCHAR(26) PRIMARY KEY,
    clerk_user_id VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    avatar_url VARCHAR(500),
    primary_auth_provider auth_provider_enum DEFAULT 'unknown',
    default_privacy_setting privacy_setting_enum DEFAULT 'private',
    last_login_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_clerk_id ON users(clerk_user_id);
CREATE INDEX idx_users_email ON users(email);