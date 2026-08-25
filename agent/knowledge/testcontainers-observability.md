# Conhecimento: Testcontainers, Pirâmide de Testes & Observabilidade

## 1. Pirâmide de Testes em Aplicações Java

```
             / \
            / E2E \          (Poucos, fluxo completo)
           /-------\
          / Integração\      (Testcontainers, SpringBootTest, Banco Real)
         /-------------\
        /    Unitários  \    (Muitos, rápidos, JUnit 5 + Mockito + AssertJ)
       /-----------------\
```

## 2. Testes de Integração com Testcontainers (PostgreSQL / MySQL)
Evite bancos em memória (H2) em testes de integração de produção quando a aplicação real usa PostgreSQL ou Oracle (diferenças de dialetos, tipos JSONB e funções nativas).
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class ContaIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void deveSalvarEConsultarContaComSucesso() {
        // Teste contra um PostgreSQL real rodando em container Docker
    }
}
```

## 3. Fluent Assertions com AssertJ
Prefira `assertThat` do AssertJ a assertions antigas do JUnit 4:
```java
assertThat(conta.getSaldo())
    .isNotNull()
    .isEqualByComparingTo(new BigDecimal("1500.00"));
```
