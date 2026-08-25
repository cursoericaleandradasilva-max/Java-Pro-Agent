# ☕ Erica Java Pro Agent

> **Agente Especialista em Engenharia de Software Java & Arquitetura Corporativa**  
> Autora: **Erica Leandra Da Silva** | Versão: **1.0.0** | Padrão: **AGENTS.md Universal**

O **Erica Java Pro Agent** é um agente de Inteligência Artificial desenhado especificamente para atuar como um **Arquiteto de Software Java Sênior / Tech Lead**. Ele guia desenvolvedores na criação, refatoração, teste e otimização de aplicações corporativas modernas utilizando **Java 21 (LTS)** e **Spring Boot 3**.

---

## 🎯 Por que este Agente é Diferente?

Ao contrário de assistentes genéricos, o **Erica Java Pro Agent** foi projetado com uma base de conhecimento profunda e regras inegociáveis de engenharia:

- 🛡️ **Clean Architecture & DDD:** Separação estrita de Domínio, Casos de Uso, Portas e Adaptadores.
- ⚡ **Java 21 Moderno:** Foco em Virtual Threads (Project Loom), Records, Sealed Interfaces, Pattern Matching e Streams.
- 🏎️ **Performance & JPA:** Prevenção proativa de problemas de performance (como N+1 em queries SQL) e tuning de JVM.
- 🧪 **Cultura TDD:** Criação de testes unitários e de integração realistas usando **JUnit 5**, **Mockito**, **AssertJ** e **Testcontainers**.
- 🔒 **Segurança Corporativa (OWASP):** Sanitização, autenticação robusta (OAuth2/JWT) e boas práticas de proteção de dados.

---

## 🏛️ Arquitetura do Agente

```
erica-java-agent/
├── README.md                                  # Documentação e guia completo de uso
├── AGENTS.md                                  # Especificação oficial do Agente (Harness Standard)
├── CLAUDE.md                                  # Compatibilidade com múltiplos harnesses
│
├── agent/
│   ├── persona.md                             # Personalidade e tom: Arquiteta Java Sênior
│   ├── guardrails.md                          # Regras inegociáveis de qualidade, segurança e design
│   └── knowledge/
│       ├── effective-java-21.md               # Guia definitivo de Java 21 (Records, Sealed Types, Loom)
│       ├── clean-architecture-ddd.md          # Arquitetura Hexagonal (Ports & Adapters) e DDD
│       ├── spring-boot-enterprise.md          # Padrões corporativos Spring Boot 3, JPA e Spring Security
│       ├── jvm-memory-concurrency.md          # Concorrência moderna, Virtual Threads e GC Tuning
│       └── testcontainers-observability.md    # Testes com Testcontainers, Micrometer e Tracing
│
├── skills/
│   ├── README.md                              # Guia das habilidades do agente
│   ├── java-architect-review/                 # Skill: Análise arquitetural em 5 pilares
│   │   └── SKILL.md
│   ├── clean-arch-scaffolder/                 # Skill: Geração de microsserviços em Clean Architecture
│   │   └── SKILL.md
│   ├── jvm-stacktrace-diagnostician/          # Skill: Diagnóstico profundo de erros da JVM e Spring
│   │   └── SKILL.md
│   ├── tdd-test-suite-crafter/                # Skill: Criação de suítes de teste (JUnit 5 + Mockito + Testcontainers)
│   │   └── SKILL.md
│   └── virtual-threads-optimizer/             # Skill: Otimização de I/O com Virtual Threads
│       └── SKILL.md
│
└── docs/
    ├── architecture-decision-records.md       # Templates de ADR para documentação técnica
    └── java-style-guide.md                   # Guia de estilo e convenções de código
```

---

## 🚀 Como Usar este Agente

Como o agente segue o padrão aberto **`AGENTS.md`**, ele funciona nativamente em qualquer harness moderno (Google Antigravity, Claude Code, Cursor, Codex, etc.).

### Exemplos de Prompts:

- **Revisão de Código:**
  > *"Avalie a classe UserService.java segundo os 5 pilares do java-architect-review."*

- **Criar Microsserviço:**
  > *"Gere a estrutura de um microsserviço de Pagamentos com Clean Architecture e Spring Boot 3."*

- **Diagnosticar Erro:**
  > *"Estou recebendo LazyInitializationException no carregamento de pedidos. Diagnostique a causa raiz."*

- **Criar Testes:**
  > *"Escreva a suíte completa de testes unitários e de integração com MockMvc para o ContaController."*

---

## 👩‍💻 Autora

Criado e mantido por **Erica Leandra Da Silva**.
