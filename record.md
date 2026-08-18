# Work Record

## 2026-08-18 15:00 - Phase 11 완료, Phase 12 시작

### Input
Phase 11(Database: JDBC/DataSource/Pool/HikariCP/JdbcTemplate/Exception Translation) 완료 후 "진행" — Phase 12(JPA/Hibernate)로 진행 요청.

### Output
- record.md 최초 생성 (기존 없었음).
- Phase 11 산출물: `practice/phase11/{jdbc,pool,hikari,jdbctemplate}` 하위 패키지. RawUserDao(문제) → MiniConnectionPool(직접 풀) → HikariCP(DataSource) → JdbcTemplate(Template Method) → Exception Translation(DuplicateKeyException) 순서로 검증 완료.
- Transaction 심화는 Phase 20+(Inventory Reservation Production Incident)로 이연 결정.
- Phase 12(JPA/Hibernate: Entity/EntityManager/Persistence Context/Dirty Checking/Lazy Loading/N+1 등) 시작.

## 2026-08-18 - Phase 12 일부 진행 후 Phase 13으로 스킵

### Input
Phase 12: User/Order Entity, EntityManagerMain(Dirty Checking 확인), LazyLoadingMain(Lazy Proxy + N+1 확인, 실행 검증까지 완료) 진행. FetchJoinMain 코드는 제시했으나 사용자가 "phase12 건너뛰고 13부터 하자"로 스킵 지시.

### Output
- Phase 12 산출물: `practice/phase12/entity/{User,Order}.java`, `practice/phase12/jpa/{EntityManagerMain,LazyLoadingMain}.java`, `META-INF/persistence.xml`(순수 Hibernate, RESOURCE_LOCAL). N+1 실측(SELECT 1+2번) 확인 완료.
- 스킵됨: FetchJoinMain 실행 검증, Lock, Batch — 미완 상태로 이연.
- Phase 13(Spring Data JPA: Repository/Query Method/JPQL/Native Query/Specification/Pagination/Sorting/Transaction Integration) 시작.

## 2026-08-18 - Phase 13 완료, Phase 14 시작 (Spring Security 진행중)

### Input
"phase14 진행" 이후 User Entity에 password 필드 추가, SecurityConfig, AuthenticationMain 순차 진행.

### Output
- Phase 13 (Spring Data JPA) 전 항목 실행검증 완료: Repository, Query Method, @Query(JPQL fetch join/Native), Pagination/Sorting, Specification.
- build.gradle: spring-security-core/web/config/crypto 6.2.4 추가, 의존성 충돌 없음 확인.
- practice/phase12/entity/User.java: password 필드 추가(nullable, 기존 2-arg 생성자 호환 위해), 3-arg 생성자 추가.
- practice/phase14/security/CustomUserDetailsService.java 작성 (UserDetailsService 구현, UserRepository 재사용).
- practice/phase14/config/SecurityConfig.java 작성 (PasswordEncoder/UserDetailsService/DaoAuthenticationProvider/AuthenticationManager Bean).
- practice/phase14/security/AuthenticationMain.java 작성 및 실행 검증: 정상 인증 성공(ROLE_USER), 오답 비밀번호 BadCredentialsException 정상 발생 확인.
- 다음: SecurityFilterChain (HTTP 요청 레벨 인증/인가, Tomcat embed 재사용).

## 2026-08-18 - Phase 14 JWT까지 완료

### Input
CustomUserDetailsService, SecurityConfig(PasswordEncoder/AuthenticationManager/SecurityFilterChain), AuthenticationMain, SecurityFilterChainMain(Basic Auth 실HTTP검증), JwtTokenProvider, JwtAuthenticationFilter, JwtLoginMain 순차 진행. 401 대신 403 나오는 문제 발견 후 AuthenticationEntryPoint 추가로 수정.

