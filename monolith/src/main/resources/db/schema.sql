-- User Management Tables
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS quantity_measurement_entity (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    this_value DOUBLE NOT NULL,
    this_unit VARCHAR(64) NOT NULL,
    this_measurement_type VARCHAR(64) NOT NULL,
    that_value DOUBLE NULL,
    that_unit VARCHAR(64) NULL,
    that_measurement_type VARCHAR(64) NULL,
    operation VARCHAR(32) NOT NULL,
    result_string VARCHAR(255) NULL,
    result_value DOUBLE NULL,
    result_unit VARCHAR(64) NULL,
    result_measurement_type VARCHAR(64) NULL,
    is_error BOOLEAN NOT NULL DEFAULT FALSE,
    error_message VARCHAR(500) NULL,
    user_id BIGINT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_qme_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_qme_operation ON quantity_measurement_entity(operation);
CREATE INDEX IF NOT EXISTS idx_qme_this_measurement_type ON quantity_measurement_entity(this_measurement_type);
CREATE INDEX IF NOT EXISTS idx_qme_created_at ON quantity_measurement_entity(created_at);
CREATE INDEX IF NOT EXISTS idx_qme_user_id ON quantity_measurement_entity(user_id);

CREATE TABLE IF NOT EXISTS quantity_measurement_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    measurement_id BIGINT NOT NULL,
    operation VARCHAR(64) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_qmh_measurement
        FOREIGN KEY (measurement_id) REFERENCES quantity_measurement_entity(id) ON DELETE CASCADE
);
