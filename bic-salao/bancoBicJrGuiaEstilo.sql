-- BIC-Jr Guia de Estilo
-- Banco de Dados do Salão

-- -----------------------------------------------
-- criar e utilizar o esquema do SGBD

DROP DATABASE IF EXISTS `bicjrguiaestilov2`;

CREATE SCHEMA IF NOT EXISTS bicJrGuiaEstiloV2;

USE bicJrGuiaEstiloV2;

-- -----------------------------------------------


-- -----------------------------------------------
-- criar a tabela de cidade

CREATE TABLE bicJrGuiaEstiloV2.tbCidade (
  cdCidade INT NOT NULL AUTO_INCREMENT,
  nmCidade VARCHAR(50) NOT NULL,
  PRIMARY KEY (cdCidade));

-- -----------------------------------------------


-- -----------------------------------------------
-- criar a tabela de cliente

CREATE TABLE bicJrGuiaEstiloV2.tbCliente (
  cdCliente INT NOT NULL AUTO_INCREMENT,
  nmCliente VARCHAR(50) NOT NULL,
  sexo VARCHAR(1) NOT NULL,
  dtNasc DATE NOT NULL,
  cpf VARCHAR(14) NOT NULL,
  rg VARCHAR(16) NOT NULL,
  email VARCHAR(255) NOT NULL,
  rua VARCHAR(50) NOT NULL,
  numero INT NOT NULL,
  complemento VARCHAR(50),
  bairro VARCHAR(50) NOT NULL,
  cep VARCHAR(10) NOT NULL,
  telefone VARCHAR(15) NOT NULL,
  observacao VARCHAR(50) NOT NULL,
  cdCidade INT NOT NULL,
  PRIMARY KEY (cdCliente),
  FOREIGN KEY (cdCidade) REFERENCES tbCidade(cdCidade),
  UNIQUE INDEX `cpf_UNIQUE` (cpf ASC),
  UNIQUE INDEX `rg_UNIQUE` (rg ASC));
  
-- -----------------------------------------------
  

-- -----------------------------------------------
-- criar a tabela de contato

CREATE TABLE bicJrGuiaEstiloV2.tbContato (
  cdContato INT NOT NULL AUTO_INCREMENT,
  telefone VARCHAR(14) NOT NULL,
  observacao VARCHAR(50) NOT NULL,
  cdCliente INT NOT NULL,
  PRIMARY KEY (cdContato),
  FOREIGN KEY (cdCliente) REFERENCES tbCliente(cdCliente));
  
-- -----------------------------------------------


-- -----------------------------------------------
-- criar a tabela de usuário

CREATE TABLE bicJrGuiaEstiloV2.tbUsuario (
  cdUsuario INT NOT NULL AUTO_INCREMENT,
  login VARCHAR(256) NOT NULL,
  pessoa VARCHAR(256) NOT NULL,
  senha VARCHAR(256) NOT NULL,
  tipo INT NOT NULL,
  PRIMARY KEY (cdUsuario),
  UNIQUE INDEX `login_UNIQUE` (login ASC));
  
-- -----------------------------------------------


-- -----------------------------------------------
-- criar a tabela de serviço

CREATE TABLE bicJrGuiaEstiloV2.tbServico (
  cdServico INT NOT NULL AUTO_INCREMENT,
  dtServico DATE NOT NULL,
  nmServico VARCHAR(256) NOT NULL,
  valor DOUBLE,
  horario VARCHAR(256),
  observacao VARCHAR(256),
  PRIMARY KEY (cdServico));
  
-- -----------------------------------------------
  
 
-- -----------------------------------------------
-- inserir cidades

INSERT INTO bicJrGuiaEstiloV2.tbcidade (nmCidade) VALUES ('Fabriciano');
INSERT INTO bicJrGuiaEstiloV2.tbcidade (nmCidade) VALUES ('Ipatinga');
INSERT INTO bicJrGuiaEstiloV2.tbcidade (nmCidade) VALUES ('Timóteo');
  
-- -----------------------------------------------


-- -----------------------------------------------
-- inserir clientes

