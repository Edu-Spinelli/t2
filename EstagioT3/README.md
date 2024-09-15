
# Trabalho AA3

Este projeto implementa uma API REST para gerenciar profissionais, empresas e vagas de emprego. O sistema oferece operações completas de CRUD (Criar, Ler, Atualizar, Excluir) para cada entidade, bem como consultas específicas, como a recuperação de vagas de emprego abertas.



## Endpoints da API

### 1. **Profissionais**

#### Criar um Novo Profissional

- **Método**: `POST`
- **URL**: `/api/profissionais`
- **Corpo (JSON)**:
  ```json
  {
      "nome": "João da Silva",
      "cpf": "12345678901",
      "telefone": "11987654321",
      "sexo": "M",
      "dataNascimento": "1990-05-15",
      "email": "joao@example.com",
      "senha": "senha123"
  }
  ```

#### Buscar Todos os Profissionais

- **Método**: `GET`
- **URL**: `/api/profissionais`

#### Buscar um Profissional por ID

- **Método**: `GET`
- **URL**: `/api/profissionais/{id}`

#### Atualizar um Profissional por ID

- **Método**: `PUT`
- **URL**: `/api/profissionais/{id}`
- **Corpo (JSON)**: Pode enviar todos ou alguns dos campos.
  ```json
  {
      "nome": "Nome Atualizado",
      "telefone": "1123456789"
  }
  ```

#### Excluir um Profissional por ID

- **Método**: `DELETE`
- **URL**: `/api/profissionais/{id}`

---

### 2. **Empresas**

#### Criar uma Nova Empresa

- **Método**: `POST`
- **URL**: `/api/empresas`
- **Corpo (JSON)**:
  ```json
  {
      "nome": "Tech Solutions",
      "cnpj": "12345678000199",
      "descricao": "Empresa especializada em desenvolvimento de software.",
      "cidade": "São Paulo",
      "email": "contato@techsolutions.com",
      "senha": "senha123"
  }
  ```

#### Buscar Todas as Empresas

- **Método**: `GET`
- **URL**: `/api/empresas`

#### Buscar uma Empresa por ID

- **Método**: `GET`
- **URL**: `/api/empresas/{id}`

#### Buscar Todas as Empresas em uma Cidade pelo Nome

- **Método**: `GET`
- **URL**: `/api/empresas/cidades/{nome}`

#### Atualizar uma Empresa por ID

- **Método**: `PUT`
- **URL**: `/api/empresas/{id}`
- **Corpo (JSON)**: Pode enviar todos ou alguns dos campos.
  ```json
  {
      "descricao": "Descrição Atualizada",
      "cidade": "Rio de Janeiro"
  }
  ```

#### Excluir uma Empresa por ID

- **Método**: `DELETE`
- **URL**: `/api/empresas/{id}`

---

### 3. **Vagas de Emprego**

#### Buscar Todas as Vagas de Emprego

- **Método**: `GET`
- **URL**: `/api/vagas`

#### Buscar uma Vaga de Emprego por ID

- **Método**: `GET`
- **URL**: `/api/vagas/{id}`

#### Buscar Vagas de Emprego Abertas de uma Empresa por ID

- **Método**: `GET`
- **URL**: `/api/vagas/empresas/{id}`
- **Nota**: Vagas abertas são aquelas cuja data limite de inscrição ainda não passou.

---

## Autenticação

Nenhuma autenticação é necessária para esses endpoints da API, com base na configuração fornecida.

---

## Instruções de Configuração

1. Clone o repositório e navegue até a pasta do projeto.
2. Certifique-se de que o Maven está instalado e execute o seguinte comando para compilar o projeto:

   ```bash
   mvn clean install
   ```

3. Execute o projeto:

   ```bash
   mvn spring-boot:run
   ```

4. A API estará disponível em `http://localhost:8080`.

---

## Notas

- O banco de dados criará automaticamente as tabelas necessárias ao iniciar, caso não existam.
- Utilize ferramentas como Postman ou cURL para interagir com a API.

