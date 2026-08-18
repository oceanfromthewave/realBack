# Spring 개념 심화 레퍼런스 (§8~73)

> 이 파일은 **매 세션 자동 로드되지 않는다.** CLAUDE.md는 가볍게 유지하고,
> 지금 학습 중인 주제에 해당하는 섹션만 `view`/`read` 로 필요할 때 읽는다.
> 전체를 한 번에 읽지 말 것. (컨텍스트 절약)
>
> 사용법: "지금 Transaction Propagation 단계다" → 이 파일에서 §25만 찾아 읽는다.

---

# 8. Spring Core 핵심 설명 규칙

Spring의 핵심은 IoC Container다.

다음 개념을 반드시 연결해서 설명한다.

```text
IoC
↓
DI
↓
Bean
↓
BeanDefinition
↓
BeanFactory
↓
ApplicationContext
↓
Bean Lifecycle
↓
BeanPostProcessor
↓
Proxy
```

단순히 "`@Component`를 붙이면 Bean이 된다."고 끝내지 않는다.

다음 질문에 답할 수 있어야 한다.

> Component Scan은 실제로 무엇을 찾는가?

> 찾은 Class를 바로 객체로 만드는가?

> BeanDefinition은 무엇인가?

> Bean은 언제 생성되는가?

> Singleton Bean은 언제 생성되는가?

> Constructor Injection은 어느 시점에 일어나는가?

> BeanPostProcessor는 무엇을 바꾸는가?

> Proxy는 왜 필요한가?

---

# 9. IoC / DI

Spring 학습에서 가장 중요한 영역 중 하나로 취급한다.

다음 순서로 진행한다.

```text
Object Creation
↓
Dependency
↓
Manual Wiring
↓
Dependency Injection
↓
Inversion of Control
↓
Container
↓
ApplicationContext
```

먼저:

```java
UserRepository repository = new UserRepository();
UserService service = new UserService(repository);
```

를 이해한다.

그 다음:

```java
@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }
}
```

를 이해한다.

그리고 마지막으로:

```text
Who calls new UserRepository()?
Who calls new UserService()?
Who resolves repository?
Who stores the objects?
Who controls lifecycle?
```

를 설명한다.

---

# 10. Bean

Bean을 단순히 "Spring이 관리하는 객체"라고만 설명하지 않는다.

다음 내용을 다룬다.

* Bean의 정의
* BeanDefinition
* Bean Name
* Bean Type
* Bean Scope
* Lazy Initialization
* Dependency
* Bean Lifecycle
* Bean Creation
* Bean Destruction
* BeanFactory
* ApplicationContext

특히:

```text
Class
↓
BeanDefinition
↓
Bean Instance
↓
Post Processing
↓
Possibly Proxy
↓
Container Registration
```

의 관계를 이해시킨다.

---

# 11. ApplicationContext

다음 질문을 반드시 설명한다.

* ApplicationContext란 무엇인가?
* BeanFactory와 무엇이 다른가?
* ApplicationContext가 제공하는 기능은 무엇인가?
* Event Publisher와 어떤 관계가 있는가?
* Environment와 어떤 관계가 있는가?
* Resource Loading은 어떻게 하는가?
* MessageSource는 무엇인가?
* ApplicationContext가 Bean Container보다 더 큰 개념인 이유는 무엇인가?

가능하면 작은 Container를 직접 구현해본다.

```text
Map<String, Object>
```

↓

Bean Registry

↓

Dependency Resolver

↓

Simple ApplicationContext

---

# 12. BeanDefinition

Spring 내부 학습에서 매우 중요하게 다룬다.

다음 개념을 연결한다.

```text
@Component
@Configuration
@Bean
@ComponentScan
        ↓
BeanDefinition
        ↓
Bean Creation
```

질문:

> Spring은 객체를 만들기 전에 무엇을 알고 있어야 하는가?

> Class 자체와 BeanDefinition은 어떻게 다른가?

> BeanDefinition에 어떤 정보가 들어가는가?

---

# 13. Component Scan

다음 내용을 깊게 다룬다.

* Component Scan
* Base Package
* ClassPath Scanning
* `@Component`
* `@Service`
* `@Repository`
* `@Controller`
* `@RestController`
* Include Filter
* Exclude Filter
* Configuration Class

그리고:

```text
Package
↓
Class Discovery
↓
Metadata Inspection
↓
Candidate Detection
↓
BeanDefinition Registration
```

의 흐름을 이해시킨다.

---

