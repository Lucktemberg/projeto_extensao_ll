# Solidário

Sistema web de gerenciamento de doações, desenvolvido em Spring Boot para o projeto de extensão.

A versão estável atual está no commit `a043e97` da branch `feature/modernizacao-ui`.

---

## O que o sistema faz

- Cadastro de doadores, receptores, doações, estoque e distribuições
- Campanhas com meta e progresso por item
- Acompanhamento público da doação pelo código (ex.: `SOL-XXXX`)
- Distribuição com destinatário, endereço e quantidade
- Dashboard com resumo operacional
- Relatório mensal exportável em **PDF** e **CSV**
- Login com sessão, CSRF e permissões por perfil

Telas públicas: `/login` e `/acompanhar`.  
Área interna: `/menu` (dashboard) e as demais telas do sistema.

---

## Tecnologias

- Java 21
- Spring Boot 4.0.8
- Spring Security (sessão + cookie CSRF)
- Spring Data JPA / Hibernate
- Thymeleaf
- PostgreSQL (Railway)
- OpenPDF
- Maven

---

## Como rodar

### Requisitos

- JDK 21
- Maven
- Senha do banco em variável de ambiente

### Variáveis

A senha **não** fica no repositório. Exporte antes de subir o sistema:

```bash
export SPRING_DATASOURCE_PASSWORD=sua_senha
```

Opcionais:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://host:porta/banco
export SPRING_DATASOURCE_USERNAME=postgres
export SERVER_PORT=8080
```

### Executar

```bash
mvn spring-boot:run
```

Ou, no IntelliJ, rode a classe `com.doacoes.App`.

Acesse: [http://localhost:8080](http://localhost:8080)

### Testes

```bash
mvn test
```

---

## Arquitetura

```txt
src/main/java/com/doacoes
├── App.java
├── config        # segurança, CSRF, autenticação
├── controller    # telas e APIs REST
├── dto
├── entity
├── repository
├── service
└── util
```

Fluxo:

```txt
Thymeleaf / JS  →  Controller  →  Service  →  Repository  →  PostgreSQL
```

---

## Principais rotas

| Área | Tela | API |
| --- | --- | --- |
| Login | `/login` | `POST /usuarios/autenticar` |
| Dashboard | `/menu` | `GET /dashboard/resumo` |
| Doações | `/tela-doacoes` | `/doacoes` |
| Campanhas | `/tela-campanhas` | `/campanhas` |
| Distribuições | `/tela-distribuicoes` | `/distribuicoes` |
| Relatórios | `/tela-relatorios` | `/relatorios/mensal`, `.csv`, `.pdf` |
| Acompanhar | `/acompanhar` | `GET /doacoes/codigo/{codigo}` |

---

## Segurança

- Autenticação por sessão
- CSRF via cookie `XSRF-TOKEN` e header `X-XSRF-TOKEN`
- Cookie de sessão `HttpOnly` e `SameSite=lax`
- Perfis: `ROLE_ADMIN` e permissões por módulo (`DOACAO`, `ESTOQUE`, `DISTRIBUICAO`, etc.)
- Senha do banco somente em `SPRING_DATASOURCE_PASSWORD`

---

## Git

Branch desta versão estável: `feature/modernizacao-ui`.

```bash
git checkout feature/modernizacao-ui
git pull
```

Não commitar:

- `target/`
- `.idea/`
- `.env`
- senhas ou arquivos de credencial
