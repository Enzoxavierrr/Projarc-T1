# Tele Pizza — Backend

Sistema de gerenciamento de pedidos de uma pizzaria, desenvolvido como trabalho acadêmico da disciplina de Projeto e Arquitetura de Software (PAS) — 5º semestre.

A Parte 1 implementou o sistema como monolito com Clean Architecture. A Parte 2 evolui para uma arquitetura de microsserviços orquestrada com Docker Compose.

---

## Arquitetura

```
                        ┌─────────────────────────────────────────────────────┐
                        │              Docker Compose                         │
                        │                                                     │
  Cliente ──────────────▶  Spring Cloud Gateway (:8080)                      │
  Admin  ──────────────▶       │  validação JWT                              │
                        │      │  roteamento por nome (Eureka)               │
                        │      │                                             │
                        │      ├──────────────▶  Serviço de Pizzaria        │
                        │      │                 (monolito — Clean Arch)     │
                        │      │                  │                          │
                        │      │                  ├── REST síncrono ──▶ Serviço de Estoque │
                        │      │                  │                    (JPA + BD próprio)  │
                        │      │                  │                                        │
                        │      │                  └── RabbitMQ ──▶ Serviço de Entregas    │
                        │      │                                   (3 instâncias)          │
                        │      │                                                           │
                        │      └──────────────▶  Eureka Server (:8761)                   │
                        └─────────────────────────────────────────────────────────────────┘
```

### Microsserviços

| Serviço | Porta | Descrição |
|---|---|---|
| `gateway` | 8080 | Ponto de entrada. Valida JWT e roteia requisições |
| `eureka` | 8761 | Name server. Todos os serviços se registram aqui |
| `pizzaria` | interno | Monolito principal com os casos de uso de negócio |
| `estoque` | interno | Gerencia ingredientes com JPA e banco próprio |
| `entregas` | interno | Consome fila RabbitMQ e simula a entrega (3 instâncias) |
| `rabbitmq` | 5672 / 15672 | Message broker. Painel web em localhost:15672 |

---

## Como executar

### Pré-requisitos

- Docker e Docker Compose instalados

### Subir tudo

```bash
docker compose up
```

### Subir com 3 instâncias do serviço de entregas

```bash
docker compose up --scale entregas=3
```

### Verificar serviços registrados no Eureka

Acesse `http://localhost:8761` no navegador.

### Painel do RabbitMQ

Acesse `http://localhost:15672` — usuário `guest`, senha `guest`.

---

## Configuração por variável de ambiente

| Variável | Padrão | Descrição |
|---|---|---|
| `IMPOSTO_ATIVO` | `lei_1234` | Define a estratégia de imposto ativa |
| `EUREKA_URL` | `http://eureka:8761/eureka` | URL do Eureka Server |
| `RABBITMQ_HOST` | `rabbitmq` | Host do RabbitMQ |

Exemplo de uso:
```bash
IMPOSTO_ATIVO=lei_5678 docker compose up
```

---

## Autenticação

O JWT é validado pelo Gateway antes de chegar em qualquer serviço. O fluxo é:

1. Registre um cliente ou use um dos clientes já cadastrados
2. Faça login e copie o `token` retornado
3. Envie o token em todas as requisições protegidas:

```
Authorization: Bearer <token>
```

Rotas públicas (sem token): `POST /clientes/registrar`, `POST /clientes/login`

---

## Endpoints

### Clientes

#### UC1 — Registrar cliente
```
POST /clientes/registrar
```
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
Respostas: `201` sucesso · `400` CPF inválido · `409` CPF ou e-mail já cadastrado

---

#### UC2 — Login
```
POST /clientes/login
```
```json
{
  "email": "joao@email.com",
  "senha": "senha123"
}
```
```json
{
  "cpf": "12345678901",
  "token": "uuid-gerado",
  "mensagem": "Usuario @João Silva logado com sucesso."
}
```

---

### Cardápio

#### UC3 — Consultar cardápio
```
GET /cardapio/corrente       → cardápio ativo
GET /cardapio/{id}           → cardápio por ID
GET /cardapio/lista          → todos os cardápios
```

---

### Pedidos

#### UC4 — Submeter pedido
```
POST /pedidos/submeter
```
```json
{
  "enderecoEntrega": "Rua das Flores, 100",
  "itens": [
    { "produtoId": 3, "quantidade": 1 },
    { "produtoId": 5, "quantidade": 2 }
  ]
}
```
O cliente é identificado pelo token. O sistema verifica estoque, aplica desconto e imposto, e retorna o pedido com status `APROVADO` ou `REPROVADO`.

---