### Output
- User Entity에 password 필드 추가(nullable, 기존 2-arg 생성자 호환).
- build.gradle: spring-security-* 6.2.4, jjwt 0.12.6 추가.
- practice/phase14/security/CustomUserDetailsService.java: UserDetailsService 구현.
- practice/phase14/config/SecurityConfig.java: PasswordEncoder/UserDetailsService/DaoAuthenticationProvider/AuthenticationManager/JwtTokenProvider/AuthenticationEntryPoint/SecurityFilterChain Bean 전부 등록, httpBasic 제거하고 JwtAuthenticationFilter로 교체.
- practice/phase14/security/AuthenticationMain.java: AuthenticationManager 직접 호출 성공/실패 검증(ROLE_USER 확인, BadCredentialsException 확인).
- practice/phase14/security/SecurityFilterChainMain.java: 실제 Tomcat+HTTP로 Basic Auth 4케이스 검증(200/401/401/200).
- practice/phase14/security/JwtTokenProvider.java, JwtAuthenticationFilter.java: JWT 발급/검증/필터 작성.
- practice/phase14/security/JwtLoginMain.java: 로그인->토큰발급->보호리소스 접근 실HTTP 검증. 최초 실행시 401 예상한 자리에 403 나옴 - httpBasic 제거로 AuthenticationEntryPoint 유실, Http403ForbiddenEntryPoint 기본값 적용된 것 확인. AuthenticationEntryPoint Bean 추가해서 401로 수정, 재검증 완료(200/401/401).
- 남은 Phase14 항목: Session, CSRF, CORS.

## 2026-08-18 - Phase 14 완료 (Session/CSRF/CORS)

### Input
Session(STATELESS 정책), CSRF(개념 설명, 코드변경 없음 - Bearer 토큰이라 구조적으로 면역), CORS(CorsConfigurationSource Bean) 진행. CORS는 curl preflight로 실제 검증 요청.

### Output
- SecurityConfig.java: sessionManagement(STATELESS) 추가 - JwtLoginMain 재실행해서 Set-Cookie 없음(JSESSIONID 미발급) 확인.
- SecurityConfig.java: corsConfigurationSource Bean 추가(localhost:3000 허용, GET/POST, Authorization/Content-Type 헤더), securityFilterChain에 .cors() 적용.
- 임시 TempCorsBootstrap.java로 Tomcat 띄우고 curl -X OPTIONS preflight 테스트: 허용 origin(localhost:3000) -> 200 + Access-Control-Allow-* 헤더 정상, 비허용 origin(evil.com) -> 403 Invalid CORS request. 확인 후 임시 파일/프로세스 정리.
- Phase14(Spring Security) 커리큘럼 전 항목 완료: Authentication, Authorization, SecurityContext, SecurityContextHolder, AuthenticationManager, AuthenticationProvider, UserDetails, PasswordEncoder, SecurityFilterChain, JWT, Session, CSRF, CORS.

## 2026-08-18 11:27 - Phase 17 진행 (Cache, HTTP Client/Timeout/Retry), Circuit Breaker/Idempotency 전 /compact

### Input
"phase 17 진행" 이후 Cache -> Redis -> HTTP Client -> Timeout -> Retry 순차 진행. "완료" 확인마다 파일 검토 및 실행검증. 마지막 "compact 정리" 요청.

