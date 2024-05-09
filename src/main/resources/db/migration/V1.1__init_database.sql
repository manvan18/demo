CREATE TABLE IF NOT EXISTS currency (
    code VARCHAR(10) NOT NULL,
    symbol VARCHAR(10) NOT NULL,
    rate NUMERIC(20, 6) NOT NULL,
    description VARCHAR(50) NOT NULL,
    version     INT        NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (code)
);