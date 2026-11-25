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

---

## Roteiro Sugerido de Commits Progressivos no Git

Para evidenciar a evolução progressiva do projeto no repositório individual (requisito da seção 5.1.b do edital), crie o repositório local com commits organizados por etapa:

```bash
git init
git add pom.xml .gitignore docs/Plantas_Industriais_legado.csv docs/sql/
git commit -m "docs: diagnostico legado e scripts SQL do modelo original e evoluido"

git add src/main/java/br/edu/eseg/plantas/PlantasIndustriaisApplication.java src/main/java/br/edu/eseg/plantas/model/ src/main/resources/application.properties
git commit -m "feat(domain): entidades JPA normalizadas e maquina de estados StatusPlanta"

git add src/main/java/br/edu/eseg/plantas/dto/ src/main/java/br/edu/eseg/plantas/exception/ src/main/java/br/edu/eseg/plantas/repository/ src/main/java/br/edu/eseg/plantas/service/
git commit -m "feat(service): repositorios JPA, regras de negocio, validacoes e calculo de KPIs"

git add src/main/java/br/edu/eseg/plantas/config/ src/main/java/br/edu/eseg/plantas/controller/
git commit -m "feat(security-web): Spring Security RBAC, carga inicial legada e controllers MVC"

git add src/main/resources/static/ src/main/resources/templates/
git commit -m "feat(ui): templates Thymeleaf responsivos com Bootstrap 5"

git add src/test/
git commit -m "test: testes unitarios da maquina de estados, servico e integracao de seguranca"

git add README.md docs/RELATORIO_TECNICO.md
git commit -m "docs: relatorio tecnico completo, diagramas Mermaid e instrucoes de execucao"
```
