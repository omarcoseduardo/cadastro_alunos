# Sistema de Cadastro de Alunos

*Documentação Técnica e Manual de Uso*

---

## 1. Sobre o Projeto

Este projeto consiste em uma aplicação web desenvolvida com Spring Boot, cujo objetivo é realizar operações básicas de CRUD (Create, Read, Update, Delete) sobre uma entidade denominada Aluno. O sistema permite ao usuário cadastrar, visualizar, editar e excluir registros de alunos por meio de uma interface web construída com Thymeleaf, e também disponibiliza uma API REST para manipulação dos dados via ferramentas externas, como o Postman.

O banco de dados utilizado é o H2, um banco relacional que é inicializado automaticamente junto com a aplicação, sem necessidade de configuração externa. Os dados inseridos via interface web ou via API REST são persistidos na mesma base de dados.

---

## 2. Dependências do Projeto

| Dependência | Versão | Finalidade |
|---|---|---|
| Java | 21 | Linguagem de programação principal |
| Spring Boot Parent | 4.0.3 | Gerenciamento de versões e configurações base |
| spring-boot-h2console | 4.0.3 | Console web para acesso ao banco H2 |
| spring-boot-starter-data-jpa | 4.0.3 | Camada de persistência e acesso ao banco de dados |
| spring-boot-starter-web | 4.0.3 | Suporte a API REST e servidor web embarcado (Tomcat) |
| spring-boot-starter-thymeleaf | 4.0.3 | Template engine para renderização de páginas HTML |
| spring-boot-devtools | 4.0.3 | Ferramentas de desenvolvimento com hot reload |
| h2 | 2.4.240 | Banco de dados relacional em memória |
| lombok | 1.18.42 | Redução de código boilerplate via anotações |
| spring-boot-starter-validation | 4.0.3 | Validação de campos de entrada com Bean Validation |

---

## 3. Arquitetura da Aplicação

A aplicação segue o padrão de arquitetura em camadas, amplamente adotado em projetos Spring Boot. Cada camada possui uma responsabilidade bem definida, promovendo separação de preocupações e facilitando a manutenção do código.

### 3.1 Camada Model

A entidade Aluno representa o modelo de dados do sistema. Ela é mapeada para uma tabela no banco de dados H2 por meio das anotações Jakarta Persistence (JPA).

| Atributo | Tipo | Obrigatório | Descrição |
|---|---|---|---|
| id | Long | Sim (gerado) | Identificador único gerado automaticamente |
| usuario | String | Sim | Nome de usuário do aluno |
| matricula | Integer | Sim | Número de matrícula do aluno |
| email | String | Não | Endereço de e-mail do aluno |

### 3.2 Camada Repository

A interface AlunoRepository estende JpaRepository, fornecendo automaticamente os métodos básicos de persistência, como save, findById, findAll e deleteById. Além disso, o Spring Data JPA gera implementações automáticas para métodos derivados de nomenclatura, como findByUsuario.

### 3.3 Camada Service

A classe AlunoService concentra a lógica de negócio da aplicação. Ela intermedia a comunicação entre os controllers e o repositório, garantindo que as regras da aplicação sejam aplicadas antes de qualquer operação de persistência. Os métodos disponibilizados são: buscarAlunoAll, buscarPorId, buscarPorUser, adicionarAluno, editarAluno e deletarAluno.

### 3.4 Camada Controller

O sistema possui dois controllers distintos, cada um com uma responsabilidade diferente.

**AlunoController:** controller anotado com @RestController, responsável por expor os endpoints da API REST. Retorna dados no formato JSON e é utilizado por ferramentas externas como o Postman.

**AlunoViewController:** controller anotado com @Controller, responsável por servir as páginas HTML renderizadas pelo Thymeleaf. Utiliza o objeto Model para passar dados do backend para o frontend.

---

## 4. Endpoints da API REST

A API REST está disponível no caminho base `/aluno` e aceita e retorna dados no formato JSON.

| Método HTTP | Endpoint | Descrição | Status Sucesso | Status Erro |
|---|---|---|---|---|
| GET | /aluno | Retorna todos os alunos cadastrados | 200 OK | 404 Not Found |
| GET | /aluno/{id} | Retorna um aluno pelo ID | 200 OK | 404 Not Found |
| GET | /aluno/buscar?usuario= | Busca alunos pelo nome de usuário | 200 OK | 204 No Content |
| POST | /aluno | Cadastra um novo aluno | 201 Created | 400 Bad Request |
| PUT | /aluno/{id} | Edita os dados de um aluno existente | 200 OK | 404 Not Found |
| DELETE | /aluno/{id} | Remove um aluno pelo ID | 204 No Content | 404 Not Found |

### 4.1 Exemplo de Corpo da Requisição (POST e PUT)

Para cadastrar ou editar um aluno via API REST, o corpo da requisição deve ser enviado no formato JSON. Atenção: o campo matricula deve ser enviado como número inteiro, sem aspas.

```json
{
    "usuario": "joao.silva",
    "matricula": 12345,
    "email": "joao@email.com"
}
```

---

## 5. Interface Web (Thymeleaf)

A interface web é composta por três páginas HTML renderizadas pelo Thymeleaf. O Thymeleaf é um template engine que permite vincular dados do backend Java diretamente ao HTML por meio de atributos especiais prefixados com `th:`.

### 5.1 Página de Cadastro (/)

