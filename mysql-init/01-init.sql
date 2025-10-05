-- Crear base de datos si no existe
CREATE DATABASE IF NOT EXISTS sales_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Usar la base de datos
USE sales_db;

-- Crear usuario y dar permisos (para MySQL 8+)
CREATE USER IF NOT EXISTS 'admin'@'%' IDENTIFIED BY 'admin123';
GRANT ALL PRIVILEGES ON sales_db.* TO 'admin'@'%';
GRANT CREATE USER ON *.* TO 'admin'@'%';
FLUSH PRIVILEGES;

-- Configuraciones adicionales para mejor compatibilidad
SET GLOBAL max_connections = 1000;
SET GLOBAL wait_timeout = 28800;
