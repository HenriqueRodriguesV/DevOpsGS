# ORBIT API - DevOps Tools & Cloud Computing

## Descrição da solução

A ORBIT API é uma aplicação Java Spring Boot voltada ao contexto da Global Solution 2026/1, conectando exploração espacial, dados e soluções aplicáveis a problemas reais na Terra.

## Arquitetura macro

Inserir aqui a imagem `arquitetura/arquitetura-macro.png`.

Fluxo:
Usuário/Postman/Swagger -> Container orbit-api -> Network Docker orbit-net -> Container orbit-db H2 -> Volume orbit-h2-data.

## Tecnologias utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- Spring Security
- Swagger/OpenAPI
- H2 Database
- Docker
- Docker Compose

## Como executar o projeto

### 1. Clonar o repositório

```bash
git clone LINK_DO_REPOSITORIO_DEVOPS
cd orbit-devops
```

### 2. Subir os containers em background

```bash
docker compose up -d --build
```

### 3. Verificar os containers rodando

```bash
docker compose ps
```

### 4. Exibir logs dos dois containers

```bash
docker compose logs -f orbit-api
docker compose logs -f orbit-db
```

### 5. Entrar nos containers

```bash
docker container exec -it orbit-api-rm562917 sh
docker container exec -it orbit-db-rm562917 sh
```

### 6. Mostrar `pwd`, `ls -l` e `whoami`

```bash
docker container exec -it orbit-api-rm562917 pwd
docker container exec -it orbit-api-rm562917 ls -l
docker container exec -it orbit-api-rm562917 whoami

docker container exec -it orbit-db-rm562917 pwd
docker container exec -it orbit-db-rm562917 ls -l
docker container exec -it orbit-db-rm562917 whoami
```

### 7. Acessar Swagger

- `http://localhost:8080/swagger-ui.html`

### 8. Acessar H2 Console

- `http://localhost:8082`
- JDBC URL: `jdbc:h2:tcp://localhost:9092/./orbitdb`

### 9. Evidência de persistência com `SELECT`

```bash
docker container exec -it orbit-db-rm562917 sh -c 'java -cp h2.jar org.h2.tools.Shell -url jdbc:h2:tcp://localhost:9092/./orbitdb -user sa -password "" -sql "select count(*) from tb_orbit_usuario"'
```

### 10. Executar testes

```bash
cd orbit-api
mvn test
```

## Observações

- O projeto usa variáveis de ambiente para banco e JWT.
- A imagem da arquitetura macro deve ser adicionada na pasta `arquitetura/`.