# 14. `@Configuration` / `@Bean`

다음 구조를 직접 비교한다.

```java
@Bean
public UserService userService() {
    return new UserService(userRepository());
}
```

와:

```java
@Component
public class UserService {
}
```

그리고 `@Configuration`이 왜 필요한지 설명한다.

특히:

* Configuration Class
* `@Bean`
* Full Configuration
* ProxyBeanMethods
* Lite Configuration
* Configuration Proxy

를 필요에 따라 깊게 다룬다.

---

# 15. Bean Lifecycle

Bean Lifecycle을 반드시 실제 호출 순서로 설명한다.

기본 흐름:

```text
Instantiate
↓
Dependency Injection
↓
Aware callbacks
↓
BeanPostProcessor.beforeInitialization
↓
@PostConstruct
↓
InitializingBean
↓
custom init-method
↓
BeanPostProcessor.afterInitialization
↓
Bean Ready
↓
@PreDestroy
↓
DisposableBean
↓
custom destroy-method
```

단순히 순서를 외우게 하지 않는다.

각 단계에서:

> 누가 호출하는가?

> 왜 필요한가?

> 어떤 BeanPostProcessor가 관여하는가?

를 설명한다.

---

# 16. Dependency Resolution

다음 개념을 깊게 다룬다.

* Constructor Injection
* Setter Injection
* Field Injection
* `@Autowired`
* `@Qualifier`
* `@Primary`
* Bean Type
* Bean Name
* Generic Type
* Collection Injection
* Optional Dependency
* Circular Dependency

특히 Constructor Injection을 기본으로 사용한다.

그리고:

```text
Dependency Injection
≠
Dependency Inversion Principle
```

이라는 점도 설명한다.

---

# 17. Scope

다음 Scope를 다룬다.

* singleton
* prototype
* request
* session
* application
* websocket

특히:

```text
Singleton Bean
+
Prototype Dependency
```

에서 발생하는 문제를 설명한다.

필요하면:

* ObjectProvider
* Provider
* Scoped Proxy

까지 연결한다.

---

# 18. PostProcessor

Spring 내부 동작을 깊게 이해하기 위해 반드시 다룬다.

## BeanFactoryPostProcessor

```text
BeanDefinition
↓
BeanFactoryPostProcessor
↓
Bean Creation
```

## BeanPostProcessor

```text
Bean Instance
↓
beforeInitialization
↓
Initialization
↓
afterInitialization
↓
Final Bean
```

그리고 Spring의 많은 기능이 PostProcessor 기반으로 구현된다는 관점으로 설명한다.

예:

```text
@Autowired
AOP
@Transactional
@Configuration
@PostConstruct
```

등이 Container Lifecycle과 어떻게 연결되는지 설명한다.

---

# 19. AOP

Spring AOP는 매우 깊게 다룬다.

다음 순서로 진행한다.

```text
Cross-cutting Concern
↓
Manual Wrapper
↓
Proxy Pattern
↓
JDK Dynamic Proxy
↓
CGLIB
↓
Advice
↓
Pointcut
↓
Advisor
↓
Spring AOP
```

다음 개념을 포함한다.

* Aspect
* Join Point
* Pointcut
* Advice
* Before
* After
* After Returning
* After Throwing
* Around
* Advisor
* Proxy
* Target
* Weaving
* Self Invocation

---

# 20. JDK Dynamic Proxy

직접 구현한다.

```text
Interface
↓
InvocationHandler
↓
Proxy.newProxyInstance()
↓
Method Invocation
```

그리고:

```java
proxy.method();
```

가 실제로 어떤 객체와 메서드를 호출하는지 설명한다.

---

# 21. CGLIB

다음 내용을 비교한다.

```text
JDK Dynamic Proxy
→ Interface 기반

CGLIB
→ Class 상속 기반
```

그리고 Spring이 어떤 상황에서 어떤 Proxy를 사용할 수 있는지 설명한다.

중요하게:

> Spring AOP는 메서드 호출을 "마법처럼 가로채는 것"이 아니라 Proxy를 통한 호출 구조라는 것을 이해시킨다.

---

# 22. Self Invocation

반드시 직접 실험한다.

```java
public void outer() {
    inner();
}

@Transactional
public void inner() {
}
```

왜 `outer()` 내부의 `inner()` 호출에서는 Proxy 기반 AOP가 기대대로 동작하지 않을 수 있는지 설명한다.

핵심:

```text
External Call
↓
Proxy
↓
Target
```

와:

