# Lancheria DDD - Sistema de Gerenciamento de Pizzaria

## Descrição do Projeto

**Lancheria DDD v1** é um sistema backend para gerenciamento de uma pizzaria, desenvolvido com **Clean Architecture** e **Domain-Driven Design (DDD)** como trabalho acadêmico da disciplina de Projeto de Arquitetura (Projarc) — 5º semestre.

---

## Como Executar

### Pré-requisitos
- Java 21+
- Maven 3.9+

### Passos

```bash
mvn spring-boot:run
```

A aplicação sobe na porta `8080`.

---

## Autenticação

A maioria dos endpoints é protegida por token Bearer. O fluxo é:

1. Registre um cliente (`POST /clientes/registrar`) ou use um dos clientes já cadastrados
2. Faça login (`POST /clientes/login`) e copie o `token` retornado
3. Em todas as requisições protegidas, envie o header:

```
Authorization: Bearer <token>
```

> **Rotas públicas** (sem token): `/clientes/registrar`, `/clientes/login`  
> **Rotas protegidas** (exigem token): todos os demais endpoints

---

## Endpoints

### Clientes

#### UC1 — Registrar Cliente

```
POST /clientes/registrar
```

**Body:**
```json
{
  "cpf": "12345678901",
  "nome": "João Silva",
  "email": "joao@email.com",
  "senha": "senha123",
  "celular": "51999999999",
  "endereco": "Rua das Flores, 100"
}
```

Regras:
- CPF deve ter exatamente **11 dígitos numéricos**
- CPF e e-mail não podem estar já cadastrados

**Respostas:**
- `201` — `{ "mensagem": "Usuario @João Silva cadastrado com sucesso." }`
- `400` — CPF inválido
- `409` — CPF ou e-mail já cadastrado

---

#### UC2 — Autenticar (Login)

```
POST /clientes/login
```

**Body:**
```json
{
  "email": "joao@email.com",
  "senha": "senha123"
}
```

**Respostas:**
- `200` — Retorna token e mensagem de boas-vindas:
```json
{
  "cpf": "12345678901",
  "token": "uuid-gerado",
  "mensagem": "Usuario @João Silva logado com sucesso."
}
```
- `401` — Email ou senha inválidos

> Guarde o `token` retornado — ele será necessário para todas as requisições protegidas.

---

### Cardápio

#### UC3 — Carregar Cardápio

