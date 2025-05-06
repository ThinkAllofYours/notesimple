# notesimple

간단한 노트 관리 Spring Boot 애플리케이션

## 프로젝트 소개

- Kotlin & Spring Boot 기반의 노트 관리 API 서버입니다.
- PostgreSQL, Elasticsearch 등 다양한 환경에서 동작하도록 설계되었습니다.

---

## 프로젝트 구조

```bash
src/
 ├── main/
 │    └── kotlin/...
 │    └── resources/
 │         ├── application.properties
 │         ├── application-local.properties
 │         └── application-docker.properties
 └── test/
      └── kotlin/...
      └── resources/
           └── application.properties (테스트용 H2 DB 설정)
```

---

## 실행 방법

### 1. 로컬 개발 환경

1. PostgreSQL 실행
2. `application-local.properties`에 맞게 DB 정보 설정
3. 아래 명령어로 실행

   ```sh
   ./gradlew bootRun
   ```

### 2. Docker 환경

- `docker-compose up --build` 명령어로 모든 서비스(앱, DB, ES) 실행

---

## 테스트 환경

- 테스트는 H2 인메모리 DB를 사용합니다.
- `src/test/resources/application.properties`에 H2 설정이 포함되어 있습니다.
- 테스트 실행:

  ```sh
  ./gradlew test
  ```

---

## 테스트 환경에서 겪었던 문제와 해결 방법

### 1. DB 설정 문제

- 테스트 시 실제 DB(PostgreSQL)에 연결을 시도해 실패
- **해결:** 테스트용 H2 DB 설정 추가, H2 의존성 추가

### 2. 포트 충돌 문제

- `@SpringBootTest`와 `main()`을 동시에 호출해 서버가 두 번 실행, 포트 충돌 발생
- **해결:** `@SpringBootTest`에서는 `main()`을 직접 호출하지 않음

---

## 주요 의존성

- Spring Boot
- Kotlin
- PostgreSQL (로컬/운영)
- H2 (테스트)
- Elasticsearch (옵션)
- JUnit 5

---

## 기타

- 환경별 설정은 `src/main/resources`의 properties 파일로 관리합니다.
- 환경별로 `spring.profiles.active`는 실행 시점에만 지정하는 것이 좋습니다.

---