```text
Target
↓
this.inner()
↓
Target 직접 호출
```

을 비교한다.

---

# 23. Transaction

Spring에서 가장 중요한 실무 영역 중 하나다.

다음 순서로 진행한다.

```text
DB Transaction
↓
Transaction Boundary
↓
Connection
↓
Commit / Rollback
↓
Transaction Manager
↓
Proxy
↓
@Transactional
```

다음 내용을 다룬다.

* Transaction
* ACID
* Commit
* Rollback
* Transaction Boundary
* Transaction Manager
* `@Transactional`
* Propagation
* Isolation
* Read Only
* Timeout
* Rollback Rule
* Nested Transaction
* Connection Binding

---

# 24. `@Transactional` 내부 동작

단순히:

```java
@Transactional
public void save() {
}
```

라고 쓰는 방법만 설명하지 않는다.

다음 흐름을 이해시킨다.

```text
Client
↓
Proxy
↓
Transaction Interceptor
↓
Transaction Begin
↓
Target Method
↓
Commit / Rollback
↓
Return
```

그리고:

```text
@Transactional
```

이 실제 객체에 직접 Transaction을 넣는 것이 아니라 Proxy / Interceptor 구조를 통해 동작한다는 점을 강조한다.

---

# 25. Transaction Propagation

다음 내용을 실험한다.

* REQUIRED
* REQUIRES_NEW
* SUPPORTS
* NOT_SUPPORTED
* MANDATORY
* NEVER
* NESTED

특히:

```text
Service A
    ↓
Service B
    ↓
Service C
```

각각 Transaction이 어떻게 연결되는지 설명한다.

---

# 26. Transaction Isolation

다음 DB 개념과 Spring Transaction을 연결한다.

* Dirty Read
* Non-repeatable Read
* Phantom Read
* READ_UNCOMMITTED
* READ_COMMITTED
* REPEATABLE_READ
* SERIALIZABLE

Oracle 경험이 있으므로 DB 이론 자체는 빠르게 진행하되,

> Spring Transaction Isolation 설정이 실제 JDBC Connection / Database와 어떻게 연결되는가?

를 깊게 다룬다.

---

# 27. Spring MVC

Spring MVC를 Annotation 사용법이 아니라 Servlet 기반 Architecture로 이해한다.

기본 구조:

```text
Client
↓
TCP
↓
HTTP
↓
Tomcat
↓
Servlet
↓
Filter
↓
DispatcherServlet
↓
HandlerMapping
↓
HandlerAdapter
↓
Controller
↓
Service
↓
Repository
↓
Response
```

---

# 28. Servlet / Tomcat

Spring MVC 전에 Servlet Container를 이해한다.

다음 내용을 다룬다.

* Servlet
* Servlet Container
* Tomcat
* Servlet Lifecycle
* Request
* Response
* Thread-per-request
* Filter
* DispatcherServlet

질문:

> Tomcat이 없으면 Spring MVC는 어떻게 HTTP 요청을 받는가?

> DispatcherServlet은 Servlet인가?

> Spring이 HTTP Server 자체인가?

를 설명한다.

---

# 29. DispatcherServlet

Spring MVC의 핵심으로 깊게 다룬다.

요청 흐름:

```text
HTTP Request
↓
Tomcat
↓
DispatcherServlet
↓
HandlerMapping
↓
Handler
↓
HandlerAdapter
↓
Controller Method
↓
Return Value Handler
↓
HttpMessageConverter
↓
HTTP Response
```

각 단계가 실제로 어떤 역할을 하는지 설명한다.

---

# 30. HandlerMapping

다음 개념을 다룬다.

* HandlerMapping
* Controller
* `@RequestMapping`
* Path Matching
* HandlerMethod
* Request Mapping Registry

특히:

```java
@GetMapping("/users")
```

이 내부적으로 어떻게 등록되는지 설명한다.

---

# 31. HandlerAdapter

Controller를 직접 호출하지 않는 이유를 설명한다.

```text
DispatcherServlet
↓
Handler
↓
HandlerAdapter
↓
Actual Invocation
```

그리고 HandlerAdapter 추상화가 왜 필요한지 설명한다.

---

# 32. Argument Resolver

다음 예제를 내부 동작까지 연결한다.

```java
@GetMapping("/users/{id}")
public UserResponse get(
        @PathVariable Long id,
        @RequestParam String name,
        @RequestHeader String token
) {
}
```

각 argument가 어떻게 만들어지는지 설명한다.

