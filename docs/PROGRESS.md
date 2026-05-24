# 작업 일지

## 2026-05-24

### 작업 내용
- `@Autowired` 필드 주입 → `@RequiredArgsConstructor` + `private final` 생성자 주입으로 전환 (Controller, Service)
- `UpdateDisbursementRequestDTO` 신규 생성 — Create와 분리
- `DisbursementService`가 `DisbursementResponse`를 직접 반환하도록 변경 → Controller는 엔티티 미접촉
- `DisbursementService` 내부 조회 로직을 `private findById()`로 분리 — `@Transactional` 전파 혼선 제거
- CLAUDE.md 현재 구현 상태 갱신, 의존성 주입 원칙 추가
- docs/API.md 예외처리 관련 임시 경고문 제거

### 배운 점
- `@Transactional(readOnly = true)` 메서드를 같은 Bean에서 내부 호출하면 Spring AOP 프록시를 거치지 않아 `readOnly` 속성이 무시됨. `private` 헬퍼 메서드로 분리하면 이 혼선을 피할 수 있음
- 생성자 주입은 `final` 필드 강제로 불변성을 확보하고, 순환 의존성을 컴파일 타임에 감지할 수 있어 실무 표준으로 사용됨
- Service가 Response DTO를 반환하면 Controller가 엔티티를 직접 다루지 않아 계층 간 결합도가 낮아짐

---

## 2026-05-24 (2)

### 작업 내용
- `Category` 엔티티 및 테이블 추가 (`ddl-auto: update`로 자동 생성)
- Category CRUD API 구현 (`POST /categories`, `GET /categories`, `DELETE /categories/{id}`)
- `Disbursement.category` 필드를 `String` → `@ManyToOne Category` FK로 변경
- `CreateDisbursementRequestDTO`, `UpdateDisbursementRequestDTO`의 `category` 필드를 `categoryId (Long)`으로 변경
- `DisbursementResponse`에 `categoryId`, `categoryName` 분리 노출
- 월별 지출 통계 API 구현 (`GET /statistics/monthly?year=`)
- 카테고리별 지출 집계 API 구현 (`GET /statistics/category?year=&month=`)
- `GlobalExceptionHandler`에 `IllegalArgumentException` (400), `IllegalStateException` (409) 핸들러 추가
- 카테고리 삭제 시 사용 중인 지출 내역 존재 여부 검증 로직 추가

### 배운 점
- 네이티브 쿼리 `Object[]` 방식은 타입 변환이 명시적이어서 Hibernate 버전에 무관하게 안전하게 동작함
- `EXTRACT(MONTH FROM expense_dt)::int`로 PostgreSQL에서 월을 정수로 추출
- 카테고리 삭제 전 FK 참조 여부 체크: DB 제약 에러를 상위로 올리는 것보다 비즈니스 로직에서 먼저 검증하는 쪽이 에러 메시지 품질이 높음
- `IllegalStateException` → 409 Conflict 매핑이 의미적으로 적절 (리소스가 현재 상태 때문에 요청 처리 불가)

---
