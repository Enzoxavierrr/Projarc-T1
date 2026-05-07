# 🍕 Lancheria DDD - Sistema de Gerenciamento de Pizzaria

## 📋 Descrição do Projeto

**Lancheria DDD v1** é um sistema backend para gerenciamento de uma pizzaria, desenvolvido com **Clean Architecture** e **Domain-Driven Design (DDD)**.

O sistema permite:
- ✅ Recuperar cardápios disponíveis
- ✅ Consultar produtos e suas receitas
- ✅ Gerenciar ingredientes e estoque
- ✅ Consultar sugestões do chef
- ✅ Gerenciar pedidos e clientes

---

## 🏗️ Arquitetura do Sistema

O projeto segue uma **Arquitetura em 4 Camadas** com princípios de **Clean Architecture** e **DDD**:

```
┌────────────────────────────────────────┐
│    ADAPTADORES (Interface Externa)     │
│  Controllers | Presenters | Repositórios│
│         (Spring Framework)              │
├────────────────────────────────────────┤
│    APLICAÇÃO (Casos de Uso)            │
│  RecuperaListaCardapiosUC              │
│  RecuperarCardapioUC                   │
│  RecuperarIngredientesUC               │
│         Responses & DTOs               │
├────────────────────────────────────────┤
│    DOMÍNIO (Lógica de Negócio)         │
│  Entidades | Serviços | Interfaces    │
│  Cardapio | Produto | Receita         │
│  Cliente | Pedido | Ingrediente       │
├────────────────────────────────────────┤
│    INFRAESTRUTURA (Dados & Framework)  │
│  Spring Boot | JDBC | H2 Database     │
└────────────────────────────────────────┘
```

### 🎯 Princípios Aplicados

| Princípio | Benefício |
|-----------|-----------|
| **Separação de Responsabilidades** | Cada camada tem um propósito claro |
| **Independência de Frameworks** | Lógica de negócio não depende de Spring |
| **Testabilidade** | Fácil testar sem frameworks externos |
| **Inversão de Dependência** | Abstrações (interfaces) no domínio |
| **Domain-Driven Design** | Entidades ricas em comportamento |

---

## 📁 Estrutura de Pastas

```
src/main/java/com/bcopstein/ex4_lancheriaddd_v1/
│
├── 📂 Adaptadores/
│   ├── Apresentacao/
│   │   ├── CardapioController.java          ← Endpoints REST
│   │   ├── Controller.java                  ← Controlador genérico
│   │   └── Presenters/
│   │       ├── CardapioPresenter.java       ← Formata resposta
│   │       └── CabecalhoCardapioPresenter.java
│   │
│   └── Dados/
│       ├── CardapioRepositoryJDBC.java     ← Implementação JDBC
│       ├── IngredientesRepositoryJDBC.java
│       ├── ProdutosRepositoryJDBC.java
│       └── ReceitasRepositoryJDBC.java
│
├── 📂 Aplicacao/ (Use Cases - Orquestração)
│   ├── RecuperaListaCardapiosUC.java        ← Listar cardápios
│   ├── RecuperarCardapioUC.java             ← Obter cardápio específico
│   └── Responses/
│       ├── CardapioResponse.java            ← DTO de resposta
│       └── CabecalhoCardapioResponse.java
│
├── 📂 Dominio/ (Lógica de Negócio Pura)
│   ├── Dados/
│   │   ├── CardapioRepository.java          ← Interface (abstração)
│   │   ├── IngredientesRepository.java
│   │   ├── ProdutosRepository.java
│   │   └── ReceitasRepository.java
│   │
│   ├── Entidades/ (Modelos de Domínio)
│   │   ├── Cardapio.java
│   │   ├── CabecalhoCardapio.java
│   │   ├── Produto.java
│   │   ├── Receita.java
│   │   ├── Ingrediente.java
│   │   ├── ItemEstoque.java
│   │   ├── Cliente.java
│   │   ├── Pedido.java
│   │   └── ItemPedido.java
│   │
│   └── Servicos/ (Orquestração de Domínio)
│       ├── CardapioService.java
│       ├── CozinhaService.java
│       └── ICozinhaService.java
│
└── Ex4LancheriadddV1Application.java        ← Ponto de entrada (Spring Boot)

resources/
├── application.yaml                        ← Configuração
├── schema.sql                              ← Criação de tabelas
└── data.sql                                ← Dados iniciais
```

