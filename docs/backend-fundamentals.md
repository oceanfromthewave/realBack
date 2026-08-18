# Backend Fundamentals — 프레임워크·언어 무관한 진짜 원리

> **이 파일이 최종 목표의 중심이다.** `spring-concepts.md`가 "Spring이 이 원리를 어떻게 구현했나"라면,
> 이 파일은 **원리 그 자체**다. 스택이 Go / Node / Python / Rust 로 바뀌어도 남는 것.
>
> 자동 로드되지 않는다. Spring 개념을 배울 때 대응하는 원리 섹션만 연결해서 읽는다.
> Spring을 배울 때마다 물어라: **"이건 Spring만의 것인가, 여기 있는 보편 원리의 한 구현인가?"**

---

# 1. 요청의 일생 (Request Lifecycle)

`GET /api/products/1` 한 줄이 화면에 뜨기까지, 프레임워크 위에서만 보면 절반을 놓친다.

```text
클라이언트
  ↓ DNS 조회 (이름 → IP, 캐시 계층들)
  ↓ TCP 3-way handshake (연결 수립, RTT 1회)
  ↓ TLS handshake (암호화 협상, RTT 추가 — 그래서 keep-alive가 중요)
  ↓ HTTP 요청 바이트 전송
서버 소켓 (OS가 accept, 커널 큐에 쌓임)
  ↓ 스레드/이벤트루프가 소켓에서 읽음  ← 동시성 모델이 갈리는 지점 (§2)
  ↓ 애플리케이션 코드 (라우팅 → 핸들러)
  ↓ DB 커넥션 풀에서 커넥션 빌림 → 쿼리 → 반환
  ↓ 응답 직렬화 (객체 → JSON 바이트)
  ↓ 소켓에 write, 커널 버퍼 → TCP → 클라이언트
```

핵심 질문:
* 이 경로에서 **시간은 어디서 사라지는가?** (대부분 네트워크 왕복 + DB 대기, CPU 아님)
* 각 단계에 **풀(pool)**이 있다: 소켓, 스레드, DB 커넥션. 어느 하나가 고갈되면 앞단이 막힌다.
* Spring에서 이 그림: Tomcat이 accept/스레드, DispatcherServlet이 라우팅, HikariCP가 DB 풀. **전부 이 보편 그림의 한 구현일 뿐이다.**

---

# 2. I/O 모델 & 동시성 모델

백엔드 성능/확장성의 뿌리. 프레임워크 선택(Servlet vs WebFlux, Node, Go)은 결국 여기서 갈린다.

## Blocking vs Non-blocking I/O

```text
Blocking     : read() 호출 → 데이터 올 때까지 그 스레드가 멈춰 대기
Non-blocking : read() 즉시 반환 → "아직 없음", 준비되면 알림 (epoll/kqueue)
```

## 동시성 모델 3종

```text
Thread-per-request  (Tomcat, 전통 서블릿)
  요청 1개 = 스레드 1개. 직관적. 블로킹해도 됨.
  한계: 스레드는 비싸다(메모리 ~1MB, 컨텍스트 스위칭). 수만 동시연결 = 수만 스레드 = 터짐.

Event Loop  (Node, Netty, WebFlux)
  스레드 몇 개가 논블로킹으로 수만 연결을 돌린다.
  강점: I/O 바운드 고동시성에 강함.
  함정: 이벤트 루프에서 절대 블로킹하면 안 됨(하나 막히면 전부 막힘).

Coroutine / 경량 스레드  (Go goroutine, Kotlin coroutine, Java Virtual Thread)
  블로킹처럼 쓰는데 런타임이 논블로킹으로 스케줄. 두 세계의 장점.
  Java 21+ Virtual Thread가 Servlet 스택의 thread-per-request 한계를 상당히 무력화한다 → 알아둘 것.
```

핵심 질문:
* **C10K 문제**: "연결 1만 개를 스레드 1만 개로?"에서 시작된 이벤트 루프의 동기.
* 내 워크로드는 **I/O 바운드인가 CPU 바운드인가?** → 모델 선택의 1차 기준.
* CPU 바운드는 이벤트 루프로 못 푼다(코어 수가 한계). I/O 바운드라야 논블로킹이 빛난다.

