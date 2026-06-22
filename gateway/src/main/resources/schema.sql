CREATE TABLE IF NOT EXISTS credenciais (
    cpf   VARCHAR(15)  NOT NULL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    role  VARCHAR(10)  NOT NULL DEFAULT 'CLIENTE'
);

INSERT INTO credenciais (cpf, email, senha, role)
VALUES ('00000000000', 'admin@pizzaria.com', 'admin123', 'ADMIN');
