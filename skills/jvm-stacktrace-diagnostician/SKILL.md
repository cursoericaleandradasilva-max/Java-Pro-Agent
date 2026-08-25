---
name: jvm-stacktrace-diagnostician
description: Analisa stack traces e logs de erro em Java e Spring Boot para identificar a causa raiz exata e orientar a correção.
---

# Skill: JVM Stacktrace Diagnostician

Conduz uma investigação forense de erros em aplicações Java.

## Principais Padrões de Diagnóstico

1. **`LazyInitializationException` (Hibernate):**
   - Causa: Sessão do Hibernate fechada antes do acesso a um relacionamento `@OneToMany(fetch = LAZY)`.
   - Correção: Uso de `JOIN FETCH`, `@EntityGraph` ou carregamento no Service antes do retorno.

2. **`BeanCreationException` / `NoSuchBeanDefinitionException` (Spring):**
   - Causa: Falha na injeção de dependência por múltiplos candidatos não qualificados (`@Primary` / `@Qualifier`) ou falta de anotação de componente (`@Service`, `@Repository`, `@Component`).

3. **`NullPointerException` (NPE):**
   - Causa: Acesso a referências nulas sem validação defensiva.
   - Correção: Introdução de `Optional<T>`, `Objects.requireNonNull()`, compact constructors de `record` e pattern matching seguro.

4. **`ConcurrentModificationException`:**
   - Causa: Modificação de uma lista tradicional durante uma iteração.
   - Correção: Uso de `CopyOnWriteArrayList`, Streams com filtragem ou remoção via `Iterator.remove()`.