```text
HTTP Request
↓
HandlerMethodArgumentResolver
↓
Controller Parameter
```

---

# 33. HttpMessageConverter

다음을 깊게 다룬다.

```text
JSON
↓
HttpMessageConverter
↓
Java Object
```

그리고:

```text
Java Object
↓
HttpMessageConverter
↓
JSON
```

을 설명한다.

Jackson의 역할도 연결한다.

Java에서 알고 있는 Jackson 지식과 Spring MVC의 MessageConverter가 어떻게 연결되는지 설명한다.

---

# 34. Filter / Interceptor / AOP

세 가지를 반드시 비교한다.

```text
Filter
→ Servlet Container Level

Interceptor
→ Spring MVC Level

AOP
→ Spring Bean Method Level
```

요청 흐름:

```text
Client
↓
Filter
↓
DispatcherServlet
↓
Interceptor
↓
Controller
↓
Service Proxy
↓
Service
```

각 계층에서 무엇을 처리하는 것이 자연스러운지 설명한다.

---

# 35. Exception Handling

다음 흐름으로 연결한다.

```text
Exception
↓
Controller
↓
HandlerExceptionResolver
↓
@RestControllerAdvice
↓
HTTP Error Response
```

다음 개념을 다룬다.

* `@ExceptionHandler`
* `@ControllerAdvice`
* `@RestControllerAdvice`
* HandlerExceptionResolver
* Error Response
* HTTP Status
* Domain Exception
* Application Exception

그리고 Exception을 어디까지 던져야 하는지 Architecture 관점에서도 설명한다.

---

# 36. Validation

다음 흐름을 이해한다.

```text
HTTP Request
↓
Message Conversion
↓
Validation
↓
Controller
```

다음 내용을 다룬다.

* Bean Validation
* Jakarta Validation
* `@Valid`
* `@Validated`
* Constraint
* Custom Constraint
* Validation Error
* Method Validation

Java Bean Validation 경험이 있다면 빠르게 진행하되 Spring MVC와 연결되는 지점을 깊게 다룬다.

---

# 37. Spring Boot

Spring Boot를 단순히 "Spring을 편하게 쓰는 것"이라고 설명하지 않는다.

다음 문제를 먼저 제시한다.

```text
Spring Framework

Servlet 설정
DataSource 설정
MessageConverter 설정
View 설정
Bean 설정
Tomcat 설정
Logging 설정
```

↓

설정이 너무 많음

↓

Spring Boot

그리고 다음을 이해한다.

* SpringApplication
* Auto Configuration
* Starter
* Embedded Server
* Configuration Properties
* Profiles
* Actuator

---

# 38. Spring Boot Startup

Application 실행 시 실제 흐름을 설명한다.

```text
main()
↓
SpringApplication.run()
↓
ApplicationContext 생성
↓
Environment 준비
↓
Configuration Class 처리
↓
Component Scan
↓
Auto Configuration
↓
BeanDefinition 등록
↓
Bean 생성
↓
Embedded Tomcat 시작
↓
Application Ready
```

가능하면 실제 로그와 Debugger로 확인한다.

---

# 39. Auto Configuration

Spring Boot 핵심 영역으로 깊게 다룬다.

다음 개념을 다룬다.

* Auto Configuration
* Conditional
* `@ConditionalOnClass`
* `@ConditionalOnMissingBean`
* `@ConditionalOnProperty`
* AutoConfiguration Import
* Starter
* Classpath
* Bean Registration

질문:

> `spring-boot-starter-web` 하나 추가했는데 왜 Tomcat이 생기는가?

> 왜 Jackson Bean이 등록되는가?

> 특정 Library가 없으면 왜 Configuration이 동작하지 않는가?

---

# 40. Configuration / Environment

다음을 다룬다.

* `application.yml`
* `application.properties`
* Environment
* PropertySource
* Profile
* `@Value`
* `@ConfigurationProperties`
* Environment Variable
* Secret 관리

특히:

```text
Configuration File
↓
PropertySource
↓
Environment
↓
Configuration Binding
↓
Bean
```

의 흐름을 이해한다.

---

# 41. Spring Events

다음 구조를 다룬다.

```text
Publisher
↓
ApplicationEvent
↓
ApplicationEventPublisher
↓
Listener
```

다음을 설명한다.

* ApplicationEvent
* ApplicationEventPublisher
* `@EventListener`
* Transactional Event
* Synchronous Listener
* Asynchronous Listener

Event가 필요한 경우와 단순 Service 호출이면 충분한 경우를 비교한다.

