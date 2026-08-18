# Spring 학습 커리큘럼 & 최종 목표 체크리스트

> 이 파일도 **자동 로드되지 않는다.** CLAUDE.md의 "현재 Phase" 포인터가 가리키는
> 단계만 필요할 때 읽는다. 개념 상세는 `spring-concepts.md` 참조.

---

# 74. Spring 학습 커리큘럼

## Phase 0 — 개발환경

* JDK
* Gradle
* Maven
* IntelliJ IDEA
* Git
* Spring Initializr
* Docker
* Oracle / PostgreSQL
* 프로젝트 실행 구조

Java 문법은 최소화한다.

---

## Phase 1 — Spring 이전의 Plain Java

* Object Creation
* Dependency
* Manual Wiring
* Interface
* Composition
* Proxy Pattern
* Reflection
* Annotation

목표:

> Spring이 해결하는 문제를 먼저 직접 경험한다.

---

## Phase 2 — IoC / DI

* IoC
* DI
* Constructor Injection
* Bean
* Container
* BeanFactory
* ApplicationContext
* Dependency Resolution

---

## Phase 3 — Bean Lifecycle

* BeanDefinition
* Component Scan
* Bean Creation
* Initialization
* Destruction
* `@PostConstruct`
* `@PreDestroy`
* BeanPostProcessor
* BeanFactoryPostProcessor
* Aware Interface

---

## Phase 4 — Configuration

* `@Configuration`
* `@Bean`
* `@Component`
* `@ComponentScan`
* Configuration Class
* Full / Lite Configuration
* Profile
* Environment
* PropertySource
* `@ConfigurationProperties`

---

## Phase 5 — Scope / Advanced DI

* Singleton
* Prototype
* Request Scope
* Session Scope
* `@Primary`
* `@Qualifier`
* Collection Injection
* ObjectProvider
* Circular Dependency
* Lazy Initialization

---

## Phase 6 — AOP / Proxy

* Proxy Pattern
* JDK Dynamic Proxy
* CGLIB
* AOP
* Aspect
* Advice
* Pointcut
* Advisor
* Proxy
* Self Invocation

---

## Phase 7 — Transaction

* JDBC Transaction
* Transaction Boundary
* PlatformTransactionManager
* `@Transactional`
* Transaction Interceptor
* Propagation
* Isolation
* Rollback
* Read Only
* Connection Binding

---

## Phase 8 — Spring MVC

* Servlet
* Tomcat
* Filter
* DispatcherServlet
* HandlerMapping
* HandlerAdapter
* Controller
* ArgumentResolver
* ReturnValueHandler
* HttpMessageConverter
* Jackson
* Interceptor

---

## Phase 9 — Spring Boot

* SpringApplication
* Auto Configuration
* Starter
* Conditional
* Embedded Tomcat
* Configuration Properties
* Profiles
* Actuator
* Application Startup

---

## Phase 10 — Validation / Exception

* Bean Validation
* `@Valid`
* `@Validated`
* Custom Constraint
* Exception Handler
* `@ControllerAdvice`
* `@RestControllerAdvice`
* Error Response
* Problem Detail

---

## Phase 11 — Database

* JDBC
* DataSource
* Connection
* Connection Pool
* HikariCP
* JdbcTemplate
* SQL
* Transaction
* Exception Translation

---

## Phase 12 — JPA / Hibernate

* JPA
* Hibernate
* Entity
* EntityManager
* Persistence Context
* Dirty Checking
* Flush
* Commit
* Lazy Loading
* Proxy
* Relationship
* Fetch Join
* N+1
* Lock
* Batch

---

## Phase 13 — Spring Data

* Repository
* Spring Data JPA
* Query Method
* JPQL
* Native Query
* Specification
* Pagination
* Sorting
* Transaction Integration

---

## Phase 14 — Spring Security

* Authentication
* Authorization
* SecurityContext
* SecurityContextHolder
* AuthenticationManager
* AuthenticationProvider
* UserDetails
* PasswordEncoder
* SecurityFilterChain
* JWT
* Session
* CSRF
* CORS

---

## Phase 15 — Testing

* JUnit
* Mockito
* Spring TestContext
* `@SpringBootTest`
* `@WebMvcTest`
* `@DataJpaTest`
* MockMvc
* Testcontainers
* Unit Test
* Integration Test
* Test Fixture

