# 🚀 API de Gerenciamento de Tarefas

Sistema completo de gerenciamento de tarefas desenvolvido com Spring Boot, seguindo as melhores práticas de programação orientada a objetos e padrão MVC.

## ✨ Características

- **Arquitetura MVC**: Separação clara entre Model, View (API) e Controller
- **Padrões OO**: Encapsulamento, herança, polimorfismo e interfaces bem definidas
- **API REST**: Endpoints RESTful com documentação Swagger/OpenAPI
- **Validações**: Validação de dados com Bean Validation
- **Tratamento de Erros**: Exception handling global e personalizado
- **Testes**: Testes unitários e de integração abrangentes
- **Documentação**: Swagger UI para testes da API
- **Banco de Dados**: MySQL com JPA/Hibernate

## 🏗️ Arquitetura

```
src/main/java/com/documents/tarefas/
├── controller/          # Camada de apresentação (REST)
├── service/            # Camada de lógica de negócio
├── repository/         # Camada de acesso a dados
├── model/              # Entidades JPA
├── dto/                # Objetos de transferência
├── exception/          # Exceções personalizadas
└── config/             # Configurações (Swagger, etc.)
```

## 🚀 Como Executar

### Pré-requisitos
- Java 17+
- Maven/Gradle
- MySQL 8.0+
- Docker (opcional)

### 1. Clonar o repositório
```bash
git clone <url-do-repositorio>
cd tarefas
```

### 2. Configurar banco de dados
```bash
# Usando Docker
docker-compose up -d

# Ou configurar MySQL manualmente
# Criar banco 'Tarefas' e usuário 'root' com senha 'root'
```

### 3. Executar a aplicação
```bash
# Com Gradle
./gradlew bootRun

# Com Maven
./mvnw spring-boot:run
```

### 4. Acessar a API
- **API**: http://localhost:9293/api/tarefas
- **Swagger UI**: http://localhost:9293/swagger-ui.html
- **Health Check**: http://localhost:9293/api/tarefas/health

## 📚 Endpoints da API

### 🔍 Consultas
- `GET /api/tarefas` - Listar tarefas com paginação e filtros
- `GET /api/tarefas/{id}` - Buscar tarefa por ID
- `GET /api/tarefas/status/{status}` - Buscar por status
- `GET /api/tarefas/prioridade/{prioridade}` - Buscar por prioridade
- `GET /api/tarefas/usuario/{usuario}` - Buscar por usuário
- `GET /api/tarefas/busca?texto={texto}` - Buscar por texto
- `GET /api/tarefas/vencidas` - Buscar tarefas vencidas
- `GET /api/tarefas/estatisticas` - Obter estatísticas

### ✏️ Operações
- `POST /api/tarefas` - Criar nova tarefa
- `PUT /api/tarefas/{id}` - Atualizar tarefa
- `PATCH /api/tarefas/{id}/concluir` - Marcar como concluída
- `PATCH /api/tarefas/{id}/andamento` - Marcar como em andamento
- `PATCH /api/tarefas/{id}/pendente` - Marcar como pendente
- `DELETE /api/tarefas/{id}` - Excluir tarefa

## 🎯 Funcionalidades

### Status das Tarefas
- **PENDENTE**: Tarefa aguardando início
- **EM_ANDAMENTO**: Tarefa sendo executada
- **CONCLUIDA**: Tarefa finalizada
- **CANCELADA**: Tarefa cancelada
- **PAUSADA**: Tarefa temporariamente pausada

### Prioridades
- **BAIXA**: Prioridade baixa
- **MEDIA**: Prioridade média (padrão)
- **ALTA**: Prioridade alta
- **URGENTE**: Prioridade máxima

### Filtros e Busca
- Paginação com ordenação
- Filtros por status, prioridade, usuário e categoria
- Busca por texto no título e descrição
- Ordenação por prioridade e data de criação

## 🧪 Executando os Testes

```bash
# Executar todos os testes
./gradlew test

# Executar testes específicos
./gradlew test --tests TarefaServiceTest

# Executar com cobertura
./gradlew test jacocoTestReport
```

## 📊 Exemplos de Uso

### Criar uma nova tarefa
```bash
curl -X POST http://localhost:9293/api/tarefas \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Implementar API REST",
    "descricao": "Desenvolver endpoints para gerenciamento de tarefas",
    "prioridade": "ALTA",
    "usuarioResponsavel": "João Silva",
    "categoria": "Desenvolvimento",
    "estimativaHoras": 8
  }'
```

### Listar tarefas com filtros
```bash
curl "http://localhost:9293/api/tarefas?status=PENDENTE&prioridade=ALTA&page=0&size=10"
```

### Marcar tarefa como concluída
```bash
curl -X PATCH http://localhost:9293/api/tarefas/1/concluir
```

## 🔧 Configurações

### application.yml
```yaml
server:
  port: 9293

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/Tarefas
    username: root
    password: root
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

### Variáveis de Ambiente
```bash
export PORT=9293
export DB_HOST=localhost
export DB_NAME=Tarefas
export DB_USER=root
export DB_PASS=root
```

## 📈 Monitoramento

### Health Check
```bash
curl http://localhost:9293/api/tarefas/health
```

### Estatísticas
```bash
curl http://localhost:9293/api/tarefas/estatisticas
```

## 🚀 Deploy

### Heroku
```bash
# Configurar variáveis de ambiente
heroku config:set SPRING_PROFILES_ACTIVE=prod
heroku config:set DB_URL=jdbc:mysql://...

# Deploy
git push heroku main
```

### Docker
```bash
# Build da imagem
docker build -t tarefas-api .

# Executar container
docker run -p 9293:9293 tarefas-api
```

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 👨‍💻 Autor

**Marcos Vinicius Campos**
- Email: marcossanches710@gmail.com
- linkedin: https://www.linkedin.com/in/marcos-vinicius-campos-ab2a691a7/

## 🙏 Agradecimentos

- Spring Boot Team
- Hibernate Team
- MySQL Team
- Comunidade Java

---

⭐ **Se este projeto te ajudou, considere dar uma estrela!**