---

# 42. Scheduling / Async

다음을 깊게 다룬다.

```text
@Scheduled
@Async
TaskExecutor
Thread Pool
```

그리고:

```text
Spring Proxy
↓
Async Interceptor
↓
Executor
↓
Thread
```

를 설명한다.

특히 Self Invocation 문제를 다시 연결한다.

---

# 43. Spring Data / JDBC

DB 경험이 있으므로 SQL 자체는 빠르게 진행한다.

먼저:

```text
Application
↓
DataSource
↓
Connection Pool
↓
JDBC Connection
↓
SQL
↓
Database
```

를 이해한다.

그 다음:

```text
JDBC
↓
JdbcTemplate
↓
Spring Data
↓
JPA
```

순서로 진행한다.

---

# 44. JdbcTemplate

다음을 다룬다.

* DataSource
* Connection
* PreparedStatement
* ResultSet
* JdbcTemplate
* RowMapper
* Exception Translation
* Transaction Integration

Spring이 JDBC의 어떤 boilerplate를 제거하는지 설명한다.

---

# 45. JPA / Hibernate

JPA를 단순 CRUD 기술로 가르치지 않는다.

다음 순서로 진행한다.

```text
JDBC
↓
ORM 문제
↓
JPA
↓
EntityManager
↓
Persistence Context
↓
Hibernate
↓
Spring Data JPA
```

다음 내용을 깊게 다룬다.

* Entity
* EntityManager
* Persistence Context
* First-level Cache
* Dirty Checking
* Flush
* Commit
* Lazy Loading
* Proxy
* Relationship
* Fetch Join
* N+1
* Batch
* Optimistic Lock
* Pessimistic Lock

---

# 46. Persistence Context

특히 깊게 다룬다.

```text
Transaction
↓
EntityManager
↓
Persistence Context
↓
Managed Entity
```

다음 상태를 직접 실험한다.

```text
New
↓
Managed
↓
Detached
↓
Removed
```

그리고:

> `save()`가 곧바로 INSERT SQL을 실행한다는 생각을 버린다.

Flush와 Commit의 차이를 직접 확인한다.

---

# 47. Dirty Checking

다음 흐름을 이해한다.

```java
User user = entityManager.find(User.class, id);
user.changeName("new");
```

↓

왜 `save()`를 호출하지 않아도 UPDATE가 발생할 수 있는가?

```text
Persistence Context
↓
Snapshot
↓
Dirty Checking
↓
UPDATE SQL
↓
Flush
```

Hibernate 내부 동작까지 필요하면 디버깅한다.

---

# 48. Lazy Loading / Proxy

다음을 연결한다.

```text
Entity
↓
Lazy Association
↓
Hibernate Proxy
↓
DB Access
```

그리고:

* LazyInitializationException
* Transaction Boundary
* Open Session in View
* Fetch Join
* Entity Graph

를 실무 사례와 함께 다룬다.

---

# 49. Spring Security

Spring Security는 깊게 학습한다.

단순히:

```java
http
    .authorizeHttpRequests(...)
```

를 작성하는 것으로 끝내지 않는다.

다음 흐름을 이해한다.

```text
HTTP Request
↓
Servlet Filter
↓
Spring Security Filter Chain
↓
Authentication
↓
SecurityContext
↓
Authorization
↓
Controller
```

---

# 50. Authentication / Authorization

두 개념을 반드시 분리한다.

```text
Authentication
→ Who are you?

Authorization
→ What can you do?
```

다음 내용을 다룬다.

* Principal
* Authentication
* AuthenticationManager
* AuthenticationProvider
* UserDetails
* SecurityContext
* SecurityContextHolder
* PasswordEncoder
* Authorization
* Role
* Authority

---

# 51. Security Filter Chain

다음 개념을 깊게 다룬다.

* SecurityFilterChain
* Filter
* Authentication Filter
* Authorization Filter
* ExceptionTranslationFilter
* SecurityContext
* AuthenticationManager

그리고 JWT를 사용한다면:

```text
Request
↓
JWT
↓
Authentication Filter
↓
Token Validation
↓
Authentication
↓
SecurityContext
↓
Authorization
```

의 흐름을 구현한다.

---

# 52. JWT

JWT 자체의 구조도 설명한다.

```text
Header
.
Payload
.
Signature
```

그리고:

```text
Authentication
vs
Session
vs
JWT
```

를 비교한다.

Access Token / Refresh Token / Rotation / Expiration / Revocation 등의 실무 설계도 다룬다.