---

## Phase 16 — Async / Concurrency

* Thread
* Thread Pool
* Executor
* TaskExecutor
* `@Async`
* CompletableFuture
* Scheduling
* `@Scheduled`
* Race Condition
* Transaction + Async

---

## Phase 17 — Cache / Integration

* Spring Cache
* `@Cacheable`
* Redis
* HTTP Client
* RestClient
* WebClient
* Timeout
* Retry
* Circuit Breaker
* Idempotency

---

## Phase 18 — Architecture

* Layered Architecture
* Hexagonal Architecture
* Clean Architecture
* Dependency Rule
* Domain
* UseCase
* Repository
* Port
* Adapter
* Modular Monolith
* Package by Feature

---

## Phase 19 — Production

* Docker
* Docker Compose
* Nginx
* JVM
* Embedded Tomcat
* Environment
* Logging
* Actuator
* Metrics
* Tracing
* Health Check
* CI/CD
* Monitoring
* 장애 대응

---

# 74-B. Phase 20 이후 — Production Backend Engineering

Phase 0~19는 **Spring 내부 원리**를 실험 코드로 팠다.
Phase 20부터는 성격이 바뀐다. "기술 학습 커리큘럼"이 아니라
**Production-Grade Backend Engineering Training**이다.

## 최종 목표 재정의

목표는 "Spring을 잘 쓰는 개발자"가 아니다.

> **복잡한 요구사항을 받아 시스템을 설계하고, 트래픽 / 동시성 / 장애 / 데이터 정합성 / 성능 / 배포 / 관측성을 고려해 Production에서 운영 가능한 시스템을 만들고, 부하를 주고, 장애를 발생시키고, 관측하고, 원인을 분석하고, 복구하고, 재발 방지까지 할 수 있는 Backend Engineer**

기준선:

```text
단순 CRUD를 만들 수 있다           → 부족
Spring을 사용할 수 있다            → 부족
Redis / Kafka를 사용할 수 있다     → 부족
Docker / Kubernetes를 쓸 수 있다   → 부족

"이 상황에서는 Kafka를 쓰지 않는다"를
근거와 수치를 들어 판단할 수 있다   → 이것이 목표
```

기술을 쓸 줄 아는 것보다 **쓰지 않기로 결정할 수 있는 것**이 더 어렵고 더 중요하다.
복잡도는 좋은 시스템의 부산물이지 목표가 아니다. 복잡도 자체를 목표로 삼으면 과설계로 간다.

## 대원칙 — 문제가 기술을 부른다

기술을 먼저 배우고 쓸 곳을 찾지 않는다. 반대로 한다.

```text
서비스 운영
    ↓
문제 발생 (느림 / 터짐 / 데이터 깨짐)
    ↓
측정으로 원인 특정
    ↓
해결책 도입 (Index / Lock / Redis / Kafka / ...)
    ↓
다시 측정해서 효과 증명
```

금지:

* "Redis 배우기", "Kafka 배우기" 같은 기술 중심 Phase 진행
* 서비스에 연결되지 않는 `main()` 실험만 만들고 넘어가기
* 병목을 측정하지 않은 채 캐시 / 비동기 / 인덱스 도입
* 효과를 재측정하지 않고 "개선했다"고 끝내기

**측정하지 않은 최적화는 완료로 인정하지 않는다.**

특히 순서가 중요하다. 캐시는 "인덱스로도 못 고치는 부하"에 쓰는 수단이다.
실행 계획을 읽기 전에 Redis부터 잡으면, 인덱스 하나면 끝날 쿼리에 캐시를 씌우고
그 대가로 캐시 무효화 버그 / 정합성 문제 / 인프라 하나를 영구히 떠안는다.
그래서 Database가 Redis보다 먼저다.

## 튜터 방식 (확정)

**전부 직설.** 소크라테스식 질문으로 유도하지 않는다.

* 개념 / 동작 방식 / 내부 원리 → 바로 설명한다. 되묻지 않는다
* 설계 결정 → 결정안과 trade-off를 먼저 제시하고, 사용자가 뒤집을 수 있게 한다
* "왜 그런지 설명해봐" 식 퀴즈 금지
* "일부러 잘못된 설계를 제시해서 찾게 하기" 금지 — 못 잡으면 틀린 것을 학습한다