INSERT INTO bicJrGuiaEstiloV2.tbcliente (nmCliente, sexo, dtNasc, cpf, rg, email, rua, numero, bairro, cep, cdCidade, telefone, observacao) VALUES ('Aline Maria Machado', 'f', '1980-01-01', '077.113.170-42', '20.824.983-7', 'aline.mm@cefetmg.br', 'Travessa José Fernandes Rosa', '402', 'Elpídio Volpini', '29309-707', '1', '(47)99973-2739', 'Celular');
INSERT INTO bicJrGuiaEstiloV2.tbcliente (nmCliente, sexo, dtNasc, cpf, rg, email, rua, numero, complemento, bairro, cep, cdCidade, telefone, observacao) VALUES ('Breno Ribeiro Fernandes', 'm', '1981-02-02', '525.925.140-74', '31.560.110-3', 'breno.rf@cefetmg.br', 'Rua Vasconcelos Drumond', '151', 'Ap. 305', 'Vila São José', '38067-035', '2', '(63)99805-6898', 'Celular');
INSERT INTO bicJrGuiaEstiloV2.tbcliente (nmCliente, sexo, dtNasc, cpf, rg, email, rua, numero, bairro, cep, cdCidade, telefone, observacao) VALUES ('Cíntia Souza Ferreira', 'f', '1982-03-03', '843.949.790-30', '20.205.220-5', 'cintia.sf@cefetmg.br', 'Quadra EQ 6/8 Bloco A', '15', 'Setor Leste (Gama)', '72450-068', '3', '(81)98432-6762', 'Celular');
INSERT INTO bicJrGuiaEstiloV2.tbcliente (nmCliente, sexo, dtNasc, cpf, rg, email, rua, numero, bairro, cep, cdCidade, telefone, observacao) VALUES ('Diego Araújo Oliveira', 'm', '1983-04-04', '816.865.560-55', '22.087.096-2', 'diego.ao@cefetmg.br', 'Travessa Lira', '36', 'Alecrim', '59030-205', '1', '(48)98725-0465', 'Celular');
INSERT INTO bicJrGuiaEstiloV2.tbcliente (nmCliente, sexo, dtNasc, cpf, rg, email, rua, numero, complemento, bairro, cep, cdCidade, telefone, observacao) VALUES ('Emily Castro Gomes', 'f', '1984-05-05', '821.501.600-64', '26.078.905-7', 'emily.cg@cefetmg.br', 'Avenida Pantaneira', '45', 'Ap. 186', 'Marajoara', '78138-164', '2', '(75)98155-8579', 'Celular');
INSERT INTO bicJrGuiaEstiloV2.tbcliente (nmCliente, sexo, dtNasc, cpf, rg, email, rua, numero, bairro, cep, cdCidade, telefone, observacao) VALUES ('Flávio Martins Carvalho', 'm', '1985-06-06', '108.300.450-66', '47.584.390-3', 'flavio.mc@cefetmg.br', 'Praça Nossa Senhora da Penha', '19', 'Alto Vila Nova', '29707-402', '3', '(79)98833-8870', 'Celular');
INSERT INTO bicJrGuiaEstiloV2.tbcliente (nmCliente, sexo, dtNasc, cpf, rg, email, rua, numero, bairro, cep, cdCidade, telefone, observacao) VALUES ('Emily Souza Moreira', 'f', '1986-07-07', '666.254.360-89', '20.478.306-9', 'emily.sm@cefetmg.br', 'Rua Odorico Zanini', '302', 'Efapi', '89809-851', '1', '(31)98582-0849', 'Celular');
INSERT INTO bicJrGuiaEstiloV2.tbcliente (nmCliente, sexo, dtNasc, cpf, rg, email, rua, numero, complemento, bairro, cep, cdCidade, telefone, observacao) VALUES ('Breno Ribeiro Martins', 'm', '1987-08-08', '966.520.080-17', '44.618.188-2', 'breno.rm@cefetmg.br', 'Rua das Caviúnas', '217', 'Ap. 904', 'Setor Comercial', '78550-099', '2', '(79)99421-1852', 'Celular');
INSERT INTO bicJrGuiaEstiloV2.tbcliente (nmCliente, sexo, dtNasc, cpf, rg, email, rua, numero, bairro, cep, cdCidade, telefone, observacao) VALUES ('Aline Clarice Araújo', 'f', '1988-09-09', '522.696.930-94', '43.872.872-5', 'aline.ca@cefetmg.br', 'Rua 5', '89', 'Conjunto Raul Balduino', '75123-220', '3', '(95)98484-0919', 'Celular');
INSERT INTO bicJrGuiaEstiloV2.tbcliente (nmCliente, sexo, dtNasc, cpf, rg, email, rua, numero, bairro, cep, cdCidade, telefone, observacao) VALUES ('Aline Maria Oliveira', 'f', '1989-10-10', '595.718.930-80', '22.215.760-4', 'aline.mo@cefetmg.br', 'Quadra X', '74', 'Portal da Alegria', '64037-400', '1', '(66)99324-1703', 'Celular');
INSERT INTO bicJrGuiaEstiloV2.tbcliente (nmCliente, sexo, dtNasc, cpf, rg, email, rua, numero, complemento, bairro, cep, cdCidade, telefone, observacao) VALUES ('Flávio Martins Souza', 'm', '1980-11-11', '258.134.980-87', '39.620.792-3', 'flavio.ms@cefetmg.br', 'Travessa Apóstolo São Mateus', '56', 'Ap. 421', 'Jardim Manguinhos', '58103-456', '2', '(86)98526-1545', 'Celular');
INSERT INTO bicJrGuiaEstiloV2.tbcliente (nmCliente, sexo, dtNasc, cpf, rg, email, rua, numero, bairro, cep, cdCidade, telefone, observacao) VALUES ('Diego Rodrigues Santos', 'm', '1981-12-12', '106.593.350-93', '43.587.069-5', 'diego.rs@cefetmg.br', 'Rua Professor Mário de Castro', '90', 'Boa Viagem', '51030-260', '3', '(92)99931-0046', 'Celular');

