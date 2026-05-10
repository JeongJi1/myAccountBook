# API 명세

Base URL: `http://localhost:8080`

> ⚠️ 예외처리 미구현 상태. 존재하지 않는 ID 조회 시 500 에러가 반환됨. `global` 패키지 예외처리 구현 후 갱신 예정.

---

## Disbursement (지출) API

### POST /disbursements
지출 내역을 등록한다.

**Request**
- Content-Type: `application/json`

| 필드       | 타입            | 필수 | 설명                          |
|-----------|----------------|-----|------------------------------|
| amount    | number         | ✅  | 지출 금액 (소수점 허용)          |
| descr     | string         | ✅  | 사용처                        |
| category  | string         | ✅  | 카테고리                       |
| expenseDt | string (ISO 8601) | ✅ | 지출 일시 (`2024-01-15T14:30:00`) |

```json
{
  "amount": 15000,
  "descr": "스타벅스 아메리카노",
  "category": "카페",
  "expenseDt": "2024-01-15T14:30:00"
}
```

**Response**
- `201 Created`

```json
{
  "id": 1,
  "amount": 15000,
  "descr": "스타벅스 아메리카노",
  "category": "카페",
  "expenseDt": "2024-01-15T14:30:00",
  "createdDt": "2024-01-15T14:31:00",
  "updatedDt": "2024-01-15T14:31:00"
}
```

- `400 Bad Request` — 필수 필드 누락 또는 유효성 검증 실패

---

### GET /disbursements
전체 지출 목록을 조회한다.

**Response**
- `200 OK`

```json
[
  {
    "id": 1,
    "amount": 15000,
    "descr": "스타벅스 아메리카노",
    "category": "카페",
    "expenseDt": "2024-01-15T14:30:00",
    "createdDt": "2024-01-15T14:31:00",
    "updatedDt": "2024-01-15T14:31:00"
  }
]
```

---

### GET /disbursements/{id}
ID로 단건 지출 내역을 조회한다.

**Path Parameter**

| 파라미터 | 타입  | 설명   |
|---------|------|-------|
| id      | Long | 지출 ID |

**Response**
- `200 OK` — 위 단건 객체와 동일한 구조
- `404 Not Found` — 해당 ID 없음 (예외처리 구현 후 적용)

---

### PUT /disbursements/{id}
지출 내역을 수정한다. 모든 필드를 전달해야 한다 (Partial Update 미지원).

**Path Parameter**

| 파라미터 | 타입  | 설명   |
|---------|------|-------|
| id      | Long | 지출 ID |

**Request** — `POST /disbursements`와 동일한 Body 구조

**Response**
- `200 OK` — 수정된 지출 내역 반환 (단건 구조와 동일)
- `404 Not Found` — 해당 ID 없음 (예외처리 구현 후 적용)

---

### DELETE /disbursements/{id}
지출 내역을 삭제한다.

**Path Parameter**

| 파라미터 | 타입  | 설명   |
|---------|------|-------|
| id      | Long | 지출 ID |

**Response**
- `204 No Content`
- `404 Not Found` — 해당 ID 없음 (예외처리 구현 후 적용)

---

## 테스트용 curl 예시

```bash
# 생성
curl -X POST http://localhost:8080/disbursements \
  -H "Content-Type: application/json" \
  -d '{"amount": 15000, "descr": "스타벅스 아메리카노", "category": "카페", "expenseDt": "2024-01-15T14:30:00"}'

# 전체 조회
curl http://localhost:8080/disbursements

# 단건 조회
curl http://localhost:8080/disbursements/1

# 수정
curl -X PUT http://localhost:8080/disbursements/1 \
  -H "Content-Type: application/json" \
  -d '{"amount": 20000, "descr": "스타벅스 라떼", "category": "카페", "expenseDt": "2024-01-15T15:00:00"}'

# 삭제
curl -X DELETE http://localhost:8080/disbursements/1
```