대신 설계를 제시할 때는 항상 **어느 규모까지 유효하고 언제 무너지는지**를 함께 말한다.

## 역할 분담

```text
Infra / 설정 파일           → Claude 작성 가능
  docker-compose, Dockerfile, build.gradle, application.yml,
  Flyway 마이그레이션 SQL, k6 스크립트, Prometheus 규칙, CI 설정

Production Code             → 사용자가 직접 작성
  Entity, Repository, Service, Controller, Config Class, 도메인 로직

측정 / 실행 / 로그 분석      → Claude 가능
```

§5의 "한 번에 하나의 핵심 Production Code 파일" 원칙은 그대로 유지한다.

## 코드 리뷰 관점

작성한 코드는 "동작하는가"만 보지 않는다. 다음 관점으로 리뷰한다.

```text
Correctness        맞게 동작하는가
Concurrency        동시 요청에서도 맞는가
Data Consistency   중간에 끊겨도 데이터가 깨지지 않는가
Failure Handling   의존 대상이 죽으면 어떻게 되는가
Performance        부하에서 어떻게 되는가
Scalability        인스턴스를 늘리면 해결되는 구조인가
Reliability        재시도 / 타임아웃 / 멱등성이 있는가
Observability      장애 시 원인을 추적할 수 있는가
Security           인증 / 인가 / 입력 검증 / 비밀 관리
Maintainability    6개월 뒤에 읽을 수 있는가
```

## 산출 문서

Phase가 진행되면서 아래 문서를 직접 작성한다. 코드보다 이쪽이 시니어와 주니어를 가른다.

```text
Requirements                  요구사항, 제약, 비기능 요구 (SLA / 정합성 / 예산)
Capacity Estimation           예상 트래픽에서 커넥션 / 메모리 / 스토리지 산정
Data Model                    스키마 설계와 그 이유
API Specification             엔드포인트, 에러 계약, 버저닝
Architecture Decision Record  왜 A가 아니라 B를 골랐는가
Sequence Diagram              핵심 흐름
Failure Scenario              무엇이 어떻게 죽고, 그때 시스템은 어떻게 동작하는가
Load Test Plan                무엇을 어떤 부하로 측정하는가
Deployment Strategy           배포 / 롤백
Monitoring Strategy           무엇을 재고 언제 알릴 것인가
Incident Report               장애 postmortem
```

## Phase 종료 조건

각 Phase는 "개념을 배웠다"로 끝나지 않는다. 다음 중 하나를 수행해야 종료한다.

* **Mini Project** — 그 Phase의 기술이 실제로 필요한 기능 하나를 서비스에 추가
* **Production Incident** — 장애를 주입하고, 지표로 원인을 특정하고, 복구하고, Incident Report 작성

## 대상 서비스

재고 / 예약 서비스 (Inventory Reservation).

이 도메인을 고른 이유: **동시성 문제가 도메인에 내장**되어 있다.
재고 1개에 동시 요청 1000개가 오면 oversell이 실제로 발생한다.
락, 트랜잭션, 캐시 일관성, 멱등성, 분산락이 전부 억지 예제 없이 자연스럽게 등장한다.

---

## Phase 20 — 서비스 뼈대 + 설계

문제: 19개 Phase 동안 만든 게 전부 실험 코드다. 실제 앱은 `/hello`를 반환하고 DB는 H2 in-memory다.

* **요구사항 정의** — 예상 트래픽, SLA, 정합성 요구 수준을 먼저 문서로
* **용량 산정** — 목표 RPS에서 커넥션 / 메모리 / 스토리지 back-of-envelope 계산
* **데이터 모델링** — 스키마 설계와 근거 (정규화 수준, 인덱스 후보, 제약조건)
* **API 설계** — 엔드포인트, 에러 계약, 멱등성 키, 버저닝
* **API 문서화** — springdoc-openapi로 OpenAPI 스펙 자동 생성. 단, "Swagger 붙이기"로 끝내지 않는다. `@RestController` / `@RequestMapping` / DTO / `@Valid` 메타데이터를 어떤 시점에 누가 읽어서 스펙 JSON을 만드는지, 코드와 문서가 어긋나는 지점은 어디인지까지 본다.
* 실제 도메인 (Product / Reservation)
* Postgres (H2 in-memory 제거)
* Flyway 마이그레이션 (`ddl-auto` 제거, 스키마가 source of truth)
* 계층 구조 (api / application / domain / infrastructure)
* Testcontainers 통합 테스트
* 기존 JWT / Docker / Nginx / Prometheus / CI를 실제로 사용

