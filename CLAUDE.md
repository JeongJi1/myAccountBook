# myAccountBook

Java/Spring Boot 백엔드 + Expo(React Native) 모바일 앱으로 구성된 가계부 포트폴리오 프로젝트.
단순 CRUD를 넘어 AI 기반 소비 분석, 알림 파싱 자동 등록, 매일 밤 AI 소비 조언 푸시까지 구현 목표.

## 기술 스택

### 백엔드
- Java 17
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL (Docker)
- Gradle
- IntelliJ IDEA

### 모바일 (다음 단계부터 시작)
- Expo (React Native) — iOS / Android 크로스플랫폼
- TypeScript
- 로컬 개발: 같은 Wi-Fi 환경에서 PC 로컬 IP로 백엔드 접속

### 외부 서비스 (단계적 도입)
- Claude API — 알림 텍스트 파싱, AI 소비 조언 생성
- Firebase Cloud Messaging (FCM) — 푸시 알림 (iOS APNs 포함)

## 프로젝트 구조

```
src/main/java/.../
├── controller   # REST API 엔드포인트 (얇게 유지)
├── service      # 비즈니스 로직 (핵심 집중)
├── repository   # DB 접근 (Spring Data JPA)
├── domain       # JPA 엔티티
├── dto          # Request / Response 분리
└── global       # 예외처리, 공통 응답 구조
```

## 개발 원칙 (반드시 준수)

### 엔티티
- `@Setter` 사용 금지 → 의도된 변경 메서드만 노출
- 기본 생성자는 `@NoArgsConstructor(access = AccessLevel.PROTECTED)` 사용
- 엔티티를 컨트롤러에서 직접 반환 금지 → 반드시 Response DTO로 변환

### 계층 분리
- Controller → Service → Repository 구조 엄격히 유지
- Controller: 요청 수신, Service 호출, Response 반환만 담당 (비즈니스 로직 금지)
- Service: 비즈니스 로직 + 엔티티 ↔ DTO 변환 담당, Response DTO 반환
- Repository: 쿼리 메서드 또는 `@Query` 활용

### DTO
- Request DTO와 Response DTO 분리
- DTO는 Java `record`로 작성 (Java 17 활용)
- 엔티티 ↔ DTO 변환은 정적 팩토리 메서드(`from`, `of`) 또는 매퍼에서 처리

### 트랜잭션
- 모든 Service 메서드에 `@Transactional` 적용
- 조회 전용 메서드는 `@Transactional(readOnly = true)` 명시
- 트랜잭션 범위는 Service 계층 내에서만

### 의존성 주입
- `@Autowired` 필드 주입 금지
- `@RequiredArgsConstructor` + `private final` 생성자 주입 사용

### 코드 품질
- 실무 스타일, 포트폴리오 수준 품질로 작성
- 메서드/변수명은 의도가 드러나게 (축약어 지양)
- 매직 넘버/문자열 금지 → 상수 또는 enum 활용

## 사용자(나) 정보

- 개발 경력은 있으나 Spring/JPA 감각을 복구하는 중
- 단순 코드 생성보다 **"왜 그렇게 하는지"** 가 중요
- 실무 관점의 설명을 선호
- 포트폴리오 품질 기준의 코드 지향

## 응답 규칙 (기능 구현 요청 시 반드시 이 순서로)

1. **왜 필요한지** — 이 기능/구조가 필요한 배경
2. **구조 설명** — 어떤 클래스/메서드를 만들고 어떻게 연결되는지
3. **코드 제공** — 위 원칙을 모두 준수한 코드
4. **테스트 방법** — curl, Postman, 또는 단위 테스트 예시
5. **실무적인 이유** — 이렇게 설계한 실무 관점 근거
   (대안과의 trade-off, 흔한 안티패턴, 확장성 고려 등)

## 현재 구현 상태

### 백엔드
- ✅ PostgreSQL 연동 (Docker)
- ✅ Disbursement CRUD API
- ✅ Category CRUD API
- ✅ 통계 / 집계 API (`/statistics/monthly`, `/statistics/category`)
- ✅ 전역 예외처리 (`GlobalExceptionHandler`)
- ✅ 생성자 주입, DTO 분리, Service 계층 DTO 변환

### 모바일 앱 — 단계별 로드맵
- ⏳ **1단계**: Expo 앱 기본 (지출 CRUD + 통계 + 카테고리) + 백엔드 연결
- ⏳ **2단계**: 백엔드 사용자 인증 (JWT) + 앱 로그인/회원가입
- ⏳ **3단계**: 알림 Share Sheet → Claude AI 파싱 → 지출 자동 등록
  - iOS: Share Sheet로 알림 텍스트 전달 (자동 읽기 불가 — iOS 정책)
  - Android: SMS BroadcastReceiver로 자동 읽기 가능
- ⏳ **4단계**: FCM 푸시 알림 + 매일 밤 10시 @Scheduled AI 소비 조언

## 도메인 정보

### `category` 테이블
| 컬럼명      | 설명       |
|-----------|----------|
| id        | PK       |
| name      | 카테고리명 (unique) |
| created_dt | 생성 일시  |

### `disbursement` 테이블
| 컬럼명       | 설명                  |
|------------|---------------------|
| id         | PK                  |
| amount     | 지출 금액              |
| descr      | 설명                  |
| category_id | FK → category.id   |
| expense_dt | 지출 일시              |
| created_dt | 생성 일시              |
| updated_dt | 수정 일시              |

## 작업 시 지켜야 할 것

- 기존 코드 스타일과 패턴을 먼저 확인하고 일관되게 작성
- 새 의존성 추가 시 사전에 알리고 이유 설명
- DB 스키마 변경 시 마이그레이션 영향 명시
- 테스트 코드는 가능한 함께 작성
- 작업 후 변경 사항을 짧게 요약
## 문서 자동 관리 규칙

### docs/PROGRESS.md (작업 일지)
다음 작업을 완료할 때마다 자동으로 추가 작성:
- 새로운 API/기능 구현
- 버그 수정
- 리팩토링
- 의존성 추가/변경

형식:
## YYYY-MM-DD
### 작업 내용
- 무엇을 했는지

### 트러블슈팅
- 만난 문제와 해결 방법 (있을 경우)

### 배운 점
- 실무 관점에서 얻은 인사이트

---

### docs/ADR/ (의사결정 기록)
다음 상황에서 자동으로 ADR 작성 제안:
- 기술 스택 선택 (라이브러리, DB, 프레임워크 등)
- 아키텍처 결정 (계층 구조, 패턴 적용 등)
- 트레이드오프가 명확한 설계 결정

파일명: docs/ADR/NNNN-title-kebab-case.md (NNNN은 4자리 순번)
형식:
# NNNN. 제목

## 상태
Accepted | Proposed | Deprecated

## 배경
어떤 문제/상황이 있었는가

## 결정
무엇을 선택했는가

## 근거
왜 그것을 선택했는가

## 대안
고려했던 다른 선택지와 그것을 채택하지 않은 이유

## 결과
이 결정으로 인한 긍정적/부정적 영향

---

### docs/API.md (API 명세)
새 API 엔드포인트를 만들거나 변경할 때마다 자동 갱신.

형식:
## [도메인명] API

### [메서드] [경로]
설명: ...
Request:
- Body: { ... }
  Response:
- 200: { ... }
- 400: ...