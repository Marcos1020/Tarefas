create table tarefas_tb(
    id integer(50) NOT NULL AUTO_INCREMENT,
    titulo varchar(100) NOT NULL,
    descricao varchar(500) NOT NULL,
    status varchar(15),
    prioridade varchar(15),
    dataCriacao TIMESTAMP,
    dataAtualizacao TIMESTAMP,
    dataConclusao TIMESTAMP,
    usuarioResponsavel varchar(500),
    categoria varchar(50) NOT NULL,
    tags varchar(200),
    estimativaHoras integer(50),
    tempoRealHoras integer(50),
    observacoes varchar(1000),
    PRIMARY KEY (`id`)
);