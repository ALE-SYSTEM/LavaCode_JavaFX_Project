CREATE DATABASE IF NOT EXISTS db_lavacao;
USE db_lavacao;

-- Tabela de motor
CREATE TABLE IF NOT EXISTS motor (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     potencia INT NOT NULL
) ENGINE=InnoDB;

CREATE TABLE marca(
                      id int NOT NULL auto_increment,
                      nome  varchar(50) NOT NULL,
                      CONSTRAINT pk_marca
                          PRIMARY KEY(id)
) engine = InnoDB;

CREATE TABLE cor(
                    id int NOT NULL auto_increment,
                    nome varchar(50) NOT NULL,
                    CONSTRAINT pk_cor
                        PRIMARY KEY(id)
) engine = InnoDB;

CREATE TABLE servico(
                        id int NOT NULL auto_increment,
                        descricao varchar(200),
                        valor decimal(10,2) NOT NULL,
                        pontos int,
                        CONSTRAINT pk_servico
                            PRIMARY KEY(id)
) engine = InnoDB;

CREATE TABLE modelo(
                       id int NOT NULL auto_increment,
                       descricao varchar(200),
                       potencia int,
                       id_marca int NOT NULL,
                       id_motor int NOT NULL,
                       categoria ENUM('PEQUENO', 'MEDIO', 'GRANDE', 'MOTO', 'PADRAO') NOT NULL DEFAULT 'PADRAO',
                       combustivel ENUM('GASOLINA', 'ETANOL', 'FLEX', 'DIESEL', 'GNV', 'OUTRO') NOT NULL DEFAULT 'OUTRO',
                       CONSTRAINT pk_modelo
                           PRIMARY KEY(id),
                       CONSTRAINT fk_modelo_marca
                           FOREIGN KEY(id_marca)
                               REFERENCES marca(id),
                          CONSTRAINT fk_modelo_motor
                           FOREIGN KEY(id_motor)
                               REFERENCES motor(id)
) engine = InnoDB;

CREATE TABLE cliente(
                        id int NOT NULL auto_increment,
                        nome varchar(100) NOT NULL,
                        celular varchar(30),
                        email varchar(50),
                        endereco varchar(200),
                        data_cadastro DATE,
                        pontuacao int DEFAULT 0,
                        CONSTRAINT pk_cliente
                            PRIMARY KEY(id)
) engine = InnoDB;

CREATE TABLE pessoafisica(
                              id_cliente INT NOT NULL REFERENCES cliente(id),
                              cpf VARCHAR(20) NOT NULL,
                              data_nascimento DATE NOT NULL,
                              CONSTRAINT pk_pessoa_fisica PRIMARY KEY (id_cliente),
                              CONSTRAINT fk_pessoa_fisica_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id)
                                  ON DELETE CASCADE
                                  ON UPDATE CASCADE
) engine=InnoDB;

CREATE TABLE pessoajuridica(
                                id_cliente INT NOT NULL REFERENCES cliente(id),
                                cnpj VARCHAR(20) NOT NULL,
                                inscricao_estadual VARCHAR(30) NOT NULL,
                                CONSTRAINT pk_pessoa_juridica PRIMARY KEY (id_cliente),
                                CONSTRAINT fk_pessoa_juridica_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id)
                                    ON DELETE CASCADE
                                    ON UPDATE CASCADE
) engine=InnoDB;

CREATE TABLE veiculo(
                        id int NOT NULL auto_increment,
                        placa varchar(15) NOT NULL,
                        observacoes varchar(200),
                        id_modelo int NOT NULL,
                        id_cor int NOT NULL,
                        id_cliente int NOT NULL,
                        CONSTRAINT pk_veiculo
                            PRIMARY KEY(id),
                        CONSTRAINT fk_veiculo_modelo
                            FOREIGN KEY(id_modelo)
                                REFERENCES modelo(id),
                        CONSTRAINT fk_veiculo_cor
                            FOREIGN KEY(id_cor)
                                REFERENCES cor(id),
                        CONSTRAINT fk_veiculo_cliente
                            FOREIGN KEY(id_cliente)
                                REFERENCES cliente(id)
) engine = InnoDB;

CREATE TABLE ordem_servico(
                              numero int NOT NULL auto_increment,
                              total decimal(10,2) NOT NULL,
                              agenda DATE NOT NULL,
                              desconto double DEFAULT 0.0,
                              id_veiculo int NOT NULL,
                              os_status ENUM('ABERTA', 'FECHADA', 'CANCELADA') NOT NULL DEFAULT 'ABERTA',
                              CONSTRAINT pk_ordem_servico
                                  PRIMARY KEY(numero),
                              CONSTRAINT fk_ordem_servico_veiculo
                                  FOREIGN KEY(id_veiculo)
                                      REFERENCES veiculo(id)
) engine = InnoDB;

