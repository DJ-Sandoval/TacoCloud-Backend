CREATE DATABASE IF NOT EXISTS tacocloud_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE tacocloud_db;

CREATE USER IF NOT EXISTS 'admin'@'%' IDENTIFIED BY 'admin123';
GRANT ALL PRIVILEGES ON tacocloud_db.* TO 'admin'@'%';
FLUSH PRIVILEGES;

SET GLOBAL max_connections = 1000;
SET GLOBAL wait_timeout = 28800;
