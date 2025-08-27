create table tarefas_tb(
    id integer(50) NOT NULL AUTO_INCREMENT,
    titulo varchar(100) NOT NULL,
    descricao varchar(500) NOT NULL,
    status varchar(15),
    prioridade varchar(15),
    categoria varchar(50) NOT NULL,
    tags varchar(200),
    observacoes varchar(1000),
    PRIMARY KEY (`id`)
);