> Spring 매핑: WebFlux를 "안 배우는" 게 아니라, **이 모델 트레이드오프를 이해한 뒤** 필요할 때 고른다. Virtual Thread가 대안이 되는 지점도 여기서 판단.

---

# 3. HTTP 심화

REST CRUD만 짜면 안 보이는 것들.

```text
HTTP/1.1 : keep-alive(연결 재사용), 하지만 요청 순차 처리(HOL blocking)
HTTP/2   : 멀티플렉싱(한 연결에 여러 스트림), 헤더 압축, 서버 푸시
HTTP/3   : QUIC(UDP 기반) — TCP HOL blocking 자체를 제거
```

메서드 시맨틱(계약이다, 구현자 마음이 아니다):
```text
Safe        : GET, HEAD — 서버 상태를 바꾸지 않는다
Idempotent  : GET, PUT, DELETE — 여러 번 보내도 결과 같다  ← 재시도 설계의 근거
Non-idempotent : POST — 두 번 = 두 번 생성될 수 있다 → 멱등성 키 필요(§9)
```

* 상태코드는 **계약**이다: 4xx(클라 잘못, 재시도 무의미) vs 5xx(서버 잘못, 재시도 의미 있을 수)
* 캐시 시맨틱: `Cache-Control`, `ETag`, 조건부 요청(`If-None-Match`) — CDN/브라우저 캐시의 기반
* **REST vs gRPC vs GraphQL**:
```text
REST    : 리소스 중심, 캐시 친화적, 범용. 오버/언더페칭 문제.
gRPC    : 바이너리(protobuf) + HTTP/2, 서비스 간 내부 통신에 빠름. 브라우저 직접 X.
GraphQL : 클라가 필요한 필드만 질의. 오버페칭 해결. 캐시/복잡도/N+1 새 숙제.
```
선택 기준: 공개 API·캐시 중요 → REST / 내부 MSA 저지연 → gRPC / 다양한 클라 프론트 → GraphQL.

---

# 4. 데이터 시스템 (DDIA 뼈대)

**프레임워크가 절대 안 가르쳐주는, 진짜 백엔드의 심장.** (참고서: Kleppmann, *Designing Data-Intensive Applications*)

## 저장 엔진
```text
B-Tree     (전통 RDB: Postgres, Oracle, MySQL InnoDB)
  읽기에 유리, 제자리 업데이트. 랜덤 쓰기.
LSM-Tree   (Cassandra, RocksDB, 카프카/많은 NoSQL)
  쓰기에 유리(순차 append + compaction). 읽기 증폭 가능.
```
질문: 내 워크로드가 **읽기 헤비냐 쓰기 헤비냐**가 엔진 성격을 정한다.

## 인덱스
* 인덱스는 **읽기를 위해 쓰기를 느리게** 만드는 거래다(공짜 아님).
* 복합 인덱스는 **순서가 전부**(맨 왼쪽 접두사 규칙).
* 커버링 인덱스, 인덱스를 못 타는 경우(함수 적용, 형변환, 앞부분 와일드카드).

## 복제 (Replication)
```text
Single-leader : 쓰기는 리더, 읽기는 팔로워로 분산. 대부분의 기본.
  → 복제 지연(replication lag) = "내가 쓴 걸 바로 못 읽는" 문제(read-your-writes)
Multi-leader / Leaderless(Dynamo) : 쓰기 충돌 해소가 숙제.
```

## 파티셔닝 / 샤딩
* 데이터가 한 노드에 안 들어갈 때 쪼갠다. **샤딩 키 선택이 운명을 가른다.**
* 핫스팟(특정 키에 몰림), 리밸런싱, 크로스 샤드 조인/트랜잭션의 어려움.
* **Consistent Hashing** — 노드 추가/삭제 시 이동을 최소화하는 기법.

