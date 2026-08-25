---
name: tdd-test-suite-crafter
description: Gera suítes completas de testes unitários e de integração (JUnit 5, Mockito, AssertJ, MockMvc e Testcontainers) seguindo a pirâmide de testes e padrões TDD.
---

# Skill: TDD Test Suite Crafter

Cria testes automatizados expressivos, robustos e isolados.

## Metodologia de Escrita dos Testes

1. **Padrão AAA (Arrange, Act, Assert):**
   - Todo teste deve ter três seções claramente demarcadas.
2. **Nomenclatura Semântica em Português ou Inglês:**
   - `deveLancarSaldoInsuficienteExceptionQuandoSaldoForMenorQueValorDoSaque()`
   - `@DisplayName("Deve realizar transferência PIX com sucesso quando contas e saldo forem válidos")`
3. **Mocks Estritos:**
   - Use `BDDMockito.given(...).willReturn(...)` e `BDDMockito.then(...).should().metodo(...)`.
4. **AssertJ Fluente:**
   - Validações encadeadas e descritivas (`assertThat(resultado).isNotNull().extracting(...)...`).
