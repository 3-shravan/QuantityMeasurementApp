CREATE TABLE IF NOT EXISTS quantity_measurement_entity (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_value DOUBLE NOT NULL,
    first_unit VARCHAR(64) NOT NULL,
    second_value DOUBLE NULL,
    second_unit VARCHAR(64) NULL,
    operation VARCHAR(64) NOT NULL,
    measurement_type VARCHAR(64) NOT NULL,
    result VARCHAR(255) NOT NULL,
    error BOOLEAN NOT NULL DEFAULT FALSE,
    error_message VARCHAR(500) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_qme_operation ON quantity_measurement_entity(operation);
CREATE INDEX IF NOT EXISTS idx_qme_measurement_type ON quantity_measurement_entity(measurement_type);

CREATE TABLE IF NOT EXISTS quantity_measurement_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    measurement_id BIGINT NOT NULL,
    operation VARCHAR(64) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_qmh_measurement
        FOREIGN KEY (measurement_id) REFERENCES quantity_measurement_entity(id) ON DELETE CASCADE
);