> Requer `Authorization: Bearer <token>`

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/cardapio/corrente` | Retorna o cardápio ativo no momento |
| `GET` | `/cardapio/{id}` | Retorna um cardápio específico pelo ID |
| `GET` | `/cardapio/lista` | Lista todos os cardápios cadastrados |

---

### Pedidos

#### UC4 — Submeter Pedido

> Requer `Authorization: Bearer <token>`

```
POST /pedidos/submeter
```

**Body:**
```json
{
  "enderecoEntrega": "Rua das Flores, 100",
  "itens": [
    { "produtoId": 3, "quantidade": 1 },
    { "produtoId": 5, "quantidade": 2 }
  ]
}
```

> O cliente é identificado automaticamente pelo token — não é necessário informar CPF no body.

O sistema verifica estoque, aplica desconto e imposto e retorna o pedido com status `APROVADO` ou `REPROVADO`. Em ambos os casos o pedido é salvo no banco e recebe um ID.

---

#### UC5 — Consultar Status do Pedido

> Requer `Authorization: Bearer <token>`

```
GET /pedidos/status/{idPedido}
```

Retorna o status atual do pedido. Possíveis valores:

| Status | Descrição |
|--------|-----------|
| `APROVADO` | Pedido aprovado, aguardando pagamento |
| `REPROVADO` | Reprovado por falta de estoque |
| `PAGO` | Pagamento processado |
| `AGUARDANDO` | Na fila da cozinha |
| `PREPARACAO` | Em preparo |
| `PRONTO` | Pronto para entrega |
| `TRANSPORTE` | Em rota de entrega |
| `ENTREGUE` | Entregue ao cliente |
| `CANCELADO` | Cancelado pelo cliente |

---

#### UC6 — Cancelar Pedido

> Requer `Authorization: Bearer <token>`

```
DELETE /pedidos/{id}
```

Cancela um pedido com status `APROVADO`. O token identifica o cliente — somente o dono do pedido pode cancelar.

**Respostas:**
- `204` — Cancelado com sucesso
- `400` — Pedido não está em status cancelável
- `403` — Pedido não pertence ao cliente autenticado
- `404` — Pedido não encontrado

---

#### UC7 — Pagar Pedido

> Requer `Authorization: Bearer <token>`

```
POST /pedidos/{id}/pagar
```

Processa o pagamento de um pedido com status `APROVADO`. O cliente é identificado pelo token. Após o pagamento, o pedido avança automaticamente para a fila da cozinha.

**Respostas:**
- `200` — Pagamento realizado com sucesso
- `400` — Pedido não está com status `APROVADO` ou pagamento recusado
- `403` — Pedido não pertence ao cliente autenticado
- `404` — Pedido não encontrado

---

#### UC8 — Listar Pedidos Entregues por Período

```
GET /pedidos/entregues?inicio=2025-01-01&fim=2025-12-31
```

Lista todos os pedidos com status `ENTREGUE` dentro do intervalo de datas informado.

**Parâmetros:**

| Parâmetro | Formato | Descrição |
|-----------|---------|-----------|
| `inicio` | `YYYY-MM-DD` | Data inicial (inclusive) |
| `fim` | `YYYY-MM-DD` | Data final (inclusive) |

---

#### UC9 — Listar Meus Pedidos Entregues

> Requer `Authorization: Bearer <token>`

```
GET /pedidos/entregues/meus?inicio=2025-01-01&fim=2025-12-31
```

Lista os pedidos com status `ENTREGUE` do cliente autenticado dentro do intervalo de datas. O CPF é extraído automaticamente do token.

---

## Clientes disponíveis no banco (dados iniciais)

| CPF | Nome | Email | Senha |
|-----|------|-------|-------|
| `9001` | Huguinho Pato | `huguinho.pato@email.com` | `123456` |
| `9002` | Luizinho Pato | `zezinho.pato@email.com` | `123456` |

---

## Produtos (cardápio ativo: ID 2)

| ID | Produto | Preço |
|----|---------|-------|
| 1 | Pizza calabresa | R$ 55,00 |
| 3 | Pizza margherita | R$ 40,00 |
| 4 | Pizza portuguesa | R$ 65,00 |
| 5 | Pizza frango com catupiry | R$ 70,00 |
| 6 | Pizza quatro queijos | R$ 75,00 |

---

## Fluxo completo de um pedido

```
1. POST /clientes/login              → autenticar e obter token
2. GET  /cardapio/corrente           → ver produtos disponíveis
3. POST /pedidos/submeter            → criar pedido (retorna id + status)
4. GET  /pedidos/status/{id}         → consultar status
5. POST /pedidos/{id}/pagar          → pagar (só se APROVADO)
6. GET  /pedidos/status/{id}         → confirmar novo status (PAGO → AGUARDANDO → ... → ENTREGUE)
```

---

## Arquitetura

O projeto segue **Clean Architecture** em 4 camadas:

```
┌────────────────────────────────────────────┐
│      ADAPTADORES (Interface Externa)       │
│  Controllers | Presenters | Repositórios   │
│  AutenticacaoFilter                        │
├────────────────────────────────────────────┤
│      APLICAÇÃO (Casos de Uso)              │
│  RegistrarClienteUC  (UC1)                 │
│  AutenticarUC        (UC2)                 │
│  CarregarCardapioUC  (UC3)                 │
│  SubmeterPedidoUC    (UC4)                 │
│  SolicitaStatusPedidoUC (UC5)              │
│  CancelarPedidoUC    (UC6)                 │
│  PagarPedidoUC       (UC7)                 │
│  ListarPedidosEntreguesUC       (UC8)      │
│  ListarPedidosClienteEntreguesUC (UC9)     │
├────────────────────────────────────────────┤
│      DOMÍNIO (Lógica de Negócio)           │
│  Entidades | Serviços | Interfaces         │
│  Pedido | Cliente | Produto | Cardápio     │
├────────────────────────────────────────────┤
│      INFRAESTRUTURA                        │
│  Spring Boot | JDBC | H2 Database          │
└────────────────────────────────────────────┘
```

---

## Tecnologias

| Tecnologia | Versão | Uso |
|-----------|--------|-----|
| Java | 21 LTS | Linguagem principal |
| Spring Boot | 3.5.4 | Framework web |
| Spring JDBC | 3.5.4 | Acesso a dados |
| H2 Database | — | Banco em memória (reinicia a cada boot) |
| Lombok | — | Reduz boilerplate |
| Maven | 3.9+ | Build e dependências |

---

## Referências

- [Clean Architecture - Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Domain-Driven Design - Eric Evans](https://www.domainlanguage.com/)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
