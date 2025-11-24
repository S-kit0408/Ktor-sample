CREATE TYPE privacy_setting_enum AS ENUM('public', 'friends', 'private');
CREATE TYPE auth_type_enum AS ENUM('password', 'google');

CREATE TABLE users (
    id VARCHAR(26) PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    avatar_url VARCHAR(500),
    default_privacy_setting privacy_setting_enum DEFAULT 'private',
    last_login_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 認証情報テーブル
CREATE TABLE auth_credentials (
    id VARCHAR(26) PRIMARY KEY,
    user_id VARCHAR(26) REFERENCES users(id) ON DELETE CASCADE,
    auth_type auth_type_enum NOT NULL,

    -- パスワード認証用
    password_hash VARCHAR(255),

    -- OAuth用
    oauth_provider VARCHAR(50),
    oauth_provider_id VARCHAR(255),

    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    CHECK (
        (auth_type = 'password' AND password_hash IS NOT NULL
            AND oauth_provider IS NULL AND oauth_provider_id IS NULL) OR
        (auth_type = 'google' AND oauth_provider IS NOT NULL
            AND oauth_provider_id IS NOT NULL AND password_hash IS NULL)
    )
);

CREATE INDEX idx_auth_credentials_user ON auth_credentials(user_id);

CREATE UNIQUE INDEX idx_auth_credentials_user_auth_type
    ON auth_credentials(user_id, auth_type);

-- 同じGoogleアカウントで複数ユーザー作成不可
CREATE UNIQUE INDEX idx_auth_credentials_oauth
    ON auth_credentials(oauth_provider, oauth_provider_id)
    WHERE oauth_provider IS NOT NULL;