완료 기준: `POST /api/products/{id}/reservations`가 진짜 Postgres에 커밋되고, 통합 테스트가 실제 Postgres로 돈다.

---

## Phase 21 — Database Internals

문제: SQL은 짤 줄 아는데 "왜 느린지"를 설명하지 못한다.

* 실행 계획 (`EXPLAIN ANALYZE`) 읽기
* 인덱스 (B-Tree, 복합 인덱스 순서, 커버링 인덱스, 인덱스를 타지 못하는 경우)
* Seq Scan vs Index Scan — 언제 Seq Scan이 더 빠른가
* MVCC (Postgres) — Oracle의 UNDO 방식과 비교
* 격리 수준을 MVCC 관점에서 재해석, 각 이상 현상을 직접 재현
* Lock: row lock / table lock / advisory lock, 락 대기 관찰
* Deadlock 재현과 해소
* Connection Pool 크기 계산, 풀 고갈 재현
* Slow Query 탐지, N+1을 실제 서비스에서 재현하고 실행 계획으로 확인
* Replication, Read / Write 분리 (필요해지는 시점 판단 포함)

완료 기준: 느린 쿼리 측정 → 실행 계획으로 원인 특정 → 개선 → 재측정 수치 제시.

---

## Phase 22 — Transaction / Lock / Concurrency

문제: 재고가 1개인데 2개가 팔린다.

* Lost Update를 **실제 HTTP 동시 요청**으로 재현 (재고 1개, 동시 요청 1000개)
* `synchronized` — 왜 단일 인스턴스에서만 통하는가
* 낙관적 락 (`@Version`) + 재시도 전략
* 비관적 락 (`PESSIMISTIC_WRITE`, `SELECT FOR UPDATE`)
* 세 방식의 처리량 / 실패율 / p99 실측 비교, 선택 기준 정리
* 멱등성 (같은 요청이 두 번 와도 한 번만 처리)
* 인스턴스를 2대로 늘려서 `synchronized`가 깨지는 것을 실증
  → 분산락이 필요한 지점을 만든다 (구현은 Redis가 들어오는 Phase 24에서 완결)

완료 기준: 부하 테스트에서 oversell 0건, 각 방식의 처리량 수치 비교표.

---

## Phase 23 — Performance Engineering

문제: "느리다"를 감으로 말하고 있다.

* k6 부하 테스트 시나리오 작성, Load Test Plan 문서화
* 10 / 100 / 1,000 / 10,000 RPS 단계별 측정
* p50 / p95 / p99 / 처리량 / 에러율
* 병목 위치 특정: Tomcat 스레드풀 vs HikariCP 커넥션풀 vs 쿼리 vs 외부 API
* 각 풀 크기를 바꿔가며 실측
* Little's Law로 필요한 풀 크기 계산 후 실측과 대조
* JVM: GC 로그, 힙, 스레드 덤프
* Rate Limiting (보호 장치로서)

완료 기준: 병목 계층을 수치로 지목하고, 튜닝 전후 개선폭을 수치로 제시.

---

## Phase 24 — Redis / Cache / Distributed Lock

문제: Phase 23에서 특정 조회가 병목으로 지목되었고, 인덱스로는 더 못 줄인다.

명령어 학습이 목적이 아니다. 아래를 **직접 발생시키고** 해결한다.

* Cache Aside 설계, TTL 결정 근거
* Cache Stampede 재현과 방어
* Cache Penetration 재현과 방어 (없는 키 반복 조회)
* Cache Avalanche 분석 (TTL 동시 만료)
* 캐시 일관성: 쓰기 시 무효화 vs 갱신, 그 사이 정합성 구멍
* **Redis 장애 주입** — Redis가 죽었을 때 시스템이 어떻게 동작해야 하는가를 설계
* Redis 자료구조 (String / Hash / Sorted Set)를 도메인에 맞게 선택
* Redis는 단일 스레드다 — 그게 왜 중요한가
* **Distributed Lock** — Phase 22에서 만든 다중 인스턴스 Race Condition을 여기서 해결
  (`SETNX`, TTL, 락 소유자 검증, 락 해제 안전성, 자동 연장의 함정)