## 일관성 & 분산 트랜잭션
```text
격리 수준(단일 DB)  : Read Committed / Repeatable Read / Serializable
분산 일관성          : Strong ↔ Eventual (그 사이 스펙트럼)
CAP : 네트워크 분단(P) 시 일관성(C)과 가용성(A) 중 택1
PACELC : 분단 아닐 때도 지연(L) vs 일관성(C) 트레이드오프는 남는다
```
* **2PC**(2단계 커밋): 강하지만 느리고 코디네이터 장애에 취약.
* **Saga**: 분산 트랜잭션을 포기하고 **보상 트랜잭션**으로 최종 일관성. (MSA의 현실적 답)
* **합의(Consensus)**: Raft/Paxos — 리더 선출, 로그 복제. Kafka/etcd/DB HA의 밑바닥. (원리만 이해)

## 배치 vs 스트림
```text
Batch  : 유한 데이터를 몰아서 (야간 집계, ETL)
Stream : 무한 이벤트를 계속 (Kafka consumer, 실시간 집계)
```

---

# 5. 시스템 설계 방법론

"기술 리스트"부터 고르지 않는다. **문제 → 제약 → 기술** 순서.

```text
1. 요구사항       기능 + 비기능(트래픽, SLA, 정합성, 예산). 비기능이 설계를 정한다.
2. 용량 추정      목표 RPS → QPS/저장량/대역폭/커넥션/메모리 back-of-envelope
3. 데이터 모델    접근 패턴에서 스키마를 역산 (쿼리가 스키마를 정한다)
4. API 설계       엔드포인트, 에러 계약, 멱등성, 버저닝
5. 병목 식별      측정으로 특정 (감으로 X)
6. 확장/보완      병목에만 해법 투입 → 재측정으로 증명
```

암산 감각(예):
* 1일 = 86,400초 ≈ 10^5. "1억 요청/일" ≈ 1,150 RPS 평균, 피크는 ×3~10.
* 디스크 seek ~10ms, SSD ~0.1ms, 메모리 ~100ns, 같은 DC 왕복 ~0.5ms, 대륙간 ~100ms.

**가장 어려운 건 "안 쓰기로 결정"하는 것.** 복잡도는 좋은 시스템의 부산물이지 목표가 아니다.
(이 방법론은 커리큘럼 Phase 20 / Phase 29와 직결)

---

# 6. 확장 & 트래픽 처리 패턴

```text
수직 확장  : 서버를 키운다. 간단하지만 한계·단일장애점.
수평 확장  : 서버를 늘린다. → 전제: Stateless (상태를 서버 밖 DB/Redis로)

로드 밸런싱 : L4(TCP, 빠름) vs L7(HTTP 내용 기반 라우팅)
CDN         : 정적/캐시 가능 응답을 엣지로 밀어 원본 부하↓, 지연↓
큐 기반 부하 평준화 : 스파이크를 큐로 흡수, 컨슈머가 자기 속도로 처리
레이트 리밋 : 토큰 버킷 / 리키 버킷 — 보호 장치이자 공정성 장치
```
핵심: **수평 확장의 전제는 stateless.** 세션을 서버 메모리에 두는 순간 확장이 깨진다 → 왜 세션 스토어(Redis)가 필요한지의 근거.

---

# 7. 신뢰성 & 장애 (Failure is normal)

분산 시스템에서 **부분 실패는 예외가 아니라 기본값**이다.

```text
Timeout        : 무한 대기 금지. 모든 외부 호출에 상한.
Retry + Backoff + Jitter : 재시도하되 지수적으로 물러서고, 몰림(thundering herd) 방지 위해 흔든다.
Circuit Breaker : 죽은 의존 대상을 계속 때리지 않는다(열림→차단→반열림 탐색).
Bulkhead       : 자원 격리 — 한 기능의 폭주가 전체를 삼키지 않게.
Backpressure   : 감당 못 할 유입을 밀어내거나 큐잉. (이벤트루프/스트림에서 필수)
Graceful Degradation : 일부 죽어도 핵심은 산다(추천 죽어도 결제는 됨).
```

전달 보장 & 멱등성:
```text
At-most-once  : 유실 가능, 중복 없음
At-least-once : 유실 없음, 중복 가능  ← 현실의 기본 → 컨슈머를 멱등하게
Exactly-once  : 대개 "at-least-once + 멱등성"으로 흉내낸다
```
* **멱등성 키**: 같은 요청이 두 번 와도 한 번만 처리. 재시도·네트워크 중복의 유일한 방어.
* 재시도가 만드는 중복 → 멱등성으로 받는다. (Phase 22 / 25와 직결)

