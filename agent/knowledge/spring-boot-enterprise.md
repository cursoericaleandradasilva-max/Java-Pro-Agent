# Conhecimento: Spring Boot 3 Enterprise & JPA de Alta Performance

## 1. Evitando o Problema de Queries N+1 no JPA
O problema N+1 ocorre quando o Hibernate executa 1 query para buscar uma lista e mais N queries para carregar relacionamentos de cada elemento.

### Solução 1: `JOIN FETCH` explícito em JPQL
```java
@Query("SELECT c FROM Conta c JOIN FETCH c.transacoes WHERE c.id = :id")
Optional<Conta> buscarPorIdComTransacoes(@Param("id") Long id);
```

### Solução 2: `@EntityGraph`
```java
@EntityGraph(attributePaths = {"transacoes", "cliente"})
List<Conta> findAll();
```

### Solução 3: DTO Projections para consultas somente leitura
```java
public interface ResumoContaProjection {
    Long getId();
    String getTitular();
    BigDecimal getSaldo();
}
```

## 2. Injeção de Dependência Limpa
Prefira injeção por construtor em conjunto com campos `final`:
```java
@Service
public class TransferenciaService {

    private final ContaRepositoryPort repository;
    private final NotificadorPort notificador;

    public TransferenciaService(ContaRepositoryPort repository, NotificadorPort notificador) {
        this.repository = repository;
        this.notificador = notificador;
    }
}
```

## 3. Transações Declarativas com `@Transactional`
- Use `@Transactional(readOnly = true)` para métodos de consulta (ativa otimizações no Hibernate como flush mode MANUAL).
- Use `@Transactional` apenas no nível de serviço onde ocorrem mutações que precisam de atomicidade.
