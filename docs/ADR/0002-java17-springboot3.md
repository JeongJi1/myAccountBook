# 0002. Java 17 + Spring Boot 3.x 선택

## 상태
Accepted

## 배경
백엔드 기술 스택을 결정할 시점에, JVM 생태계 내에서 언어/프레임워크 버전을 선택해야 했다.
포트폴리오 프로젝트이므로 "현재 실무에서 가장 많이 요구되는 스택"과 "최신 기능을 적극 활용할 수 있는 스택" 두 기준을 동시에 충족해야 했다.

## 결정
Java 17 LTS + Spring Boot 3.x 선택.

## 근거

### Java 17
- **LTS(Long-Term Support) 버전**: Java 11 이후의 LTS로, 기업 환경에서 채택률이 급격히 상승 중
- **record 타입**: DTO를 불변 객체로 간결하게 표현 가능 (`class` + `@Getter` + 생성자 코드 제거)
- **sealed class / pattern matching**: 도메인 타입 계층을 더 안전하게 모델링 가능 (향후 활용 여지)
- **텍스트 블록**: SQL, JSON 등 멀티라인 문자열 가독성 향상

### Spring Boot 3.x
- **Spring Framework 6 기반**: `javax.*` → `jakarta.*` 패키지로 전환되어 Jakarta EE 10 표준 준수
- **GraalVM Native Image 공식 지원**: 향후 컨테이너 배포 시 메모리 사용량과 시작 시간을 대폭 줄일 수 있는 옵션 확보
- **Observation API 통합**: Micrometer 기반 모니터링/추적을 코드 변경 없이 적용 가능
- **Spring Boot 2.x EOL 예정**: 신규 프로젝트에서 2.x를 선택하면 단기간 내 마이그레이션 부채 발생

## 대안

| 대안 | 탈락 이유 |
|------|----------|
| Java 11 + Spring Boot 2.x | LTS이지만 2026년 이후 EOL. record 등 최신 언어 기능 미지원. 포트폴리오 관점에서 구식 스택으로 인식될 수 있음 |
| Java 21 LTS | 2023년 출시로 실무 채택 초기 단계. Virtual Threads(Project Loom)는 매력적이지만 Spring Boot 3.2+ 에서 정식 지원되어 3.x에서도 업그레이드로 이용 가능 |
| Kotlin + Spring Boot | 언어 표현력은 뛰어나지만 이 프로젝트의 목표인 "Spring/JPA 실무 감각 복구"에 불필요한 학습 비용 추가 |

## 결과
- **긍정적**: DTO를 `record`로 작성해 불변성 보장 및 코드량 감소, 최신 실무 스택 경험 축적
- **부정적**: `javax.*` → `jakarta.*` 전환으로 레거시 코드/라이브러리 호환성 확인 필요 (이 프로젝트는 신규이므로 영향 없음)