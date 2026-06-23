-- ============================================================
-- CARO GAME - Database Setup Script
-- Chạy script này trong MySQL Workbench hoặc mysql CLI
-- mysql -u root -p < database_setup.sql
-- ============================================================

CREATE DATABASE IF NOT EXISTS caro_game
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE caro_game;

CREATE TABLE IF NOT EXISTS users (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    username     VARCHAR(20)  NOT NULL UNIQUE COMMENT 'Tên đăng nhập (3-20 ký tự)',
    password_hash VARCHAR(64) NOT NULL COMMENT 'SHA-256 hash của mật khẩu',
    wins         INT DEFAULT 0  COMMENT 'Số trận thắng',
    losses       INT DEFAULT 0  COMMENT 'Số trận thua',
    draws        INT DEFAULT 0  COMMENT 'Số trận hòa',
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS matches (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    player1      VARCHAR(20) NOT NULL COMMENT 'Người chơi X',
    player2      VARCHAR(20) NOT NULL COMMENT 'Người chơi O',
    winner       VARCHAR(20)          COMMENT 'NULL = hòa',
    total_moves  INT DEFAULT 0,
    played_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tạo index để tìm lịch sử trận theo username nhanh hơn
CREATE INDEX idx_matches_player1 ON matches(player1);
CREATE INDEX idx_matches_player2 ON matches(player2);

-- Tài khoản test (password: 123456)
-- SHA-256 của "123456" = 8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92
INSERT IGNORE INTO users (username, password_hash) VALUES
    ('test1', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92'),
    ('test2', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92');

SELECT '✅ Database caro_game đã sẵn sàng!' AS status;