### Output
- Phase15(Testing), Phase16(Async/Concurrency)는 이번 세션 이전에 이미 완료되어 있었음(record 기록 누락 확인, 코드는 src/main/java/practice/phase16 전체 존재).
- Phase17 산출물:
  - `practice/phase17/problem/SlowExternalCallDemo.java` - 캐시 없을 때 반복 호출 비용 실측(9196ms/100회).
  - `practice/phase17/cache/CacheConfig.java` - `@EnableCaching` + `ConcurrentMapCacheManager` -> 이후 Redis(`LettuceConnectionFactory` + `RedisCacheManager`, TTL 30s)로 교체 완료.
  - `practice/phase17/cache/ProductPriceService.java` - `@Cacheable(cacheNames="productPrice", key="#productId")`. 최초 `cacheManager=` 속성 오사용 버그 발견 후 수정.
  - `practice/phase17/cache/CacheDemo.java` - 검증용, 캐시 hit/miss 로그로 확인 완료.
  - `practice/phase17/http/PricingMockServer.java` - 실습용 Mock 외부 API(embedded Tomcat, :8090). `/price/{id}?delayMs=N`, `/down/{id}`(항상 500).
  - `practice/phase17/http/PricingApiClient.java` - `RestClient` + `JdkClientHttpRequestFactory`(readTimeout 1s) + 3회 재시도(linear backoff 100/200/300ms).
  - `practice/phase17/http/PricingApiClientDemo.java`, `TimeoutDemo.java`, `RetryDemo.java` - 검증용, timeout(1s 초과시 `ResourceAccessException`)과 재시도 backoff 실측 확인.
- build.gradle: `-parameters` 컴파일 옵션 추가(SpEL `#productId` 파라미터명 인식 위해 필수), `spring-data-redis:3.2.5`, `lettuce-core:6.3.2.RELEASE`, `spring-web:6.1.14` 추가.
- docker-compose.yml 신규 생성(redis:7-alpine, 6379 포트), 컨테이너 기동 중.
- Mock 서버(PricingMockServer)는 :8090에서 계속 실행 중 (백그라운드 gradle 프로세스).
- 남은 Phase17 항목: Circuit Breaker, Idempotency.

## 2026-08-18 - Phase 17 마무리 (Circuit Breaker, Idempotency)

### Input
Circuit Breaker 신규 작성 완료 -> PricingApiClient 통합(수정해줘 요청으로 직접 적용) -> Idempotency 진행(추천대로 서버쪽) -> 완료

