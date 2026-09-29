# ☕ Erica Java Pro Agent - Sistema Multiagentes

> **Squad de IA Especialista em Engenharia de Software Java & Arquitetura Corporativa**  
> Autora: **Erica Leandra Da Silva** | Versão: **2.0.0** | Padrão: **AGENTS.md Universal**

O **Erica Java Pro Agent** é um **Sistema Multiagentes Especializado** em Java 21 LTS e Spring Boot 3. Ele não é apenas um chatbot: é uma **equipe completa de engenharia de software** que atua em conjunto para planejar, codificar, testar e auditar aplicações com padrões corporativos.

---

## 👥 O Time Multiagentes

```
                          [ Demanda do Desenvolvedor ]
                                       │
                                       ▼
                   ┌───────────────────────────────────────┐
                   │   🧠 AGENTE ORQUESTRADOR (Tech Lead)  │
                   └───────────────────┬───────────────────┘
                                       │
         ┌─────────────────────────────┼─────────────────────────────┐
         ▼                             ▼                             ▼
┌──────────────────┐          ┌──────────────────┐          ┌──────────────────┐
│   💻 EXECUTOR    │          │  🧪 EXAMINADOR   │          │   🛡️ AUDITOR     │
│   (Java Coder)   │          │   (QA & Tests)   │          │ (Security & Arch)│
└──────────────────┘          └──────────────────┘          └──────────────────┘
```

### 1. 🧠 Agente Orquestrador (Tech Lead / Arquiteta)
- Recebe os requisitos do usuário e projeta a arquitetura de alto nível.
- Divide o trabalho e delega tarefas para o Executor, Examinador e Auditor.
- Consolida as entregas e garante o alinhamento com os objetivos de negócio.

### 2. 💻 Agente Executor (Java Coder / Implementador)
- Especialista em desenvolvimento "mão na massa" com **Java 21** e **Spring Boot 3**.
- Cria a estrutura de pacotes, Use Cases, Repositories, DTOs e Controllers.
- Aplica recursos modernos: Records, Sealed Types, Pattern Matching e Virtual Threads.

### 3. 🧪 Agente Examinador (QA / Test Specialist)
- Garante que nenhuma linha de código vá para produção sem cobertura de testes.
- Escreve suítes completas de testes unitários com **JUnit 5**, **Mockito** e **AssertJ**.
- Cria testes de integração com **MockMvc** e **Testcontainers** (PostgreSQL real).
- Testa cenários de erro, valores nulos, concorrência e edge cases.

### 4. 🛡️ Agente Auditor (Security, Architecture & Quality)
- Executa auditoria técnica rigorosa antes de aprovar qualquer entrega.
- Verifica conformidade com **Clean Architecture**, **SOLID** e **DDD**.
- Detecta gargalos de performance e anti-patterns (como queries N+1 no JPA).
- Audita segurança conforme as diretrizes **OWASP Top 10**.

---

## 🏛️ Estrutura do Repositório

```
erica-java-agent/
├── README.md                                  # Apresentação do Squad Multiagentes
├── AGENTS.md                                  # Especificação oficial do Sistema Multiagentes
│
├── agents/                                    # Definição e Personas do Squad
│   ├── orquestrador/persona.md                # 🧠 Tech Lead & Coordenação
│   ├── executor/persona.md                    # 💻 Implementação Java 21 / Spring Boot 3
│   ├── examinador/persona.md                  # 🧪 Testes Automatizados, QA e Edge Cases
│   └── auditor/persona.md                     # 🛡️ Auditoria de Segurança, SOLID e Performance
│
├── agent/
│   ├── guardrails.md                          # Regras inegociáveis de engenharia de software
│   └── knowledge/                             # Base de Conhecimento Compartilhada
│       ├── effective-java-21.md               # Java 21 moderno (Records, Sealed Types, Loom)
│       ├── clean-architecture-ddd.md          # Arquitetura Hexagonal e DDD
│       ├── spring-boot-enterprise.md          # Padrões corporativos Spring Boot 3 e JPA
│       ├── jvm-memory-concurrency.md          # Virtual Threads e Garbage Collection
│       └── testcontainers-observability.md    # Testcontainers e Observabilidade
│
└── skills/                                    # Habilidades Executáveis
    ├── java-architect-review/                 # Auditoria em 5 dimensões pelo Auditor
    ├── clean-arch-scaffolder/                 # Geração de microsserviços pelo Executor
    ├── eda-transactional-outbox/              # EDA, Outbox Pattern e Idempotência pelo Squad
    ├── jvm-stacktrace-diagnostician/          # Diagnóstico de erros pelo Auditor/Executor
    ├── tdd-test-suite-crafter/                # Suítes de teste pelo Examinador
    └── virtual-threads-optimizer/             # Otimização de I/O pelo Executor/Auditor
```

---

## 👩‍💻 Autora

Idealizado e desenvolvido por **Erica Leandra Da Silva**.
