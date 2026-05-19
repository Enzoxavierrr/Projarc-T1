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

## Documentação Interativa (Swagger)

Com a aplicação rodando, acesse:

```
http://localhost:8080/swagger-ui/index.html
```

O Swagger UI lista todos os endpoints agrupados por UC, com descrições, exemplos de JSON prontos e botão **"Try it out"** para testar direto no navegador — sem precisar de Bruno ou Postman.

---

## Endpoints

### Cardápio (UC3)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/cardapio/corrente` | Retorna o cardápio ativo no momento |
| `GET` | `/cardapio/{id}` | Retorna um cardápio específico pelo ID |
| `GET` | `/cardapio/lista` | Lista todos os cardápios cadastrados |

---

### Pedidos

#### UC4 — Submeter Pedido

```
POST /pedidos/submeter
```

**Body:**
```json
{
  "clienteCpf": "9001",
  "enderecoEntrega": "Rua das Flores, 100",
  "itens": [
    { "produtoId": 3, "quantidade": 1 },
    { "produtoId": 5, "quantidade": 2 }
  ]
}
```

> **Atenção:** o produto de ID `1` sempre é reprovado pelo `EstoqueServiceFake` (comportamento intencional de teste). Use outros IDs para obter pedido APROVADO.

O sistema verifica estoque, aplica desconto e imposto e retorna o pedido com status `APROVADO` ou `REPROVADO`. Em ambos os casos o pedido é salvo no banco e recebe um ID.

---

#### UC5 — Consultar Status do Pedido

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

```
DELETE /pedidos/{id}?cpf=9001
```

Cancela um pedido com status `APROVADO`. O CPF informado deve ser do cliente dono do pedido.

**Respostas:**
- `204` — Cancelado com sucesso
- `400` — Pedido não está em status cancelável
- `403` — CPF não pertence ao dono do pedido
- `404` — Pedido não encontrado

---

#### UC7 — Pagar Pedido

```
POST /pedidos/{id}/pagar
```

**Body:**
```json
{
  "cpf": "9001"
}
```

Processa o pagamento de um pedido com status `APROVADO`. Após o pagamento, o pedido é encaminhado automaticamente para a fila da cozinha.

**Respostas:**
- `200` — Pagamento realizado
- `400` — Pedido não está com status `APROVADO`
- `403` — CPF não pertence ao dono do pedido
- `404` — Pedido não encontrado

---

## Clientes e Produtos disponíveis no banco (dados iniciais)

### Clientes

| CPF | Nome |
|-----|------|
| `9001` | Huguinho Pato |
| `9002` | Luizinho Pato |

### Produtos (cardápio ativo: ID 2)

| ID | Produto | Preço |
|----|---------|-------|
| 1 | Pizza calabresa | R$ 55,00 (**reprovado no estoque fake**) |
| 3 | Pizza margherita | R$ 40,00 |
| 4 | Pizza portuguesa | R$ 65,00 |
| 5 | Pizza frango com catupiry | R$ 70,00 |
| 6 | Pizza quatro queijos | R$ 75,00 |

---

## Arquitetura

O projeto segue **Clean Architecture** em 4 camadas:

```
┌────────────────────────────────────────┐
│    ADAPTADORES (Interface Externa)     │
│  Controllers | Presenters | Repositórios│
├────────────────────────────────────────┤
│    APLICAÇÃO (Casos de Uso)            │
│  CarregarCardapioUC                    │
│  SubmeterPedidoUC                      │
│  SolicitaStatusPedidoUC               │
│  CancelarPedidoUC                      │
│  PagarPedidoUC                         │
├────────────────────────────────────────┤
│    DOMÍNIO (Lógica de Negócio)         │
│  Entidades | Serviços | Interfaces     │
│  Pedido | Cliente | Produto | Cardápio │
├────────────────────────────────────────┤
│    INFRAESTRUTURA                      │
│  Spring Boot | JDBC | H2 Database      │
└────────────────────────────────────────┘
```

---

## Tecnologias

| Tecnologia | Versão | Uso |
|-----------|--------|-----|
| Java | 21 LTS | Linguagem principal |
| Spring Boot | 3.5.4 | Framework web |
| Spring JDBC | 3.5.4 | Acesso a dados |
| H2 Database | — | Banco em memória (reinicia a cada boot) |
| SpringDoc OpenAPI | 2.8.9 | Swagger UI / documentação interativa |
| Lombok | — | Reduz boilerplate |
| Maven | 3.9+ | Build e dependências |

---

## Fluxo completo de um pedido

```
1. GET  /cardapio/corrente          → ver produtos disponíveis
2. POST /pedidos/submeter           → criar pedido (retorna id + status)
3. GET  /pedidos/status/{id}        → consultar status
4. POST /pedidos/{id}/pagar         → pagar (só se APROVADO)
5. GET  /pedidos/status/{id}        → confirmar novo status (AGUARDANDO → PREPARACAO → PRONTO)
```

---

## Referências

- [Clean Architecture - Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Domain-Driven Design - Eric Evans](https://www.domainlanguage.com/)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [SpringDoc OpenAPI](https://springdoc.org/)