#### UC5 — Consultar status
```
GET /pedidos/status/{idPedido}
```

| Status | Descrição |
|---|---|
| `APROVADO` | Aguardando pagamento |
| `REPROVADO` | Falta de estoque |
| `PAGO` | Pagamento processado |
| `AGUARDANDO` | Na fila da cozinha |
| `PREPARACAO` | Em preparo |
| `PRONTO` | Pronto para entrega |
| `TRANSPORTE` | Em rota de entrega |
| `ENTREGUE` | Entregue ao cliente |
| `CANCELADO` | Cancelado pelo cliente |

---

#### UC6 — Cancelar pedido
```
DELETE /pedidos/{id}
```
Cancela pedidos com status `APROVADO`. Somente o dono do pedido pode cancelar.

Respostas: `204` cancelado · `400` status inválido · `403` não é o dono · `404` não encontrado

---

#### UC7 — Pagar pedido
```
POST /pedidos/{id}/pagar
```
Após o pagamento o pedido avança automaticamente para a fila da cozinha e depois para entrega via RabbitMQ.

Respostas: `200` sucesso · `400` status inválido · `403` não é o dono · `404` não encontrado

---

#### UC8 — Listar pedidos entregues por período (admin)
```
GET /pedidos/entregues?inicio=2025-01-01&fim=2025-12-31
```

---

#### UC9 — Meus pedidos entregues
```
GET /pedidos/entregues/meus?inicio=2025-01-01&fim=2025-12-31
```

---

### Impostos e Descontos (Admin)

#### Listar estratégias de desconto disponíveis
```
GET /admin/desconto
```

#### Trocar estratégia de desconto ativa
```
PUT /admin/desconto/{codigo}
```

Estratégias disponíveis: `SemDesconto`, `DescontoFidelidade`, `PromocaoVerao`

O imposto é configurado via variável de ambiente `IMPOSTO_ATIVO`. Estratégias disponíveis: `lei_1234` (10% flat), `lei_5678` (progressivo).

---

## Fluxo completo de um pedido

```
1. POST /clientes/login              → obter token
2. GET  /cardapio/corrente           → ver produtos disponíveis
3. POST /pedidos/submeter            → criar pedido → retorna id + APROVADO ou REPROVADO
4. POST /pedidos/{id}/pagar          → pagar (somente se APROVADO)
5. GET  /pedidos/status/{id}         → acompanhar: PAGO → AGUARDANDO → PREPARACAO → PRONTO
                                          (automático via RabbitMQ) → TRANSPORTE → ENTREGUE
```

---

## Dados iniciais

### Clientes

| CPF | Nome | Email | Senha |
|---|---|---|---|
| `9001` | Huguinho Pato | `huguinho.pato@email.com` | `123456` |
| `9002` | Luizinho Pato | `zezinho.pato@email.com` | `123456` |

### Cardápio ativo (ID 2)

| ID | Produto | Preço |
|---|---|---|
| 1 | Pizza calabresa | R$ 55,00 |
| 3 | Pizza margherita | R$ 40,00 |
| 4 | Pizza portuguesa | R$ 65,00 |
| 5 | Pizza frango com catupiry | R$ 70,00 |
| 6 | Pizza quatro queijos | R$ 75,00 |

---

## Estrutura do repositório

```
/
├── eureka-server/          → Name server (Spring Cloud Netflix Eureka)
├── gateway/                → Spring Cloud Gateway com autenticação JWT
├── pizzaria/               → Monolito principal (Clean Architecture)
│   ├── Adaptadores/        → Controllers, filtros, repositórios
│   ├── Aplicacao/          → Casos de uso (UC1–UC9)
│   └── Dominio/            → Entidades, serviços, interfaces
├── estoque/                → Microsserviço de estoque (JPA)
├── entregas/               → Microsserviço de entregas (RabbitMQ consumer)
└── docker-compose.yml      → Orquestração de todos os containers
```

---

## Tecnologias

| Tecnologia | Uso |
|---|---|
| Java 21 | Linguagem principal |
| Spring Boot 3.5 | Framework base |
| Spring Cloud Gateway | API Gateway e roteamento |
| Spring Cloud Netflix Eureka | Service discovery |
| Spring Data JPA | Acesso a dados no microsserviço de estoque |
| Spring AMQP (RabbitMQ) | Comunicação assíncrona com entregas |
| H2 Database | Banco em memória (pizzaria e estoque) |
| Docker Compose | Orquestração dos containers |
| Lombok | Redução de boilerplate |

---

## Equipe

Trabalho desenvolvido em equipe de 5 integrantes para a disciplina PAS — Prof. Bernardo Copstein.
