# 🗣️ Cursos de Idiomas API

Este projeto foi desenvolvido como parte do curso **Web Developer Java**, com o objetivo de implementar uma API REST para o gerenciamento de alunos e turmas de uma escola de idiomas, praticando os seguintes conceitos:

- Desenvolvimento de API REST com Spring Boot  
- Arquitetura em camadas com DDD (Domain-Driven Design)  
- Persistência de dados com Spring Data JPA (usando Code First via Migrations)  
- Integração com banco de dados SQL Server  
- Validações com Jakarta Bean Validation  
- Boas práticas de modelagem, versionamento de API e aplicação de regras de negócio  

---

### 🧠 Regras de Negócio

- Aluno deve ser cadastrado com **pelo menos uma turma**
- **E-mail e CPF** devem ser válidos
- Aluno **pode se matricular em várias turmas**, mas **não mais de uma vez na mesma**
- Cada turma pode ter **no máximo 5 alunos**
- Aluno **não pode ser excluído** se estiver associado a uma turma
- Turma **não pode ser excluída** se possuir alunos

---

### 🛠 Tecnologias utilizadas

- Java 21  
- Spring Boot 3.5.x 
- Spring Data JPA  
- SQL Server  
- Maven  
- SpringDoc OpenAPI (Swagger)  
- Jakarta Bean Validation  
- Docker + Docker Compose *(opcional para SQL Server)*

---

### 📦 Entidades principais

#### 👤 Aluno

- `id`: UUID  
- `nome`: String (obrigatório)  
- `cpf`: String (formato válido e único)  
- `email`: String (formato válido e único)  
- `turmas`: Lista de turmas associadas (obrigatório pelo menos 1)

#### 🏫 Turma

- `id`: UUID  
- `numero`: Integer (identificador da turma)  
- `anoLetivo`: Integer  
- `alunos`: Lista de alunos (limite de 5)

---

### 🔄 Endpoints principais (Swagger)

A API possui documentação interativa disponível via Swagger:

> 🔗 Acesse: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

#### 👨‍🎓 Alunos

- `POST /api/v1/alunos` – Cadastrar novo aluno  
- `GET /api/v1/alunos` – Listar todos os alunos  
- `GET /api/v1/alunos/{id}` – Buscar aluno por ID  
- `PUT /api/v1/alunos/{id}` – Atualizar dados do aluno  
- `DELETE /api/v1/alunos/{id}` – Excluir aluno (somente se não estiver em turma)
- `GET /api/v1/alunos/{alunoId}/turma/{turmaId}` – Listar todos os alunos da turma

#### 🏫 Turmas

- `POST /api/v1/turmas` – Cadastrar nova turma  
- `GET /api/v1/turmas` – Listar todas as turmas  
- `GET /api/v1/turmas/{id}` – Buscar turma por ID  
- `PUT /api/v1/turmas/{id}` – Atualizar turma  
- `DELETE /api/v1/turmas/{id}` – Excluir turma (somente se estiver vazia)
- `GET /api/v1/turmas/{turmaId}/aluno/{alunoId}` – Listar todas as turmas do aluno

#### 👩‍🏫 Secretaria

- `POST /api/v1/secretaria/matricular/turma/{turmaId}/aluno/{alunoId}` – Matricular aluno em uma turma
- `DELETE /api/v1/secretaria/desmatricular/turma/{turmaId}/aluno/{alunoId}` – Remover aluno de uma turma

---

### ▶️ Como executar o projeto

#### 1. Clonar o repositório
```bash
git clone https://github.com/chiarelli/CursoIdiomasAPI.git
cd api-idiomas
```
#### 2. Executar o banco SQL Server (via docker)

```bash
docker-compose -f docker-compose.dev.yml up -d
```

#### 3. Executar os testes (opcional)
Lembrando que o container precisa estar `ready` para realizar os testes.
```bash
./mvnw test
```

#### 4. Executar a aplicação
No terminal (ou pela IDE):

```bash
./mvnw spring-boot:run
```
---

### ☑️ Status atual

- ✅ Projeto inicial com Spring Boot configurado

- ✅ Entidades Aluno e Turma com relacionamento

- [ ] Regras de negócio implementadas

- [ ] Rotas da API Rest implementadas

- [ ] Documentação via Swagger

- ✅ Validações com Jakarta Bean Validation

- ✅ Integração com SQL Server

- [ ] Testes automatizados (em desenvolvimento)

---
###### 👨‍💻 Autor
> Feito por Raphael Mathias Chiarelli Gomes durante o curso de Spring Boot Web Developer na [COTI](https://www.cotiinformatica.com.br/curso/java).

