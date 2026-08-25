# Guardrails de Engenharia: Erica Java Pro Agent

Estas são as **regras inegociáveis** que o agente deve seguir e fazer cumprir em qualquer código Java gerado ou revisado.

---

## 🚫 Práticas Proibidas (Anti-Patterns)
1. **Entidades JPA Expostas na Web:** Nunca retorne classes com anotações `@Entity` diretamente em `@RestController`. Sempre utilize **DTOs / Records**.
2. **Injeção por `@Autowired` em Atributos (Field Injection):** Proibido. Sempre utilize **injeção por construtor** (garante imutabilidade e facilita testes unitários).
3. **Engolir Exceções:** Proibido blocos `catch (Exception e) {}` vazios ou que apenas imprimam `e.printStackTrace()`. Exceções devem ser tratadas ou relançadas em exceções semânticas de negócio.
4. **Queries N+1 no JPA:** Proibido relacionamentos `@OneToMany` com fetch EAGER indiscriminado ou iterações que disparem uma query SQL para cada elemento de uma lista. Use `JOIN FETCH`, `@EntityGraph` ou DTO Projections.
5. **Tipos Primitivos Mutáveis Compartilhados em Threads:** Nunca use estruturas não thread-safe em singletons do Spring sem a devida sincronização (`AtomicReference`, `ConcurrentHashMap` ou imutabilidade).

---

## ✅ Práticas Obrigatórias (Best Practices)
1. **Imutabilidade por Padrão:** Use `record` para DTOs, eventos e objetos de transferência de valor.
2. **Validação de Fronteira (Fail-Fast):** Use Jakarta Validation (`@Valid`, `@NotNull`, `@NotBlank`, `@Positive`) nos controllers e validações defensivas (`Objects.requireNonNull()`, `IllegalArgumentException`) nos construtores do domínio.
3. **Tratamento Centralizado:** Centralize erros em classes com `@RestControllerAdvice` retornando a especificação RFC 7807 (Problem Details) ou JSON padronizado.
4. **Logs Estruturados:** Use `org.slf4j.Logger` (`LoggerFactory.getLogger` ou `@Slf4j`) com mensagens contextualizadas, nunca `System.out.println` em código de produção.
5. **Testabilidade:** Todo `@Service` deve poder ser testado isoladamente sem levantar o contexto completo do Spring (usando `MockitoExtension`).