### Output
- practice/phase17/resilience/CircuitBreaker.java (신규, 사용자 작성): CLOSED/OPEN/HALF_OPEN 상태머신, synchronized로 동시성 보호
- practice/phase17/http/PricingApiClient.java 수정(직접 적용): CircuitBreaker 필드 추가, fetchPrice 진입 시 allowRequest() 체크, catch를 ResourceAccessException -> RestClientException으로 확장(HttpServerErrorException 등 5xx도 잡히도록 - 없으면 breaker가 500 에러에 반응 못하는 버그였음)
- practice/phase17/http/CircuitBreakerDemo.java (검증용): /down/* 연속 호출로 OPEN 전이 + fast-fail 확인. 3회 실패 후 OPEN, 이후 elapsed=0ms로 즉시 컷 확인.
- practice/phase17/cache/CacheConfig.java 수정(사용자 작성): StringRedisTemplate bean 추가(기존 LettuceConnectionFactory 재사용). 이전 세션에서 지적한 미사용 ConcurrentMapCacheManager import는 이미 제거되어 있었음.
- practice/phase17/idempotency/PaymentService.java (신규, 사용자 작성): Redis SETNX(setIfAbsent)로 원자적 idempotency key claim, PROCESSING sentinel로 진행중/완료 구분, 완료시 결과로 덮어쓰기(TTL 24h)
- practice/phase17/idempotency/PaymentServiceDemo.java (검증용): 같은 key 재호출시 doCharge 재실행 안 됨, 다른 key는 정상 과금 확인.

Phase 17 커리큘럼 전체(Cache, HTTP Client/RestClient, Timeout, Retry, Circuit Breaker, Idempotency) 완료.

## 2026-08-18 - Phase 18 완료 (Architecture: Hexagonal/Clean, Domain/Port/UseCase/Adapter)

### Input
"phase 18진행" 이후 Domain -> Port -> UseCase -> JPA Entity -> JPA Repository -> Repository Adapter -> Config(Bean wiring) -> Controller -> HTTP Bootstrap 순차 진행, 매 단계 컴파일/실행 검증. 마지막에 Layered/Modular Monolith/Package by Feature 개념 정리로 마무리, "src/main 파일은 내가 다 작성, 너는 코드까지 다 보여주고 test/설정/infra만 네가 작성" 규칙 확인.

### Output
- practice/phase18/domain/Reservation.java (사용자 작성): 순수 Java 도메인 모델, PENDING/CONFIRMED/CANCELLED/EXPIRED 상태전이, setter 없이 confirm/cancel/expire 메서드로만 전이. 나중에 reconstitute() static factory + private 5-인자 생성자 추가(Adapter가 DB에서 복원할 때 필요) - 최초 누락으로 컴파일 에러 발생, 수정.
- practice/phase18/port/ReservationRepository.java (사용자 작성): Domain이 정의한 Port 인터페이스(save/findById), JPA 흔적 없음.
- practice/phase18/usecase/ReserveStockUseCase.java (사용자 작성): Port만 의존하는 Application 레이어, Spring 무주석.
- practice/phase18/adapter/persistence/ReservationJpaEntity.java (사용자 작성): 영속성 전용 모델, Domain과 분리. @Enumerated를 String 필드에 잘못 붙여서 Hibernate 시작 시 AnnotationException 발생 - 제거해서 수정.
- practice/phase18/adapter/persistence/ReservationJpaRepository.java (사용자 작성): Spring Data JPA 인터페이스.
- practice/phase18/adapter/persistence/ReservationRepositoryAdapter.java (사용자 작성): Port 구현체, Domain<->JPA Entity 변환.
- practice/phase18/config/AppConfig.java (신규, 요청으로 직접 작성): DataSource/EntityManagerFactory/TransactionManager(Phase13 JpaConfig 패턴 재사용) + ReserveStockUseCase @Bean 등록. @EnableJpaRepositories만으로는 @Repository 클래스가 스캔 안 되는 문제 발견, @ComponentScan 추가해서 해결.
- practice/phase18/WiringDemo.java (검증용, 직접 작성): Spring Context로 UseCase->Adapter->DB 저장/조회 확인.
- practice/phase18/adapter/web/ReservationController.java (사용자 작성): Driving Adapter, UseCase만 의존, ReserveRequest/ReserveResponse record DTO로 Domain 노출 안 함.
- practice/phase18/config/WebConfig.java, HttpBootstrap.java (검증용, 직접 작성): 실제 embedded Tomcat + Spring MVC DispatcherServlet 띄워서 curl 없이 Java HttpClient로 POST /reservations 실HTTP 검증, 200 확인.
- build.gradle: spring-webmvc:6.1.14, jackson-datatype-jsr310:2.17.2 추가(각각 DispatcherServlet 미존재, Instant 직렬화 실패 문제로 필요해짐).
- 개념 정리(코드 없이 텍스트로): Layered vs Hexagonal vs Clean Architecture 차이, Modular Monolith, Package by Feature(현재 practice.phase18 패키지 구조 자체가 예시) - Dependency Rule 실무 적용 범위(단순 CRUD엔 과함, 언제 justify되는지)까지 정리.

Phase18(Architecture) 커리큘럼 전 항목 완료: Layered/Hexagonal/Clean Architecture, Dependency Rule, Domain, UseCase, Repository, Port, Adapter, Modular Monolith, Package by Feature.

## 2026-08-18 - Phase 19 진행 (Embedded Tomcat/Environment, Logging, Health Check, Docker)

### Input
"phase19 진행" 이후 순서 제안(Embedded Tomcat+Env → Logging → Health Check → Docker → Nginx → CI/CD → Tracing/Monitoring/장애대응) 후 순차 진행. 코드 제시 포맷을 "경로 먼저, 코드 바로 뒤"로 바꿔달라는 피드백 반영(메모리 저장).

### Output
- `practice/phase19/AppServer.java` (사용자 작성): Phase18 데모용 HttpBootstrap(요청 1번 쏘고 종료)을 실서비스처럼 계속 떠있는 서버로 승격. `APP_PORT` env로 포트 외부화, `tomcat.getServer().await()`로 main 스레드 블록, shutdown hook으로 graceful stop. Windows 콘솔 프로세스는 taskkill이 강제종료만 되고 graceful close 자체가 안 됨 확인(플랫폼 한계) — 실제 SIGTERM 검증은 Docker 단계에서 완료.
- Logging: `build.gradle`에 logback-classic 추가(내가 작성), `src/main/resources/logback.xml` 신규(콘솔+일별 롤링 파일 appender, 14일 보관, 내가 작성). `AppServer.java`의 `System.out.println` → SLF4J `Logger` 교체(사용자 요청으로 내가 직접 수정 - "작성해줘" 명시 요청). 검증: 콘솔 포맷 정상, `logs/app.log` 생성, Spring/Hibernate/HikariCP 내부 로그도 같은 포맷으로 자동 통합됨 확인.
- Health Check: `practice/phase19/health/HealthController.java` (사용자 작성) - `GET /health`, DataSource로 실제 DB 커넥션 체크(`isValid(2)`), 200 UP / 503 DOWN. Boot Actuator 없이 수동 구현으로 원리 학습(Liveness vs Readiness 개념 정리). `WebConfig` componentScan에 `practice.phase19.health` 추가(내가 작성). 검증: `/health` 200 UP 확인. DB DOWN 케이스는 재현 번거로워 스킵(로직상 명확).
- Actuator/Metrics는 스킵 결정 - Health Check로 원리 이미 확인했고 Boot 없는 프로젝트에 풀 Actuator 붙이는 건 낭비 판단. Metrics/Tracing은 나중 모니터링 스택 붙을 때로 이연.
- Docker: `Dockerfile`(신규, 내가 작성) - multi-stage(JDK로 `./gradlew installDist -PmainClass=practice.phase19.AppServer` 빌드 → JRE 런타임에 결과물만 복사), `.dockerignore` 신규, `docker-compose.yml`에 `app` 서비스 추가(포트 8080, `APP_PORT` env, redis에 depends_on).
- Docker Desktop 최초 기동이 오래 걸려서(엔진 파이프 안 뜸) 대기하는 동안 CI/CD(`​.github/workflows/ci.yml` 신규, 내가 작성 - checkout → setup-java 17 → `./gradlew test`)를 먼저 작성. 단, 프로젝트가 git repo 아니라서(`git init` 안 된 상태) 실제 트리거 검증은 못 함 - 사용자에게 지금 git init할지 나중에 할지 물어본 상태(응답 대기 중).
- Docker Desktop 기동 완료 후 `docker compose build app` 성공, `docker compose up -d`로 app+redis 기동, 호스트에서 `curl localhost:8080/health` 200 UP 확인. `docker compose stop app`으로 실제 SIGTERM 보내서 shutdown hook 로그(Tomcat → JPA EntityManagerFactory → HikariCP 순서로 정리) 실증 확인. `docker compose down`으로 정리.
- 남은 Phase19 항목: Nginx, CI/CD(git init 여부 확정 후 마무리), Tracing, Monitoring, 장애 대응.

## 2026-08-18 - Phase 19 계속 (Git 초기화 + CI/CD 실증)

### Input
GitHub repo(oceanfromthewave/realBack) 생성 완료, URL 제공.

### Output
- `.gitignore` 신규(build/.gradle/.idea/tomcat.*/logs 제외).
- `git init`, 최초 커밋(Phase 0~19 진행분 전체, 181 files), `git remote add origin` + `git push -u origin main`.
- CI 실제 트리거 확인: push -> Actions 실행 -> `test` job 성공(42s). `setup-java@v4` deprecated 경고 있어서 `@v5`로 바로 수정, 재푸시 후 재확인 성공.
- CI/CD 항목 완료. 남은 Phase19: Nginx, Tracing, Monitoring, 장애 대응.
