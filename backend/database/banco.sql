CREATE DATABASE IF NOT EXISTS sistema_votacao_ideias;
USE sistema_votacao_ideias;

CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS ideias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    descricao TEXT NOT NULL,
    usuario_id INT NULL,
    CONSTRAINT fk_ideias_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE IF NOT EXISTS votos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    ideia_id INT NOT NULL,
    CONSTRAINT uk_voto_usuario_ideia UNIQUE (usuario_id, ideia_id),
    CONSTRAINT fk_votos_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_votos_ideia FOREIGN KEY (ideia_id) REFERENCES ideias(id)
);

-- Garante a regra de um voto por usuario/ideia tambem em bancos criados na primeira etapa.
SET @indice_existe = (
    SELECT COUNT(*)
    FROM information_schema.statistics
    WHERE table_schema = DATABASE()
      AND table_name = 'votos'
      AND index_name = 'uk_voto_usuario_ideia'
);
SET @comando = IF(
    @indice_existe = 0,
    'ALTER TABLE votos ADD CONSTRAINT uk_voto_usuario_ideia UNIQUE (usuario_id, ideia_id)',
    'SELECT ''Indice de votos ja existe'' AS mensagem'
);
PREPARE stmt FROM @comando;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