CREATE TABLE item_os(
                        id_servico int NOT NULL REFERENCES servico(id),
                        numero_os int NOT NULL REFERENCES ordem_servico(numero),
                        valor_servico decimal(10, 2),
                        observacoes varchar(200),

                        CONSTRAINT pk_item
                            PRIMARY KEY(id_servico, numero_os)
) engine = InnoDB;

-- Motor
INSERT INTO motor (potencia) VALUES (100);
INSERT INTO motor (potencia) VALUES (120);
INSERT INTO motor (potencia) VALUES (140);
INSERT INTO motor (potencia) VALUES (180);
INSERT INTO motor (potencia) VALUES (90);

INSERT INTO marca(nome) VALUES('Ford');
INSERT INTO marca(nome) VALUES('Ferrari');
INSERT INTO marca(nome) VALUES('Audi');
INSERT INTO marca(nome) VALUES('Fiat');

INSERT INTO cor(nome) VALUES('Azul');
INSERT INTO cor(nome) VALUES('Preto');
INSERT INTO cor(nome) VALUES('Branco');

INSERT INTO servico(descricao, valor, pontos) VALUES ('Lavacao Completa', 70.0, 10);
INSERT INTO servico(descricao, valor, pontos) VALUES ('Polimento', 50.0, 10);
INSERT INTO servico(descricao, valor, pontos) VALUES ('Lavação Motor', 100.0, 10);

INSERT INTO modelo(descricao, id_marca, id_motor) VALUES ('Ka', 1, 1);
INSERT INTO modelo(descricao, id_marca, id_motor) VALUES ('Q5', 3, 2);
INSERT INTO modelo(descricao, id_marca, id_motor) VALUES ('Toro', 4, 3);

INSERT INTO cliente(nome, celular, email, endereco, data_cadastro, pontuacao) VALUES ('Jose Alfredo', '(48)992345677',
                                                                                       'josealfredo@email.com', 'Rua Jose Alfredo, 356 - Florianopolis', '2024-11-05', 30);
INSERT INTO cliente(nome, celular, email, endereco, data_cadastro, pontuacao) VALUES ('Mariana Silva', '(11)988749090',
                                                                                       'marianasilva@email.com', 'Avenida das Marianas, 4455 - Cidade', '2022-03-10', 40);
INSERT INTO cliente(nome, celular, email, endereco, data_cadastro, pontuacao) VALUES ('Empresa nova', '(51)999234563',
                                                                                       'empresanova@email.com', 'Rua 7, casa 3 - Cidade', '2024-12-01', 20);
INSERT INTO cliente(nome, celular, email, endereco, data_cadastro, pontuacao) VALUES ('Outra empresa', '(47)3233-4556',
                                                                                       'outraempresa@email.com', 'Avenida das empresas, 123 - Jardim empresarial', '2023-08-23', 0);

INSERT INTO pessoafisica(id_cliente, cpf, data_nascimento) VALUES (1, '123.456.789-01', '1987-02-15');
INSERT INTO pessoafisica(id_cliente, cpf, data_nascimento) VALUES (2, '234.567.890-12', '1997-07-01');

INSERT INTO pessoajuridica(id_cliente, cnpj, inscricao_estadual) VALUES (3, '12.345.678/0001-00', '388.108.598.269');
INSERT INTO pessoajuridica(id_cliente, cnpj, inscricao_estadual) VALUES (4, '11.245.648/0001-10', '456.789.123.852');

INSERT INTO veiculo(placa, observacoes, id_modelo, id_cor, id_cliente) VALUES ('EJJ7765','Arranhado na porta direita', 1, 2, 1);
INSERT INTO veiculo(placa, observacoes, id_modelo, id_cor, id_cliente) VALUES ('TRJ7I64','Lama no teto', 2, 1, 2);
INSERT INTO veiculo(placa, observacoes, id_modelo, id_cor, id_cliente) VALUES ('GRA6399','Carro de madame', 3, 3, 4);

INSERT INTO ordem_servico(total, agenda, id_veiculo) VALUES (165.90, '2024-12-08', 1);
INSERT INTO ordem_servico(total, agenda, id_veiculo) VALUES (89.90, '2024-01-09', 2);
INSERT INTO ordem_servico(total, agenda, id_veiculo) VALUES (68, '2024-03-08', 3);

INSERT INTO item_os(id_servico, numero_os, valor_servico, observacoes) VALUES (1, 1, 100, 'Usar cera pasta');
INSERT INTO item_os(id_servico, numero_os, valor_servico, observacoes) VALUES (2, 1, 50, 'Normal');
INSERT INTO item_os(id_servico, numero_os, valor_servico, observacoes) VALUES (3, 2, 189.90, 'Entregar as 18:00');
