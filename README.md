# 🎬 IndicaAI

> API REST para recomendação de filmes com inteligência artificial, autenticação JWT e integração com banco de dados PostgreSQL.

🌐 **Acesse o projeto:** [https://indicaai-lemon.vercel.app/](https://indicaai-lemon.vercel.app/)

---

## 📋 Sobre o Projeto

O **IndicaAI** é uma API desenvolvida em **Java com Spring Boot** que oferece recomendações personalizadas de filmes utilizando IA. O sistema conta com autenticação segura via **JWT**, persistência de dados com **PostgreSQL** e migrações gerenciadas pelo **Flyway**.

---

## 🚀 Tecnologias Utilizadas

| Tecnologia | Versão |
|---|---|
| Java | 17 |
| Spring Boot | 3.3.6 |
| Spring Security | - |
| Spring Data JPA | - |
| Spring WebFlux | - |
| PostgreSQL | - |
| Flyway | - |
| JWT (jjwt + auth0) | 0.12.3 / 4.4.0 |
| Lombok | - |
| Maven | - |
| Docker | - |

---

## 🏗️ Arquitetura

```
indicaAI/
├── src/
│   ├── main/
│   │   ├── java/com/indicaAI/
│   │   │   ├── controller/     # Endpoints REST
│   │   │   ├── service/        # Regras de negócio
│   │   │   ├── repository/     # Acesso ao banco de dados
│   │   │   ├── model/          # Entidades JPA
│   │   │   ├── dto/            # Data Transfer Objects
│   │   │   ├── security/       # Configurações de segurança e JWT
│   │   │   └── config/         # Configurações gerais
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/   # Scripts Flyway
│   └── test/
├── Dockerfile
├── pom.xml
└── mvnw
```

---

## ⚙️ Pré-requisitos

Antes de rodar o projeto, certifique-se de ter instalado:

- [Java 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven 3.8+](https://maven.apache.org/download.cgi)
- [PostgreSQL](https://www.postgresql.org/download/)
- [Docker](https://www.docker.com/) *(opcional)*

---

## 🔧 Configuração

### 1. Clone o repositório

```bash
git clone https://github.com/MatheusFreiresDev/indicaAI.git
cd indicaAI
```

### 2. Configure o banco de dados

Crie um banco de dados PostgreSQL e configure as variáveis no `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/indicaai
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true

jwt.secret=sua_chave_secreta_jwt
```

### 3. Execute a aplicação

```bash
./mvnw spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

---

## 🐳 Rodando com Docker

### Build da imagem

```bash
docker build -t indicaai .
```

### Executar o container

```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/indicaai \
  -e SPRING_DATASOURCE_USERNAME=seu_usuario \
  -e SPRING_DATASOURCE_PASSWORD=sua_senha \
  indicaai
```

---

## 🔐 Autenticação

A API utiliza **JWT (JSON Web Token)** para autenticação. Para acessar os endpoints protegidos:

1. Registre um usuário via `POST /auth/register`
2. Faça login via `POST /auth/login`
3. Utilize o token retornado no header das requisições:

```
Authorization: Bearer <seu_token_jwt>
```

---

## 📡 Endpoints Principais

| Método | Endpoint | Descrição | Auth |
|---|---|---|---|
| `POST` | `/auth/register` | Cadastro de usuário | ❌ |
| `POST` | `/auth/login` | Login e geração de token | ❌ |
| `GET` | `/filmes/recomendar` | Recomendações de filmes via IA | ✅ |
| `GET` | `/filmes` | Lista filmes disponíveis | ✅ |
| `POST` | `/filmes` | Cadastra um filme | ✅ |

> ⚠️ Consulte a documentação completa da API para ver todos os endpoints disponíveis.

---

## 🧪 Testes

```bash
./mvnw test
```

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 👨‍💻 Autor

Desenvolvido por **Matheus Freires**

[![GitHub](https://img.shields.io/badge/GitHub-MatheusFreiresDev-181717?style=flat&logo=github)](https://github.com/MatheusFreiresDev)