---

# 53. Testing

Spring Test는 다음 순서로 진행한다.

```text
Unit Test
↓
Spring Context Test
↓
Slice Test
↓
Integration Test
↓
Full Application Test
```

다음 내용을 다룬다.

* JUnit
* Mockito
* Spring TestContext
* `@SpringBootTest`
* `@WebMvcTest`
* `@DataJpaTest`
* MockMvc
* Testcontainers
* Test Fixture
* Integration Test
* Transactional Test

---

# 54. Spring TestContext

다음 질문을 설명한다.

> Spring Test는 ApplicationContext를 매 테스트마다 새로 만드는가?

> Context Cache는 무엇인가?

> `@SpringBootTest`가 왜 느릴 수 있는가?

> Slice Test는 무엇을 로딩하지 않는가?

> MockMvc는 실제 Tomcat을 사용하는가?

테스트의 내부 동작과 실행 속도까지 다룬다.

---

# 55. Architecture

Spring 프로젝트를 단순히 Controller / Service / Repository로만 설명하지 않는다.

초기:

```text
controller/
service/
repository/
```

↓

계층 분리:

```text
api/
application/
domain/
infrastructure/
config/
```

↓

최종:

```text
Client
   ↓
API Adapter
   ↓
Application / UseCase
   ↓
Domain
   ↓
Port
   ↓
Adapter
   ↓
Database / External System
```

다음 Architecture를 비교한다.

* Layered Architecture
* Hexagonal Architecture
* Clean Architecture
* Modular Monolith
* Domain-oriented Package Structure

---

# 56. Spring에서 중요한 Architecture 원칙

Spring이 제공하는 DI를 이용해 결합도를 낮춘다.

하지만:

> DI를 사용한다고 좋은 Architecture가 자동으로 만들어지는 것은 아니다.

다음 문제를 항상 검토한다.

* Domain이 Spring에 강하게 의존하는가?
* Service가 Infrastructure를 직접 생성하는가?
* Repository가 DB 기술에 과도하게 노출되는가?
* Controller가 Business Logic을 가지고 있는가?
* Transaction Boundary가 어디에 있는가?
* 외부 API 호출과 DB Transaction이 뒤섞여 있는가?
* 하나의 Service가 너무 많은 책임을 가지는가?

---

# 57. Spring에서 "직접 구현 vs Framework" 비교

중요한 개념에서는 반드시 다음 비교를 사용한다.

```text
[Plain Java]

개발자가 직접 하는 것
- 객체 생성
- dependency 연결
- lifecycle 관리
- proxy 생성
- transaction 처리
- routing
- validation
- exception 처리
- serialization
```

```text
[Spring]

Framework가 대신 해주는 것
- Bean 생성
- dependency resolution
- lifecycle
- proxy
- transaction interception
- routing
- argument resolution
- validation
- message conversion
- exception handling
```

그리고 반드시:

> **Framework는 마법이 아니라 개발자가 직접 작성할 수 있는 코드를 추상화한 것이다.**

라는 관점으로 설명한다.

---

# 58. 내부 구현 학습

가능한 개념은 직접 작은 버전을 구현한다.

## DI Container

```text
Map<Class<?>, Object>
↓
Bean Registry
↓
Constructor Dependency Resolver
↓
Simple Container
↓
Spring ApplicationContext
```

## AOP

```text
Manual Wrapper
↓
JDK Dynamic Proxy
↓
CGLIB
↓
Spring AOP
```

## Transaction

```text
Manual Transaction Template
↓
Proxy
↓
Transaction Interceptor
↓
@Transactional
```

## MVC

```text
Map<Path, Handler>
↓
Router
↓
Dispatcher
↓
Argument Resolver
↓
Message Converter
↓
Spring MVC
```

## DI Annotation

```text
@Component Scan
↓
Class Discovery
↓
BeanDefinition
↓
Bean Creation
↓
Dependency Injection
```

---

# 59. Spring Boot Project 방식

학습은 하나의 작은 Backend 프로젝트를 점진적으로 발전시키는 방식으로 진행한다.

초기:

```text
Plain Java
```

↓

```text
Simple DI Container
```

↓

```text
Spring Core
```

↓

```text
Spring Boot
```

↓

```text
Spring MVC
```

↓

```text
Validation
```

↓

```text
Database
```

↓

```text
Transaction
```

↓

```text
JPA
```

↓

```text
Security
```

↓

```text
Testing
```

↓

```text
Docker
```

↓

