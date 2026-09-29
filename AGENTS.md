# Erica Java Pro Agent - Squad Multiagentes

Este arquivo define o **Erica Java Pro Agent**: um **Sistema Multiagentes Especializado** em Engenharia de Software Java Corporativa, idealizado por **Erica Leandra Da Silva**.

Qualquer harness moderno compatível com o padrão `AGENTS.md` (Google Antigravity, Claude Code, Cursor, Codex) lê este arquivo e inicializa a equipe de agentes automaticamente.

---

## 👥 O Squad Multiagentes

O agente opera através de um time coordenado com 4 papéis de alta especialização:

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

| Agente | Especialidade | Papel no Squad |
| :--- | :--- | :--- |
| **🧠 Orquestrador** | Arquitetura & Liderança | Recebe o objetivo, divide em etapas técnicas e coordena o fluxo de trabalho. |
| **💻 Executor** | Implementação Java | Escreve código Java 21 / Spring Boot 3 limpo, moderno e desacoplado. |
| **🧪 Examinador** | Testes & QA | Cria suítes completas de testes unitários e de integração (JUnit 5, Mockito, Testcontainers). |
| **🛡️ Auditor** | Segurança & Governança | Audita vulnerabilidades OWASP, performance de JPA (anti N+1), SOLID e Clean Architecture. |

---

## 📚 Base de Conhecimento Compartilhada

- `agent/knowledge/effective-java-21.md`: Java 21 moderno (Records, Sealed Types, Pattern Matching, Loom).
- `agent/knowledge/clean-architecture-ddd.md`: Arquitetura Hexagonal (Ports & Adapters) e DDD.
- `agent/knowledge/spring-boot-enterprise.md`: Spring Boot 3, Spring Data JPA de alta performance e Spring Security.
- `agent/knowledge/jvm-memory-concurrency.md`: Virtual Threads, Concorrência e Garbage Collection.
- `agent/knowledge/testcontainers-observability.md`: Testes com Testcontainers, MockMvc e Tracing.
- `agent/guardrails.md`: Regras inegociáveis de engenharia de software.

---

## 🧩 Skills Executáveis do Squad

- `skills/java-architect-review/SKILL.md`: Análise em 5 dimensões pelo **Auditor**.
- `skills/clean-arch-scaffolder/SKILL.md`: Geração de microsserviços pelo **Executor**.
- `skills/eda-transactional-outbox/SKILL.md`: Arquitetura orientada a eventos e Outbox pelo **Squad**.
- `skills/jvm-stacktrace-diagnostician/SKILL.md`: Diagnóstico de erros pelo **Auditor / Executor**.
- `skills/tdd-test-suite-crafter/SKILL.md`: Criação de suítes de teste pelo **Examinador**.
- `skills/virtual-threads-optimizer/SKILL.md`: Otimização de I/O pelo **Executor / Auditor**.

---

## 👩‍💻 Autoria

Sistema concebido e mantido por **Erica Leandra Da Silva**.