---

## 🔄 Fluxos Principais

### 1️⃣ Fluxo: Recuperar Lista de Cardápios

```
Cliente HTTP
    ↓
[GET /cardapio/lista]
    ↓
CardapioController.recuperaListaCardapios()
    ↓
RecuperaListaCardapiosUC.run()
    ↓
CardapioService.recuperaListaDeCardapios()
    ↓
CardapioRepository.cardapiosDisponiveis()  (Interface)
    ↓
CardapioRepositoryJDBC.cardapiosDisponiveis()  (Implementação JDBC)
    ↓
📊 Base de Dados (H2)
    ↓
CabecalhoCardapioResponse (DTO)
    ↓
CabecalhoCardapioPresenter (Formata JSON)
    ↓
✅ Resposta JSON ao cliente
```

### 2️⃣ Fluxo: Recuperar Cardápio Específico com Sugestões

```
Cliente HTTP
    ↓
[GET /cardapio/{id}]
    ↓
CardapioController.recuperaCardapio(id)
    ↓
RecuperarCardapioUC.run(id)
    ↓
CardapioService.recuperaCardapio(id)
    ↓
CardapioRepository.recuperaPorId(id)
    ↓
📊 Base de Dados
    ↓
CardapioService.recuperaSugestoesDoChef()
    ↓
CardapioRepository.indicacoesDoChef()
    ↓
CardapioResponse (combina cardápio + sugestões)
    ↓
CardapioPresenter (formata com indicações)
    ↓
✅ JSON com produtos e sugestões do chef
```

### 3️⃣ Fluxo: Gerenciamento de Receitas

```
Receita
  ├── ID
  ├── Nome
  ├── Descrição
  └── Ingredientes (1:N)
        ├── Ingrediente 1 → Quantidade
        ├── Ingrediente 2 → Quantidade
        └── Ingrediente N → Quantidade

Produto
  ├── ID
  ├── Descrição
  ├── Preço
  ├── Receita (1:1)
  └── Ativo (booleano)
```

---

## 🔑 Entidades do Domínio

### Cardapio
Agrupa produtos sob um mesmo contexto/categoria

```java
public class Cardapio {
    private CabecalhoCardapio cabecalhoCardapio;
    private List<Produto> produtos;
}
```

### Produto
Representa um item disponível no cardápio

```java
public class Produto {
    private long id;
    private String descricao;
    private double preco;
    private boolean ativo;
    // Referência para Receita
}
```

### Receita
Define os ingredientes necessários para um produto

```java
public class Receita {
    private long id;
    private String nome;
    private String descricao;
    private List<Ingrediente> ingredientes;
}
```

### Ingrediente
Componente básico das receitas

```java
public class Ingrediente {
    private long id;
    private String nome;
    private double valor;
}
```

### Pedido
Representa uma compra do cliente

```java
public class Pedido {
    private long id;
    private Cliente cliente;
    private List<ItemPedido> itens;
    private double valorTotal;
}
```

---

## 💻 Tecnologias

| Tecnologia | Versão | Uso |
|-----------|--------|-----|
| **Java** | 21 LTS | Linguagem principal |
| **Spring Boot** | 3.5.4 | Framework web |
| **Spring JDBC** | 3.5.4 | Acesso a dados |
| **H2 Database** | Latest | Banco de dados em memória |
| **Lombok** | Latest | Reduz boilerplate |
| **Maven** | 3.9.11 | Build & dependências |

---

## 🚀 Como Executar

### Pré-requisitos
- Java 21+ instalado
- Maven 3.9+

### Passos

1. **Clone ou navegue até o projeto:**
   ```bash
   cd "Portifólio/Quinto-Semestre/Projarc/Projarc-T1"
   ```

2. **Execute com Maven:**
   ```bash
   mvn spring-boot:run
   ```

3. **Ou compile e execute:**
   ```bash
   mvn clean install
   java -jar target/ex4-lancheriaddd-v1-0.0.1-SNAPSHOT.jar
   ```

4. **Acesse a aplicação:**
   ```
   http://localhost:8080
   ```

---

## 📡 Endpoints Principais