---

# 8. 관측성 (Observability)

"로그 좀 봤다"가 아니라, **장애 시 원인을 추적 가능한 구조**인가.

```text
세 기둥 : Metrics(수치, 추세/알림) · Logs(사건, 상세) · Traces(요청의 서비스 경계 넘는 경로)
RED(요청)  : Rate, Errors, Duration
USE(자원)  : Utilization, Saturation, Errors
SLI → SLO → Error Budget : 목표를 수치로, 예산을 다 쓰면 기능보다 안정화
```
함정: 메트릭 **cardinality 폭발**(라벨에 userId 넣기 → 카디널리티 터짐), 로그에 민감정보, trace 컨텍스트(Trace ID) 전파 누락.

---

# 9. 보안 기초

Spring Security "설정"이 아니라, 그 밑의 모델.

```text
Authentication(인증) : 너 누구냐  ≠  Authorization(인가) : 너 뭐 할 수 있냐
세션(서버 상태) vs 토큰/JWT(자기완결, 무상태) — 확장성 vs 폐기(revocation) 트레이드오프
OAuth2 / OIDC : 위임 인증. Authorization Code / Client Credentials 흐름 이해.
```
* 최소 권한 원칙, 비밀 관리(코드/이미지에 시크릿 금지, Vault/환경 주입)
* 입력은 전부 불신: SQLi/XSS/SSRF/CSRF는 **경계에서** 막는다
* 전송 암호화(TLS)와 저장 암호화 구분, 위협 모델링(무엇을 누구로부터)

---

# 10. 데이터베이스 원리 (DB 일반)

특정 DB가 아니라 관계형 DB 공통(Phase 21에서 Postgres로 실증).

```text
ACID : Atomicity / Consistency / Isolation / Durability
이상현상 : Dirty Read / Non-repeatable Read / Phantom  ← 격리수준이 무엇을 막는지로 이해
동시성 제어 : MVCC(Postgres/Oracle, 스냅샷) vs Lock 기반
락 : row/table/advisory, 낙관적(@Version) vs 비관적(FOR UPDATE)
실행 계획 : Seq Scan vs Index Scan — 언제 풀스캔이 더 빠른가
N+1, 커넥션 풀 크기(Little's Law), 슬로우 쿼리
```

---

# 11. 원리 → Spring/Java 구현 매핑 (요약)

| 보편 원리 | Spring/Java/Postgres 구현 | 배울 곳 |
|---|---|---|
| 의존성을 런타임이 관리 | IoC Container / DI | concepts §9~16 |
| 자원 풀링 | HikariCP, Tomcat 스레드풀 | concepts §43, Phase 11/23 |
| I/O·동시성 모델 | Servlet thread-per-request / (Virtual Thread) | 이 문서 §2 |
| 횡단 관심사 주입 | AOP Proxy, `@Transactional`, Filter/Interceptor | concepts §19~26, §34 |
| 라우팅→디스패치 | DispatcherServlet | concepts §27~33 |
| 트랜잭션 경계/격리 | `@Transactional`, 격리수준 | concepts §23~26, Phase 21/22 |
| 캐시 & 무효화 | Spring Cache, Redis | concepts §66, Phase 24 |
| 비동기 메시징/전달보장 | Kafka, Outbox | Phase 25 |
| 신뢰성 패턴 | Resilience4j (retry/circuit breaker) | concepts §68, Phase 26 |
| 관측성 | Micrometer, Actuator, Tracing | concepts §63, Phase 26 |
| 인증/인가 | Spring Security, JWT | concepts §49~52, Phase 14 |

> 이 표를 거꾸로 읽는 연습을 한다: **왼쪽(원리)을 먼저 말할 수 있으면, 오른쪽(Spring)은 그 사례로 자연스럽게 따라온다.**
> 면접이든 새 스택이든, 남는 건 왼쪽이다.

---

# 12. 더 깊이 (레퍼런스)

* *Designing Data-Intensive Applications* (Kleppmann) — §4 데이터 시스템의 정본
* System Design 자료 — §5 방법론/추정 감각
* 각 Phase 완료 시 §11 표의 해당 행을 "원리로" 설명할 수 있는지 자가진단(→ curriculum 체크리스트와 병행)