-- -----------------------------------------------


-- -----------------------------------------------
-- inserir usuários

INSERT INTO bicJrGuiaEstiloV2.tbusuario (login, pessoa, senha, tipo) VALUES ('secretaria', 'Violeta', 'secretaria', 1);
INSERT INTO bicJrGuiaEstiloV2.tbusuario (login, pessoa, senha, tipo) VALUES ('gerente', 'Margarida', 'gerente', 2);
  
-- -----------------------------------------------


-- -----------------------------------------------
-- inserir serviços

INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-01-30', 'Corte', 59.00, 'Beatriz', '');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-01-31', 'Maquiagem', 69.00, 'Carlos', 'Observação do serviço');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-02-14', 'Manicure', 79.00, 'Regina', '');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-02-14', 'Pedicure', 89.00, 'Karen', 'Observação do serviço');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-03-26', 'Pintura', 99.00, 'Ana', '');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-03-28', 'Corte', 59.00, 'Bruno', 'Observação do serviço');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-03-30', 'Maquiagem', 69.00, 'Cecília', '');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-03-31', 'Manicure', 79.00, 'Rita', 'Observação do serviço');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-04-11', 'Pedicure', 89.00, 'Kiara', '');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-05-05', 'Pintura', 99.00, 'Alice', 'Observação do serviço');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-05-07', 'Corte', 59.00, 'Maria', '');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-05-16', 'Maquiagem', 69.00, 'Vilma', 'Observação do serviço');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-05-25', 'Manicure', 79.00, 'Taís', '');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-05-26', 'Pedicure', 89.00, 'Nádia', 'Observação do serviço');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-05-30', 'Pintura', 99.00, 'Arnaldo', '');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-06-07', 'Corte', 59.00, 'Márcio', 'Observação do serviço');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-06-08', 'Maquiagem', 69.00, 'Vinícius', '');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-06-09', 'Manicure', 79.00, 'Tânia', 'Observação do serviço');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-06-10', 'Pedicure', 89.00, 'Nicole', '');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-06-11', 'Pintura', 99.00, 'Dalila', 'Observação do serviço');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-07-12', 'Corte', 59.00, 'Maurício', '');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-08-18', 'Maquiagem', 69.00, 'Vitória', 'Observação do serviço');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-09-09', 'Manicure', 79.00, 'Tereza', '');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-09-17', 'Pedicure', 89.00, 'Nilce', 'Observação do serviço');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-10-09', 'Pintura', 99.00, 'Daniel', '');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-10-10', 'Corte', 59.00, 'Beatriz', 'Observação do serviço');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-11-16', 'Maquiagem', 69.00, 'Vilma', '');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-11-23', 'Manicure', 79.00, 'Regina', 'Observação do serviço');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-12-18', 'Pedicure', 89.00, 'Karen', '');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-12-09', 'Pintura', 99.00, 'Ana', 'Observação do serviço');
INSERT INTO bicJrGuiaEstiloV2.tbservico (dtServico, nmServico, valor, horario, observacao) VALUES ('2018-12-17', 'Maquiagem', 99.00, 'Carlos', '');

-- -----------------------------------------------