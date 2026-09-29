---
name: eda-transactional-outbox
description: Gera arquitetura orientada a eventos resiliente com Transactional Outbox Pattern, publicação confiável (Kafka/RabbitMQ), consumo com idempotência e tratamento de Dead Letter Queue em Java 21 e Spring Boot 3.
---

# 📬 Skill: Event-Driven Architecture & Transactional Outbox Pattern

Esta skill capacita o squad a projetar e implementar sistemas orientados a eventos de missão crítica, eliminando o problema do *Dual-Write* e garantindo entrega **At-Least-Once** com consumo **Exatamente-Uma-Vez (Idempotente)**.

---

## 👥 Atribuições do Squad nesta Skill

| Agente | Responsabilidade |
| :--- | :--- |
| **🧠 Orquestrador** | Define o fluxo de eventos, contratos de mensagens (Schema) e limites de consistência eventual. |
| **💻 Executor** | Implementa a entidade `OutboxEvent`, o publicador atômico, o dispatcher com Virtual Threads e o consumidor idempotente. |
| **🧪 Examinador** | Cria testes de integração com **Testcontainers** (Kafka/RabbitMQ + PostgreSQL) simulando falhas de rede e reprocessamento. |
| **🛡️ Auditor** | Valida isolamento transacional, ausência de deadlocks na tabela outbox, índices para alta concorrência e conformidade com os Guardrails. |

---

## 🛠️ Pipeline de Implementação em 5 Etapas

```
[ Ação de Negócio ] ──(Mesma Transação ACID)──► [ Tabela de Negócio ] + [ Tabela Outbox ]
                                                                             │
                                                                 (Dispatcher Assíncrono)
                                                                             ▼
[ Consumidor Idempotente ] ◄── [ Broker: Kafka / RabbitMQ ] ◄─────── [ Message Publisher ]
```

### 1. Modelagem da Tabela e Entidade Outbox
Crie a tabela `outbox_events` no mesmo banco de dados relacional para garantir atomicidade transacional:

```java
public record OutboxEventRecord(
    UUID id,
    String aggregateType,
    String aggregateId,
    String eventType,
    String payload,
    Instant createdAt,
    OutboxStatus status
) {
    public enum OutboxStatus {
        PENDING, PUBLISHED, FAILED
    }
}
```

### 2. Emissão de Eventos Integrada à Transação
Ao salvar a entidade de domínio, o evento de domínio é persistido na tabela outbox dentro do mesmo `@Transactional`:

```java
@Service
public class CriarPedidoUseCase {

    private final PedidoRepositoryPort pedidoRepository;
    private final OutboxRepositoryPort outboxRepository;
    private final ObjectMapper objectMapper;

    public CriarPedidoUseCase(
        PedidoRepositoryPort pedidoRepository,
        OutboxRepositoryPort outboxRepository,
        ObjectMapper objectMapper
    ) {
        this.pedidoRepository = pedidoRepository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public PedidoResponse executar(CriarPedidoCommand command) {
        Pedido pedido = Pedido.criar(command.clienteId(), command.itens());
        pedidoRepository.salvar(pedido);

        PedidoCriadoEvent event = new PedidoCriadoEvent(pedido.getId(), pedido.getValorTotal());
        String payloadJson = converterParaJson(event);

        OutboxEventRecord outbox = new OutboxEventRecord(
            UUID.randomUUID(),
            "Pedido",
            pedido.getId().toString(),
            "PedidoCriadoEvent",
            payloadJson,
            Instant.now(),
            OutboxStatus.PENDING
        );
        outboxRepository.salvar(outbox);

        return new PedidoResponse(pedido.getId(), pedido.getStatus());
    }

    private String converterParaJson(Object event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new EventSerializationException("Erro ao serializar evento para Outbox", e);
        }
    }
}
```

### 3. Outbox Dispatcher com Virtual Threads
Processa eventos pendentes e publica no broker com retry exponencial e garantia de não-bloqueio:

```java
@Component
public class OutboxMessageDispatcher {

    private static final Logger log = LoggerFactory.getLogger(OutboxMessageDispatcher.class);

    private final OutboxRepositoryPort outboxRepository;
    private final MessageBrokerPublisherPort brokerPublisher;

    public OutboxMessageDispatcher(
        OutboxRepositoryPort outboxRepository,
        MessageBrokerPublisherPort brokerPublisher
    ) {
        this.outboxRepository = outboxRepository;
        this.brokerPublisher = brokerPublisher;
    }

    @Scheduled(fixedDelay = 1000)
    public void despacharEventosPendentes() {
        List<OutboxEventRecord> pendentes = outboxRepository.buscarPendentesComLock(50);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (OutboxEventRecord evento : pendentes) {
                executor.submit(() -> processarEvento(evento));
            }
        }
    }

    private void processarEvento(OutboxEventRecord evento) {
        try {
            brokerPublisher.publicar(evento.eventType(), evento.aggregateId(), evento.payload());
            outboxRepository.marcarComoPublicado(evento.id(), Instant.now());
            log.info("Evento outbox publicado com sucesso: id={}, tipo={}", evento.id(), evento.eventType());
        } catch (Exception ex) {
            log.error("Falha ao publicar evento outbox: id={}", evento.id(), ex);
            outboxRepository.marcarComoFalha(evento.id(), ex.getMessage());
        }
    }
}
```

### 4. Consumo com Deduplicação e Idempotência
Garante que mensagens duplicadas enviadas pelo broker sejam ignoradas sem efeitos colaterais:

```java
@Component
public class PedidoCriadoConsumer {

    private static final Logger log = LoggerFactory.getLogger(PedidoCriadoConsumer.class);

    private final ProcessarFaturamentoUseCase faturamentoUseCase;
    private final IdempotencyRepositoryPort idempotencyRepository;

    public PedidoCriadoConsumer(
        ProcessarFaturamentoUseCase faturamentoUseCase,
        IdempotencyRepositoryPort idempotencyRepository
    ) {
        this.faturamentoUseCase = faturamentoUseCase;
        this.idempotencyRepository = idempotencyRepository;
    }

    @Transactional
    public void consumir(String messageId, String payload) {
        if (idempotencyRepository.jaProcessado(messageId)) {
            log.warn("Mensagem duplicada detectada e ignorada: messageId={}", messageId);
            return;
        }

        faturamentoUseCase.processar(payload);
        idempotencyRepository.registrarProcessamento(messageId, Instant.now());
    }
}
```

### 5. Resiliência e Dead Letter Queue (DLQ)
- Configuração de políticas de retry com backoff exponencial no broker.
- Encaminhamento automático para DLQ após N tentativas sem sucesso.
- Alertas e métricas com Micrometer/Prometheus para monitorar taxa de eventos com erro.

---

## 🧪 Estratégia de Testes Automatizados (Examinador)

```java
@Testcontainers
@SpringBootTest
class OutboxPatternIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));

    @Test
    @DisplayName("Deve salvar evento na outbox e publicar no Kafka garantindo consistência")
    void deveSalvarOutboxEPublicarKafka() {
        // Given: Comando de criação de pedido
        // When: UseCase executado
        // Then: Pedido persistido, Outbox gravado e mensagem recebida no tópico Kafka
    }
}
```

---

## 🛡️ Checklist do Auditor (Critérios de Aprovação)

- [ ] **Atomicidade:** A entidade de negócio e o registro outbox são persistidos sob a mesma anotação `@Transactional`.
- [ ] **Locking Seguro:** O dispatcher utiliza `SELECT ... FOR UPDATE SKIP LOCKED` para evitar concorrência entre instâncias do microsserviço.
- [ ] **Idempotência no Consumidor:** Há tabela de chave de idempotência com `UNIQUE CONSTRAINT` para evitar reprocessamento indevido.
- [ ] **Zero DTOs com Entidades:** Payloads são serializados a partir de Java Records específicos de evento de domínio.
- [ ] **Virtual Threads:** O dispatcher utiliza `newVirtualThreadPerTaskExecutor()` para alto throughput de publicação I/O-bound.