* Spring Cache 추상화 (`@Cacheable`)와 그 한계

완료 기준: 캐시 도입 전후 p95 비교, 캐시 무효화 버그 재현, 다중 인스턴스에서 oversell 0건.

---

## Phase 25 — Kafka / Async Messaging

문제: 예약 성공 후 결제 / 재고 / 알림까지 동기로 처리해서 응답이 느리고, 하나가 실패하면 전부 실패한다.

단순 Producer / Consumer 구현이 목적이 아니다. 아래를 직접 발생시킨다.

```text
Client → API → DB → Kafka → Payment
                          → Inventory
                          → Notification
```

* 동기 호출의 한계를 먼저 실측
* Topic / Partition / Consumer Group / Offset
* At-least-once와 **메시지 중복** 재현 → 멱등 Consumer
* **메시지 유실** 재현 (acks, 커밋 시점)
* **순서 보장**은 파티션 단위로만 된다 — 순서가 필요한 키를 파티셔닝
* Consumer 장애 / 재시작 / Rebalance
* Retry, Backoff, **DLQ**
* **Consumer Lag** 모니터링
* **Transactional Outbox** — DB 커밋과 메시지 발행의 원자성
* `@TransactionalEventListener`와 비교: 언제 Kafka까지 필요한가

완료 기준: 예약 API 응답 시간 감소 수치, Consumer를 죽였다 살려도 유실 / 중복 처리 0건.

---

## Phase 26 — Distributed Systems

문제: 서비스가 2개 이상이 되면서 "어디서 실패했는지" 알 수 없다.

이론보다 **실제 시스템 설계** 중심으로.

* Stateless, Horizontal Scaling, Load Balancing
* 서비스 분리 기준, 동기(HTTP) vs 비동기(Kafka) 선택 기준
* Eventual Consistency — 어디까지 허용할 것인가
* Saga (보상 트랜잭션) — 분산 트랜잭션이 불가능한 이유부터
* Timeout / Retry / Backoff / Circuit Breaker / Bulkhead / Backpressure
* 재시도가 만드는 중복 → 멱등성 키
* **Partial Failure**와 장애 전파
* 분산 트레이싱: Trace ID / Span, Micrometer Tracing + OpenTelemetry
* Grafana 대시보드, 로그 / 메트릭 / 트레이스를 하나의 요청으로 연결

완료 기준: 요청 하나를 서비스 경계를 넘어 끝까지 추적하고, 중간 서비스를 죽였을 때의 시스템 반응을 설계대로 검증.

---

## Phase 27 — Reliability / Chaos Engineering

문제: 장애를 겪어본 적은 있어도 의도적으로 만들어 본 적은 없다.

* 장애 주입: DB 정지, Redis 정지, Kafka 정지, 네트워크 지연 삽입, 커넥션풀 고갈, 디스크 포화, 인스턴스 강제 종료
* 각 장애의 증상이 어떤 지표에 **먼저** 나타나는가
* Alert 규칙 설계 — 무엇을, 언제, 누구에게
* Graceful Shutdown, Zero-Downtime Deployment
* 롤백 전략, Backup / Recovery
* 장애 대응 절차: 탐지 → 완화 → 원인 분석 → 재발 방지
* **Incident Report 작성**

완료 기준: 장애를 주입하고, 지표만 보고 원인을 특정하고, 복구하고, Incident Report를 남긴다.

---

## Phase 28 — Kubernetes (선택)

우선순위 낮음. Docker Compose로 충분한 단계에서는 진행하지 않는다.
Phase 20~27을 끝낸 뒤 실제로 필요해지면 진행한다.

이유: 오케스트레이션은 인프라 영역이라 Backend 원리를 거의 가르치지 않는다.
같은 시간에 DB / 동시성 / 성능을 파는 것이 우선순위가 높다.

* Pod / Deployment / Service / Ingress
* ConfigMap / Secret
* Liveness / Readiness Probe가 Actuator와 어떻게 연결되는가
* HPA (오토스케일링)
* 롤링 업데이트, 무중단 배포

