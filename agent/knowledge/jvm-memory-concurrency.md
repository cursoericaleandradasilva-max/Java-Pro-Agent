# Conhecimento: JVM, Concorrência Moderna & Virtual Threads (Project Loom)

## 1. Virtual Threads (Java 21)
No modelo tradicional (Platform Threads), cada thread do Java é mapeada 1:1 para uma thread do Sistema Operacional (pesada, ~1MB de stack).
Com **Virtual Threads**, a JVM gerencia milhões de threads leves na Heap com custo desprezível.

### Habilitando Virtual Threads no Spring Boot 3.2+:
Em `application.properties`:
```properties
spring.threads.virtual.enabled=true
```

### Criando Virtual Threads em código:
```java
// Executor assíncrono com Virtual Threads
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    IntStream.range(0, 10_000).forEach(i -> {
        executor.submit(() -> {
            // Operação de I/O bloqueante (chamada HTTP, query no banco)
            realizarChamadaExterna();
        });
    });
}
```

## 2. Cuidados com Virtual Threads
- **Não use Pool de Virtual Threads:** Virtual Threads são descartáveis; crie sob demanda.
- **Cuidado com Pinning (`synchronized`):** Em código de I/O de alta concorrência com Virtual Threads, prefira `ReentrantLock` no lugar de blocos `synchronized` para evitar o pinning da carrier thread.
