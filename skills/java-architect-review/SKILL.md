---
name: java-architect-review
description: Executa uma auditoria profunda e revisão de código Java em 5 dimensões (Arquitetura, Performance, Segurança, Clean Code e Testabilidade).
---

# Skill: Java Architect Review

Analisa código Java e gera um parecer técnico estruturado de nível Tech Lead / Arquiteto Sênior.

## Processo de Avaliação em 5 Pilares

1. **🏛️ Arquitetura & Design:**
   - O código respeita o princípio de responsabilidade única (SRP)?
   - Existe vazamento de entidades JPA para a camada web?
   - As dependências são injetadas por construtor?

2. **⚡ Performance & Recursos:**
   - Existem riscos de queries N+1 no JPA ou iterações desnecessárias?
   - O uso de coleções é adequado para o volume de dados?
   - Operações de I/O bloqueante poderiam se beneficiar de Virtual Threads?

3. **🔒 Segurança & Validação:**
   - As entradas externas são validadas de forma defensiva (Bean Validation)?
   - Há proteção contra injeção de SQL ou exposição indevida de dados sensíveis?

4. **🧹 Clean Code & Modern Java:**
   - O código utiliza Java 21 moderno (Records, Sealed Types, Pattern Matching)?
   - As exceções são semânticas ou genéricas?

5. **🧪 Testabilidade:**
   - A classe pode ser facilmente testada com Mocks sem acoplamento oculto?

## Formato do Relatório de Saída

- **Resumo Executivo (Nota de 1 a 10 e status de aprovação)**
- **Pontos Fortes Identificados**
- **Oportunidades de Melhoria (com diffs de código antes vs. depois)**
- **Recomendação Final do Arquiteto**
