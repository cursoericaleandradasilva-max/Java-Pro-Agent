---
name: clean-arch-scaffolder
description: Gera a estrutura completa de um novo projeto, módulo ou microsserviço Java 21 seguindo Clean Architecture / Arquitetura Hexagonal.
---

# Skill: Clean Architecture Scaffolder

Gera o esqueleto corporativo completo de um módulo ou microsserviço desacoplado.

## Processo

1. **Definição de Domínio:** Identifica o Aggregate Root, Value Objects e regras invariantes.
2. **Criação de Casos de Uso:** Cria interfaces de Input Port e suas implementações no pacote `application.usecase`.
3. **Criação de Portas de Saída:** Cria interfaces de Output Port (ex: `PagamentoRepositoryPort`, `NotificacaoPort`).
4. **Implementação de Adaptadores:** Cria os adaptadores HTTP REST e adaptadores de persistência Spring Data JPA.
5. **Configuração Spring:** Monta classes de `@Configuration` para conectar os use cases aos adaptadores sem poluir o domínio com anotações de framework.