### Cardápio

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/cardapio/lista` | Lista todos os cardápios disponíveis |
| `GET` | `/cardapio/{id}` | Obtém cardápio específico com sugestões do chef |

**Resposta Exemplo - Lista de Cardápios:**
```json
{
  "cabecalhos": [
    {
      "id": 1,
      "titulo": "Pizzas Doces"
    },
    {
      "id": 2,
      "titulo": "Pizzas Salgadas"
    }
  ]
}
```

**Resposta Exemplo - Cardápio Específico:**
```json
{
  "titulo": "Pizzas Salgadas",
  "itens": [
    {
      "id": 1,
      "descricao": "Margherita",
      "preco": 35.90,
      "sugestaoDoChef": true
    },
    {
      "id": 2,
      "descricao": "Pepperoni",
      "preco": 42.50,
      "sugestaoDoChef": false
    }
  ]
}
```

---

## 🧪 Testabilidade

A arquitetura Clean Architecture garante alta testabilidade:

```java
// ✅ Teste unitário (sem Spring, sem DB)
@Test
void deveRecuperarCardapioDoBancoDados() {
    // Given
    CardapioRepository repositorioMock = mock(CardapioRepository.class);
    CardapioService servico = new CardapioService(repositorioMock);
    
    // When
    Cardapio resultado = servico.recuperaCardapio(1);
    
    // Then
    assertNotNull(resultado);
    verify(repositorioMock).recuperaPorId(1);
}
```

---

## 📊 Diagrama de Contexto (C4)

Atores que interagem com o sistema:

```
┌─────────────┐
│   Cliente   │ (Realiza pedidos via Web/App)
└──────┬──────┘
       │
┌──────▼──────────────────────────────┐
│   Sistema de Gerenciamento Pizzaria │
│   (Spring Boot + Clean Architecture)│
└──────┬──────────────────────────────┘
       │
   ┌───┴────┬────────┬────────┐
   │        │        │        │
┌──▼─┐  ┌──▼─┐  ┌──▼──┐  ┌──▼──┐
│Eq. │  │Admin│  │Chef │  │Entregador│
│Cozinha│ │    │  │(Sugestões)  │
└────┘  └────┘  └─────┘  └───────┘
```

---

## 🔐 Segurança (Estrutura futura)

O diagrama de contexto prevê:
- ✅ **Spring Security** na camada Edge
- ✅ **JWT/Cookies** para autenticação
- ✅ Diferentes roles: Cliente, Administrador, Cozinha, Entregador

---

## 📝 Padrões de Design Utilizados

| Padrão | Localização | Propósito |
|--------|-----------|----------|
| **Repository** | `Dominio/Dados/` | Abstração de persistência |
| **Use Case** | `Aplicacao/` | Orquestração de regras |
| **Presenter** | `Adaptadores/Apresentacao/Presenters/` | Formatação de respostas |
| **Service** | `Dominio/Servicos/` | Lógica de domínio |
| **DTO** | `Aplicacao/Responses/` | Transferência de dados |
| **Dependency Injection** | Spring Annotations | Injeção de dependências |

---

## 🎓 Aprendizados em Clean Architecture

Este projeto exemplifica:

1. ✅ **Independência de Frameworks**
   - A lógica de negócio não conhece Spring
   - Fácil trocar JDBC por JPA ou outra tecnologia

2. ✅ **Testabilidade**
   - Testes unitários sem infraestrutura
   - Mocks de repositórios

3. ✅ **Inversão de Dependência**
   - Camadas internas definem interfaces
   - Camadas externas implementam

4. ✅ **Domain-Driven Design**
   - Entidades ricas com comportamento
   - Linguagem ubíqua (Cardápio, Produto, Receita)

---

## 📚 Referências

- [Clean Architecture - Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Domain-Driven Design - Eric Evans](https://www.domainlanguage.com/)
- [Spring Boot Official Documentation](https://spring.io/projects/spring-boot)

---

## 👨‍💻 Autor

Desenvolvido como projeto educacional - Quinto Semestre

---

## 📄 Licença

Este projeto é fornecido como material educacional.

---

**Última atualização:** 6 de Maio de 2026  
**Versão Java:** 21 LTS  
**Versão Spring Boot:** 3.5.4
