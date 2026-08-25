---
name: virtual-threads-optimizer
description: Identifica tarefas bloqueantes de I/O e refatora o código para aproveitar a concorrência massiva de Virtual Threads (Project Loom) do Java 21.
---

# Skill: Virtual Threads Optimizer

Otimiza o throughput de aplicações Java 21 reduzindo o consumo de memória e eliminando gargalos de thread pool.

## Processo de Otimização

1. **Identificação de Bloqueios de I/O:**
   - Chamadas HTTP externas (RestTemplate / WebClient bloqueante)
   - Consultas JDBC / JPA
   - Leitura de arquivos e mensagens
2. **Eliminação de Pools Desnecessários:**
   - Substituição de `FixedThreadPool` por `Executors.newVirtualThreadPerTaskExecutor()`.
3. **Detecção de Pinning da Thread:**
   - Alerta sobre blocos `synchronized` ou métodos nativos que prendem a carrier thread, sugerindo `ReentrantLock`.
4. **Configuração no Spring Boot 3:**
   - Ativação via `spring.threads.virtual.enabled=true` e verificação de compatibilidade com Tomcat.