Acessível em `http://localhost:8080/`, esta página contém o formulário de cadastro de alunos. O formulário está vinculado ao objeto Aluno por meio do atributo `th:object`, e cada campo utiliza `th:field` para mapear os atributos da entidade. A página também exibe mensagens de erro de validação caso os campos obrigatórios não sejam preenchidos corretamente.

### 5.2 Página de Lista de Alunos (/alunos)

Acessível pelo botão "Ver cadastros" na página de cadastro, esta página exibe todos os alunos cadastrados em formato de tabela. Para cada aluno, são disponibilizados os botões de exclusão e edição. A exclusão solicita confirmação ao usuário antes de efetivar a operação. Caso nenhum aluno esteja cadastrado, uma mensagem informativa é exibida no lugar da tabela.

### 5.3 Página de Edição (/alunos/{id}/editar)

Acessada ao clicar no botão Editar na lista de alunos, esta página exibe um formulário pré-preenchido com os dados atuais do aluno selecionado. Após a edição, o usuário pode salvar as alterações ou retornar para a lista sem salvar.

---

## 6. Vinculação Thymeleaf e Backend

A comunicação entre o backend Java e as páginas HTML ocorre por meio do objeto Model, disponibilizado pelo Spring MVC. O controller adiciona atributos ao Model, que ficam acessíveis no HTML pelo Thymeleaf com a sintaxe `${nomeDoAtributo}`.

| Atributo | Descrição | Exemplo |
|---|---|---|
| th:object | Vincula um objeto Java ao formulário | `th:object="${aluno}"` |
| th:field | Vincula um campo do formulário a um atributo do objeto | `th:field="*{usuario}"` |
| th:text | Exibe o valor de uma expressão no conteúdo do elemento | `th:text="${aluno.email}"` |
| th:each | Itera sobre uma lista de objetos | `th:each="aluno : ${alunos}"` |
| th:if | Exibe o elemento somente se a condição for verdadeira | `th:if="${#lists.isEmpty(alunos)}"` |
| th:unless | Exibe o elemento somente se a condição for falsa | `th:unless="${#lists.isEmpty(alunos)}"` |
| th:action | Define a URL de submissão do formulário | `th:action="@{/}"` |
| th:href | Define o destino de um link de forma dinâmica | `th:href="@{/alunos}"` |
| th:errors | Exibe mensagens de erro de validação do campo | `th:errors="*{usuario}"` |

---

## 7. Banco de Dados H2

O H2 é um banco de dados relacional escrito em Java, configurado neste projeto para persistir dados em arquivo, garantindo que os registros sejam mantidos mesmo após a reinicialização da aplicação.

### 7.1 Configuração

```properties
spring.datasource.url=jdbc:h2:file:./banco;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=sa
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### 7.2 Console H2

O console web do H2 está acessível em `http://localhost:8080/h2-console` durante a execução da aplicação. Para autenticar, utilize usuário `sa` e senha `sa`.

---

## 8. Como Executar o Projeto

### 8.1 Pré-requisitos

- Java 21 ou superior instalado
- Maven configurado no sistema
- IDE recomendada: IntelliJ IDEA

### 8.2 Executando pela IDE

1. Clone ou abra o projeto na IDE
2. Aguarde o Maven baixar as dependências
3. Execute a classe CadastrosApplication
4. Acesse `http://localhost:8080/` no navegador

---

## 9. Guia de Uso do Sistema

### 9.1 Cadastrar um Aluno

1. Acesse `http://localhost:8080/` no navegador
2. Preencha o campo Usuário (obrigatório)
3. Preencha o campo Matrícula com um número inteiro (obrigatório)
4. Preencha o campo Email (opcional)
5. Clique em Cadastrar

### 9.2 Visualizar Alunos Cadastrados

1. Na página de cadastro, clique no botão Ver cadastros
2. A tabela exibirá todos os alunos cadastrados no sistema

### 9.3 Editar um Aluno

1. Na lista de alunos, clique no botão Editar da linha desejada
2. O formulário será pré-preenchido com os dados atuais do aluno
3. Altere os campos desejados e clique em Salvar

### 9.4 Excluir um Aluno

1. Na lista de alunos, clique no botão Excluir da linha desejada
2. Uma caixa de confirmação será exibida solicitando confirmação da ação
3. Confirme para efetivar a exclusão ou cancele para retornar

### 9.5 Utilizando a API REST via Postman

1. Certifique-se de que a aplicação está em execução
2. Abra o Postman e crie uma nova requisição
3. Selecione o método HTTP desejado (GET, POST, PUT ou DELETE)
4. Informe a URL conforme a tabela de endpoints da seção 4
5. Para POST e PUT, selecione Body > raw > JSON e informe o corpo conforme o exemplo da seção 4.1
6. Clique em Send e verifique a resposta

---

## 10. Estrutura do Projeto

```
src/main/java/com/aula_04/cadastros/
    controller/
        AlunoController.java        (API REST)
        AlunoViewController.java    (Interface Web)
    model/
        Aluno.java                  (Entidade JPA)
    repository/
        AlunoRepository.java        (Spring Data JPA)
    service/
        AlunoService.java           (Lógica de Negócio)
    CadastrosApplication.java       (Classe principal)

src/main/resources/
    static/css/style.css            (Estilos CSS)
    templates/
        fragments/head.html         (Fragmento de cabeçalho)
        crud.html                   (Página de cadastro)
        lista.html                  (Página de listagem)
        editar.html                 (Página de edição)
    application.properties          (Configurações da aplicação)
```

---

*Projeto acadêmico desenvolvido com Spring Boot, Thymeleaf e H2 Database.*