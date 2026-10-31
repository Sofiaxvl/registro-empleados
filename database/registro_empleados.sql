CREATE DATABASE IF NOT EXISTS registro_empleados
  CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE registro_empleados;

CREATE TABLE IF NOT EXISTS empleados (
  id      INT AUTO_INCREMENT PRIMARY KEY,
  nombre  VARCHAR(100) NOT NULL,
  cargo   VARCHAR(100),
  salario DECIMAL(12,2) NOT NULL DEFAULT 0,
  correo  VARCHAR(120)
);

INSERT INTO empleados (nombre, cargo, salario, correo) VALUES
  ('Ana Pérez',  'Contadora',     2500000, 'ana@empresa.com'),
  ('Luis Gómez', 'Desarrollador', 3800000, 'luis@empresa.com');
