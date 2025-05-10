# 🛠️ Comments API (Back-end)

Este repositório representa o **back-end** da aplicação fullstack [Interactive Comments](https://interactive-comments-theta-seven.vercel.app/login), um sistema robusto de comentários com autenticação, likes, replies e integrações modernas.

> 📄 Repositório do Front-end:
> [https://github.com/K1dou/interactive-comments-front](https://github.com/K1dou/interactive-comments-front)

> 📖 Documentação via Swagger UI:
> [https://comments-api-c43806001036.herokuapp.com/swagger-ui/index.html](https://comments-api-c43806001036.herokuapp.com/swagger-ui/index.html)

---

## 🚀 Tecnologias e Ferramentas

* **Java 17** + **Spring Boot 3**
* **Spring Security** (JWT + OAuth2 com Google)
* **Spring Data JPA** com **PostgreSQL**
* **Spring Validation** (validações em DTOs)
* **Swagger/OpenAPI** para documentação
* **Cloudinary** para upload de imagens de avatar
* **Redis** para contagem de likes (alta performance)
* **Testes unitários** com **JUnit** e **Mockito**
* Arquitetura MVC: `Controller`, `Service`, `Repository`, `DTO`
* Tratamento de exceções global via `@ControllerAdvice`
* CORS configurado para ambiente local e produção

---

## ✅ Funcionalidades principais

### 🔐 Autenticação e Segurança

* Login com e-mail e senha (JWT)
* Login social com Google (OAuth2)
* Geração e renovação de **access token** e **refresh token**


### 📃 Comentários

* Criação de comentários e respostas em níveis
* Paginação com ordenação por `createdAt`
* Likes com controle via Redis e botão toggle

### 👤 Usuário

* Cadastro com upload de avatar via Cloudinary
* Validação de dados em DTOs (nome, email, senha...)
* Retorno de dados seguros via `AuthorDTO`
* Upload opcional de imagem com `multipart/form-data`

---

## 🧪 Testes Automatizados

* Testes de serviço e repositório com `@DataJpaTest` e `Mockito`
* Validações nos controllers com cobertura de cenários de erro

---

## 🌐 Swagger UI

A documentação da API está disponível no Swagger para exploração e testes interativos:

➡️ [https://comments-api-c43806001036.herokuapp.com/swagger-ui/index.html](https://comments-api-c43806001036.herokuapp.com/swagger-ui/index.html)

---

## 🧱 Estrutura do Projeto

```
src/
├── config/              # Segurança e configurações globais
├── controller/          # Controllers REST
├── dto/                 # Data Transfer Objects (entrada e saída)
├── exception/           # Handler global de exceções
├── model/               # Entidades JPA
├── repository/          # Interfaces JPA e custom queries
├── security/            # Filtro JWT, OAuth2 handler
├── service/             # Regra de negócio

```

---

## 📦 Endpoints principais

* `POST /users/createUser` – Cria um novo usuário
* `POST /users/login` – Login tradicional (JWT)
* `POST /users/refresh` – Gera novo access token
* `GET /users/me` – Retorna dados do usuário autenticado
* `GET /comments` – Lista comentários paginados
* `POST /comments/{id}/like` – Like em um comentário
* `POST /comments/{id}/unlike` – Unlike

Veja todos os endpoints no Swagger!

---

## 🏗️ Como rodar localmente

1. Clone o repositório:

```bash
git clone https://github.com/K1dou/Fullstack-project-comments.git
```

2. Configure seu `.env` ou `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/comments_db
spring.datasource.username=seu_user
spring.datasource.password=sua_senha
JWT_SECRET=seu_segredo
CLOUDINARY_URL=...
```

3. Rode a aplicação:

```bash
./mvnw spring-boot:run
```

---

## 💼 Licença

Este projeto está licenciado sob a Licença MIT. Sinta-se à vontade para usar, aprender e contribuir.

---

Desenvolvido com foco em segurança, escalabilidade e boas práticas — por [K1dou](https://github.com/K1dou) ✨
