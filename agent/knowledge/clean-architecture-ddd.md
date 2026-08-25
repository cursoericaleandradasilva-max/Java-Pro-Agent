# Conhecimento: Clean Architecture & Domain-Driven Design (DDD)

## 1. Princípio da Inversão de Dependência (Hexagonal / Ports & Adapters)

Na Clean Architecture, a regra fundamental é:
> **As camadas internas (Domínio e Casos de Uso) nunca conhecem nem dependem das camadas externas (Banco de Dados, Frameworks, Web, Filas).**

```
     [ Adaptadores Primários / Inbound ]
              (REST Controllers, CLI, Mensagens)
                         │
                         ▼
             [ Portas de Entrada (Input Ports) ]
                         │
                         ▼
               [ Casos de Uso (Use Cases) ]  <--- Regras de Aplicação
                         │
                         ▼
                 [ Entidades de Domínio ]    <--- Regras de Negócio Puras
                         ▲
                         │
            [ Portas de Saída (Output Ports) ]
                         ▲
                         │
     [ Adaptadores Secundários / Outbound ]
       (Spring Data JPA Repositories, HTTP Clients, Kafka)
```

## 2. Estrutura de Pacotes Recomendada

```
com.empresa.modulo/
├── domain/                    # 100% Java puro, sem annotations de frameworks
│   ├── model/                 # Entidades e Value Objects
│   │   ├── Conta.java
│   │   └── Dinheiro.java      # Value Object imutável
│   └── exception/             # Exceções de Domínio
├── application/               # Casos de Uso da Aplicação
│   ├── usecase/
│   │   ├── CriarContaUseCase.java
│   │   └── RealizarTransferenciaUseCase.java
│   └── port/
│       ├── in/                # Interfaces de entrada
│       └── out/               # Interfaces de saída (ex: ContaRepositoryPort)
└── infrastructure/            # Adaptadores externos
    ├── adapter/
    │   ├── in/web/            # RestControllers, DTOs
    │   └── out/persistence/   # JPA Entities, Spring Data Repositories, Mappers
    └── config/                # Beans de Configuração do Spring
```
