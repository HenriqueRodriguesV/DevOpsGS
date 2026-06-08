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
cd DevOpsGS
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

## Testando o CRUD completo (com autenticação JWT)

Os endpoints de dados exigem token JWT. Fluxo: **registrar/login -> usar o token**.

```bash
# 1) Registrar e obter o token (campo "token" na resposta)
curl -X POST http://localhost:8080/api/auth/registrar \
  -H "Content-Type: application/json" \
  -d '{"nome":"Rep RM562917","email":"rep@orbit.com","senha":"orbit123"}'

# guarde o token
TOKEN="COLE_O_TOKEN_AQUI"

# 2) CREATE
curl -X POST http://localhost:8080/api/pessoas -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nome":"Astronauta Teste","matricula":"RM562917","funcao":"Comandante","nivelCondicaoFisica":9,"nivelHabilidade":8}'

# 3) READ
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/pessoas

# 4) UPDATE (use o id retornado no CREATE)
curl -X PUT http://localhost:8080/api/pessoas/1 -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nome":"Astronauta Atualizado","matricula":"RM562917","funcao":"Piloto","nivelCondicaoFisica":10,"nivelHabilidade":10}'

# 5) DELETE
curl -X DELETE -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/pessoas/1
```

Outros recursos: `/api/missoes`, `/api/veiculos`, `/api/recursos`, `/api/pontos-de-apoio` (ver Swagger).

## Como executar em nuvem (Azure)

Deploy em VM Linux Ubuntu via Azure CLI, região `northcentralus`. Há scripts em `scripts/`.

```bash
# automatizado (requer: az login)
bash scripts/azure-create-vm.sh        # cria RG/VM, abre portas 8080/8082/9092, instala Docker e sobe os containers
```

Passo a passo manual na VM:

```bash
ssh azureuser@IP_DA_VM
sudo apt update && sudo apt install -y git
curl -fsSL https://get.docker.com -o get-docker.sh && sudo sh get-docker.sh
sudo systemctl enable docker && sudo systemctl start docker
git clone https://github.com/HenriqueRodriguesV/DevOpsGS.git
cd DevOpsGS
sudo docker compose up -d --build
sudo docker compose ps
```

Portas liberadas na nuvem: 22 (SSH), 8080 (API/Swagger), 8082 (H2 Console), 9092 (H2 TCP).

Acessos em nuvem:
- Swagger: `http://IP_DA_VM:8080/swagger-ui.html`
- H2 Console: `http://IP_DA_VM:8082`

Remover os recursos da nuvem (evitar custos): `bash scripts/azure-delete-resources.sh`

## Roteiro do vídeo demonstrativo

1. Mostrar o repositório no GitHub e o README.
2. Na VM em nuvem: `git clone ...` e `cd DevOpsGS`.
3. `docker compose up -d --build`
4. `docker compose ps` (containers em background)
5. `docker compose logs orbit-api` e `docker compose logs orbit-db`
6. Acessar o Swagger em `http://IP_DA_VM:8080/swagger-ui.html`
7. CRUD pela API (registrar/login, CREATE, READ, UPDATE, DELETE)
8. `docker container exec` nos dois containers: `pwd`, `ls -l`, `whoami` (usuários não-root)
9. `SHOW TABLES` e `SELECT` direto no container do banco (persistência)
10. Explicar a arquitetura e mostrar que roda em nuvem (IP público)

## Checklist dos requisitos

- [x] App Java conteinerizada (Dockerfile, imagem personalizada)
- [x] Usuário não-root (orbituser) e WORKDIR (/app)
- [x] Porta 8080 exposta e variável de ambiente na app
- [x] Container app com RM (orbit-api-rm562917)
- [x] Banco H2 em container separado, servidor TCP
- [x] Volume nomeado (orbit-h2-data), portas 9092/8082, variável de ambiente
- [x] Container banco com RM (orbit-db-rm562917)
- [x] App e banco na mesma rede (orbit-net) via Docker Compose
- [x] Containers em background, logs, docker exec (pwd/ls/whoami)
- [x] CRUD completo persistindo em tabelas relacionadas (9 tabelas)
- [x] SELECT direto no container do banco
- [x] README com how-to, execução local e em nuvem
- [ ] Imagem arquitetura/arquitetura-macro.png (desenhar no Draw.io)
- [ ] Vídeo demonstrativo em nuvem
- [ ] PDF final (capa, RM 562917, nomes, link GitHub, link YouTube)

## Links da entrega

- GitHub: https://github.com/HenriqueRodriguesV/DevOpsGS
- Vídeo no YouTube: _preencher_
- Swagger em nuvem: `http://IP_DA_VM:8080/swagger-ui.html`