---

## Phase 29 — Final Production System

Phase 20~27의 결과물을 하나의 시스템으로 정리한다.

**기술 리스트를 미리 정해놓고 시작하지 않는다.** 요구사항과 제약(트래픽 / SLA / 정합성 / 예산)을
먼저 정의하고, 각 기술이 필요한지를 **근거를 들어 판단**한다.
"Kafka를 쓰지 않는다"가 정답인 경우를 판별하는 것도 결과물에 포함된다.

정리할 문서:

* Requirements / Capacity Estimation
* Architecture Decision Record (채택한 것과 **채택하지 않은 것 + 이유**)
* Data Model / API Specification / Sequence Diagram
* 성능 수치 (부하 테스트 결과, 튜닝 전후)
* Failure Scenario와 실제 대응 기록
* Deployment / Monitoring Strategy
* **비용** — 각 인프라 선택의 운영 비용. 실무 결정은 전부 비용 제약을 받는다
* 남은 기술 부채

---



---

# 최종 목표 자가진단 체크리스트

> 각 Phase를 끝낼 때, 아래 질문에 스스로 답할 수 있는지 확인한다.
> (원본 §93)


### IoC

> Spring에서 IoC란 정확히 무엇이며 Java의 일반적인 객체 생성과 무엇이 다른가?

### DI

> Spring Container는 Dependency를 어떻게 찾고 주입하는가?

### Bean

> BeanDefinition과 실제 Bean Instance는 무엇이 다른가?

### ApplicationContext

> ApplicationContext는 BeanFactory와 무엇이 다른가?

### Component Scan

> Spring은 `@Component`가 붙은 클래스를 어떻게 찾아 BeanDefinition으로 등록하는가?

### Lifecycle

> Bean은 생성된 이후 어떤 Lifecycle을 거치는가?

### BeanPostProcessor

> Spring의 여러 기능은 왜 BeanPostProcessor를 사용하는가?

### AOP

> Spring AOP는 실제로 무엇을 Proxy하는가?

### Proxy

> JDK Dynamic Proxy와 CGLIB는 어떻게 다른가?

### Self Invocation

> 왜 `this.method()` 호출에서는 Spring AOP가 동작하지 않을 수 있는가?

### Transaction

> `@Transactional`은 실제로 어떻게 Transaction을 시작하고 종료하는가?

### Propagation

> REQUIRED와 REQUIRES_NEW는 실제 Transaction에서 무엇이 다른가?

### MVC

> HTTP Request는 Tomcat에서 Controller까지 어떻게 전달되는가?

### DispatcherServlet

> DispatcherServlet은 정확히 무엇을 하는가?

### HandlerMapping

> Spring MVC는 URL과 Controller Method를 어떻게 연결하는가?

### Argument Resolver

> `@RequestParam`, `@PathVariable`, `@RequestBody`는 어떻게 Java 객체/값이 되는가?

### HttpMessageConverter

> Java Object와 JSON은 어떻게 서로 변환되는가?

### Filter

> Servlet Filter와 Spring Interceptor의 차이는 무엇인가?

### Spring Boot

> `SpringApplication.run()` 이후 ApplicationContext와 Embedded Tomcat은 어떻게 준비되는가?

### Auto Configuration

> Spring Boot는 Classpath와 조건을 이용해 어떻게 필요한 Bean을 자동 구성하는가?

### Configuration

> `@Configuration`과 `@Bean`은 Container에 무엇을 알려주는가?

### JPA

> EntityManager와 Persistence Context는 어떤 역할을 하는가?

### Dirty Checking

> Entity의 필드 하나를 변경했을 뿐인데 왜 UPDATE SQL이 실행되는가?

### Lazy Loading

> Hibernate Proxy는 왜 필요한가?

### Security

> Spring Security Filter Chain은 HTTP Request를 어떻게 인증하고 인가하는가?

### Testing

> `@SpringBootTest`, `@WebMvcTest`, `@DataJpaTest`는 각각 무엇을 로딩하는가?

### Production

> Spring Boot 애플리케이션이 실제 서버에서 JVM, Tomcat, Thread Pool, DB Connection Pool과 어떻게 상호작용하는가?

