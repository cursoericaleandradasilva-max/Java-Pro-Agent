# Conhecimento: Java 21 Moderno & Boas Práticas (Effective Java)

## 1. Records para Dados Imutáveis
Records eliminam boilerplate (getters, equals, hashCode, toString) e garantem imutabilidade semântica:
```java
public record TransferenciaRequest(
    @NotNull Long contaOrigem,
    @NotNull Long contaDestino,
    @Positive BigDecimal valor
) {
    // Compact constructor para validação defensiva
    public TransferenciaRequest {
        Objects.requireNonNull(contaOrigem, "Conta de origem não pode ser nula");
        Objects.requireNonNull(contaDestino, "Conta de destino não pode ser nula");
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor deve ser estritamente positivo");
        }
    }
}
```

## 2. Sealed Classes & Interfaces
Permitem restringir a hierarquia de tipos para modelagem precisa de domínios finitos:
```java
public sealed interface StatusTransacao permits Sucesso, Falha, Pendente {}

public record Sucesso(String codigoTransacao, LocalDateTime timestamp) implements StatusTransacao {}
public record Falha(String motivo, String codigoErro) implements StatusTransacao {}
public record Pendente(LocalDateTime expiraEm) implements StatusTransacao {}
```

## 3. Pattern Matching para `switch`
Permite desconstrução elegante e exaustiva de tipos:
```java
public String processarStatus(StatusTransacao status) {
    return switch (status) {
        case Sucesso s -> "Aprovada: " + s.codigoTransacao();
        case Falha f   -> "Rejeitada: " + f.motivo();
        case Pendente p -> "Aguardando confirmação até " + p.expiraEm();
    }; // O compilador garante que todos os casos foram cobertos (exhaustiveness check)
}
```

## 4. Sequenced Collections (Java 21)
Acesso uniforme ao primeiro e último elemento:
- `collection.getFirst()` e `collection.getLast()`
- `collection.reversed()`
