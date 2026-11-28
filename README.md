# Sistema de Gestão de Plantas Industriais — Projeto Integrador Full Stack (ESEG 2S2026)

Aplicação web full stack desenvolvida em **Java 17 + Spring Boot 3.3.5** para modernização da base legada `Plantas_Industriais.csv`, contemplando normalização relacional (3FN), máquina de estados auditada, gestão de linhas de produção (JPA 1:N), autenticação e autorização no servidor (Spring Security) e interface responsiva com Bootstrap 5 + Thymeleaf.

---

## Tecnologias Utilizadas

- **Backend:** Java 17, Spring Boot 3.3.5 (Spring MVC, Spring Data JPA, Spring Security, Bean Validation)
- **Persistência:** Hibernate/JPA + H2 Database em modo arquivo (`./data/plantasdb`, com driver PostgreSQL incluído)
- **Frontend:** HTML5, CSS3, Bootstrap 5.3, Bootstrap Icons, Thymeleaf + `thymeleaf-extras-springsecurity6`
- **Testes:** JUnit 5, Mockito, Spring Boot Test (`MockMvc`), Spring Security Test

---

## Como Executar o Projeto

### Pré-requisitos
- JDK **17** ou **21** instalado (`java -version`)
- Maven **3.9+** (ou Maven embutido do **IntelliJ IDEA**)

### Opção 1: Pelo IntelliJ IDEA (Mais rápido)
1. Extraia o projeto e abra a pasta `projeto-plantas-industriais` no IntelliJ IDEA (`File -> Open -> selecione o pom.xml`).
2. Aguarde o Maven baixar as dependências automaticamente.
3. Abra `src/main/java/br/edu/eseg/plantas/PlantasIndustriaisApplication.java` e clique em **Run**.
4. Acesse no navegador: **http://localhost:8080**

### Opção 2: Pelo Terminal
```bash
# Executar todos os testes automatizados
mvn test

# Iniciar a aplicação web
mvn spring-boot:run
```

---

## Credenciais de Acesso (Perfis Distintos)

| Perfil | Usuário | Senha | Permissões no Servidor |
|---|---|---|---|
| **Gestor Industrial (`ROLE_ADMIN`)** | `admin` | `admin123` | Dashboard, catálogo, criar/editar/excluir plantas, transições de status auditadas, gestão total de linhas, tela `/auditoria` e `/h2-console` |
| **Operador (`ROLE_OPERADOR`)** | `operador` | `operador123` | Dashboard, catálogo com filtros, detalhes da planta e cadastro/alternância de linhas de produção (recebe **403 Forbidden** em rotas administrativas) |

### Console do Banco H2 (opcional, perfil ADMIN)
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:file:./data/plantasdb`
- User Name: `sa` | Password: *(vazio)*

---

## Estrutura de Artefatos

- `docs/Plantas_Industriais_legado.csv` — Base legada original
- `docs/RELATORIO_TECNICO.md` — Diagnóstico legado, matriz de rastreabilidade, diagramas ER (legado e evoluído), diagrama de classes, ADRs, cobertura de testes e guia da defesa oral
- `docs/sql/01_modelo_legado.sql` — Esquema original reconstruído
- `docs/sql/02_modelo_evoluido.sql` — Esquema evoluído (3FN, constraints e índices)
- `docs/sql/03_migracao_dados_legado.sql` — Script SQL de saneamento e migração dos 10 registros