```text
Observability
```

↓

```text
Production Architecture
```

기능보다 **Spring과 Backend 내부 원리 학습을 우선한다.**

---

# 60. 최종 프로젝트

최종적으로 작은 Production 수준의 Spring Backend를 직접 구현한다.

예상 구조:

```text
Client
   ↓
Nginx
   ↓
Load Balancer
   ↓
Embedded Tomcat
   ↓
Servlet Filter
   ↓
Spring Security Filter Chain
   ↓
DispatcherServlet
   ↓
Interceptor
   ↓
Controller
   ↓
Application / UseCase
   ↓
Domain
   ↓
Repository
   ↓
JPA / JDBC
   ↓
HikariCP
   ↓
Database
```

그리고:

```text
Docker
Docker Compose
Environment Variables
Configuration
Logging
Exception Handling
Validation
Authentication
Authorization
Testing
Actuator
Metrics
Tracing
CI/CD
Monitoring
```

까지 연결한다.

---

# 61. HTTP / Web Fundamentals

Spring MVC를 제대로 이해하기 위해 다음을 다룬다.

* TCP
* HTTP
* Request
* Response
* HTTP Method
* Status Code
* Header
* Cookie
* Session
* JSON
* REST
* Content-Type
* Accept
* Connection
* Keep-Alive

그 다음:

```text
TCP
↓
HTTP
↓
Servlet
↓
Tomcat
↓
DispatcherServlet
↓
Spring MVC
```

를 연결한다.

---

# 62. Logging

Spring Logging을 Production 관점에서 다룬다.

* SLF4J
* Logger
* Log Level
* Appender
* Formatter
* MDC
* Correlation ID
* Structured Logging

그리고:

```text
HTTP Request
↓
Correlation ID
↓
Application Log
↓
DB Log
↓
External API Log
```

처럼 하나의 요청을 추적하는 방법을 설명한다.

---

# 63. Observability

Production Backend 학습의 중요한 영역으로 다룬다.

```text
Logs
+
Metrics
+
Traces
```

다음 개념을 다룬다.

* Health Check
* Readiness
* Liveness
* Spring Boot Actuator
* Micrometer
* Metrics
* Prometheus
* Distributed Tracing
* Trace ID
* Span
* OpenTelemetry

---

# 64. Performance

단순히 "Spring은 느리다/빠르다" 같은 설명을 하지 않는다.

다음 병목을 구분한다.

```text
HTTP
↓
Servlet Thread
↓
Controller
↓
Service
↓
DB
↓
External API
```

그리고:

* Thread Pool
* Connection Pool
* DB Query
* N+1
* Serialization
* GC
* CPU
* Memory
* Lock
* Contention
* Cache

를 각각 측정하는 방법을 설명한다.

---

# 65. Concurrency

Java Backend 경험을 활용해 다음을 깊게 다룬다.

* Thread
* Thread Pool
* Executor
* Synchronization
* Lock
* Race Condition
* Visibility
* Atomicity
* Concurrent Collection
* CompletableFuture
* Spring TaskExecutor
* `@Async`

그리고:

```text
HTTP Request
↓
Tomcat Thread Pool
↓
Spring
↓
Application
↓
DB Connection Pool
```

에서 각각의 Pool이 무엇인지 구분한다.

---

# 66. Cache

다음 순서로 진행한다.

```text
Cache Problem
↓
Local Cache
↓
Spring Cache Abstraction
↓
@Cacheable
↓
Redis
↓
Distributed Cache
```

다음을 다룬다.

* Cache Hit
* Cache Miss
* TTL
* Eviction
* Cache Stampede
* Cache Consistency
* Cache Aside
* Redis

단순히 Annotation 사용법만 설명하지 않는다.

---

# 67. External API / Integration

Backend 실무에서 자주 사용하는 외부 시스템 연동을 다룬다.

```text
Spring Application
↓
HTTP Client
↓
External API
```

다음을 비교한다.

* RestClient
* WebClient
* HTTP Client
* Timeout
* Retry
* Backoff
* Circuit Breaker
* Connection Pool
* Error Handling

특히:

> 외부 API 호출을 DB Transaction 내부에서 수행하면 어떤 문제가 발생하는가?

를 설명한다.

---

# 68. Exception / Resilience

Production 환경에서 실패를 정상적인 상황으로 취급한다.

다음을 다룬다.

* Timeout
* Retry
* Circuit Breaker
* Bulkhead
* Rate Limit
* Fallback
* Idempotency

그리고:

```text
External Failure
↓
Application Error
↓
Retry?
↓
Fallback?
↓
Client Response
```

의 설계 원칙을 설명한다.

---

# 69. Security 기본 원칙

Spring Security 학습 외에도 Backend 보안을 다룬다.

* Authentication
* Authorization
* Password Hashing
* Session
* JWT
* CSRF
* CORS
* XSS
* SQL Injection
* SSRF
* Secret Management
* HTTPS
* Security Headers

보안 기능을 Annotation 암기로 끝내지 않는다.

---

# 70. Database Transaction과 Spring의 경계

항상 다음 경계를 의식한다.

```text
HTTP Request
    ↓
Controller
    ↓
Service
    ↓
Transaction Boundary
    ↓
Repository
    ↓
Database
```

그리고:

> Transaction Boundary는 왜 보통 Service Layer에 두는가?

를 설명한다.

다음 상황도 다룬다.

```text
Transaction
    ↓
DB
    ↓
External API
```

에서 DB Transaction과 외부 시스템의 성공/실패를 어떻게 처리하는가?

---

# 71. Production Deployment

Spring Boot 애플리케이션의 실제 실행을 이해한다.

```text
Source
↓
Gradle / Maven
↓
JAR
↓
JVM
↓
SpringApplication
↓
Embedded Tomcat
↓
Application
```

Docker에서는:

```text
Docker Image
↓
Container
↓
JVM
↓
Spring Boot
↓
Tomcat
↓
Application
```

을 설명한다.

---

# 72. Docker

다음을 다룬다.

* Dockerfile
* Image
* Container
* Network
* Volume
* Environment
* Docker Compose
* Health Check

그리고 Spring Boot와 Database를 함께 구성한다.

```text
Nginx
↓
Spring Boot Container
↓
Database Container
```

---

# 73. CI/CD

Production 학습의 마지막 단계에서 다룬다.

```text
Git
↓
Build
↓
Test
↓
Package
↓
Docker Image
↓
Deploy
↓
Health Check
↓
Monitoring
```

GitHub Actions 등의 CI/CD를 사용할 수 있다.

---


# 보강 A. API 경계 / DTO 매핑

§81에서 "Entity를 API Response로 직접 노출"을 안티패턴으로만 언급했다. 여기서는 "그럼 어떻게"를 다룬다.

핵심 질문:

> 왜 Entity를 그대로 반환하면 안 되는가?

* 영속성 관심사(연관관계, Lazy Proxy)가 API 계약에 새어나간다
* 직렬화 시점에 Lazy 로딩이 터지거나 의도치 않은 쿼리가 나간다
* API 스펙이 DB 스키마 변경에 강하게 묶인다
* 노출하면 안 되는 필드가 실수로 나간다

다음을 구분해서 설명한다.

```text
Request DTO   →  입력 검증(@Valid)의 경계
Domain / Entity →  비즈니스 규칙의 중심
Response DTO  →  API 계약(외부에 보여줄 형태)
```

매핑 방식 비교:

```text
수동 매핑(생성자 / 정적 팩토리)   →  명시적, 컴파일 타임 안전, 보일러플레이트
MapStruct                        →  컴파일 타임 코드 생성, 성능/안전
ModelMapper 류 리플렉션 매퍼      →  편하지만 런타임 마법 / 디버깅 어려움
```

원칙: 무분별한 DTO 남발(§81)도 문제고, Entity 직접 노출도 문제다.
**계층을 넘는 경계(Controller ↔ 외부)에서만** DTO를 두고, 내부 계층끼리는 과도한 변환을 만들지 않는다.

---

# 보강 B. Reactive / WebFlux — 범위 밖(스코프 명시)

이 커리큘럼은 **Servlet 스택(Spring MVC + Tomcat, blocking I/O)** 기준이다.

* 기본적으로 WebFlux / Reactor(Mono / Flux) / R2DBC 로 넘어가지 않는다
* 외부 API 호출에서 `WebClient`를 쓰더라도, 그건 HTTP 클라이언트로 쓰는 것이지
  애플리케이션 전체를 reactive 로 바꾸는 것이 아니다
* Reactive 가 필요한 시점(초고동시성 + I/O 바운드 + backpressure 요구)을 만나면,
  그때 Servlet 스택의 한계를 **먼저 실측**한 뒤 별도 트랙으로 다룬다

즉 Reactive 는 "안 배운다"가 아니라 **"지금 트랙의 범위가 아니다"**. 함부로 새지 않는다.
