# Spring Boot 테스트 환경 구축 중 겪었던 문제와 해결 과정

## 1. 문제 요약

### 1 DB 설정 문제

- 테스트 실행 시, Spring Boot가 실제 DB(PostgreSQL)에 연결을 시도함
- 로컬 DB가 실행 중이 아니거나, CI 환경 등에서 DB가 준비되어 있지 않으면 테스트 실패
- 에러 메시지 예시:

  ```bash
  Caused by: org.springframework.beans.factory.BeanCreationException
  Caused by: org.springframework.boot.autoconfigure.jdbc.DataSourceProperties$DataSourceBeanCreationException
  ```

### 2 포트 충돌(PortInUseException) 문제

- 테스트 코드에서 `@SpringBootTest`와 `main()`을 동시에 사용
- Spring Boot 서버가 두 번 실행되면서 동일 포트(8080)를 중복 사용
- 에러 메시지 예시:

  ```bash
  Caused by: org.springframework.boot.web.server.PortInUseException
  Caused by: java.net.BindException
  ```

---

## 2. 원인 분석 및 해결 방법

### 2-1 DB 설정 문제

#### 원인

- 테스트 환경에 맞는 DB 설정(`src/test/resources/application.properties`)이 없거나,
- `spring.profiles.active`가 잘못 지정되어 실제 DB 설정이 적용됨

#### 해결 방법

- `src/test/resources/application.properties`에 H2 인메모리 DB 설정 추가

  ```properties
  spring.datasource.url=jdbc:h2:mem:testdb
  spring.datasource.driver-class-name=org.h2.Driver
  spring.datasource.username=sa
  spring.datasource.password=
  spring.jpa.hibernate.ddl-auto=create-drop
  spring.jpa.show-sql=false
  ```

- build.gradle에 H2 의존성 추가

  ```kotlin
  testImplementation("com.h2database:h2")
  ```

- 모든 환경별 properties 파일에서 `spring.profiles.active` 설정 제거  
  (실행 시점에만 지정)

---

### 2-2 포트 충돌(PortInUseException) 문제

#### 2-2-1 원인

- `@SpringBootTest`는 테스트 실행 시 Spring Boot 서버를 띄움
- 테스트 코드에서 `main()`을 직접 호출하면 서버가 한 번 더 실행되어 포트 충돌 발생

#### 2-2-2 해결 방법

- `@SpringBootTest`를 사용하는 테스트에서는 `main()`을 직접 호출하지 않음
- 만약 main 함수의 출력을 테스트하고 싶으면, `@SpringBootTest` 없이 일반 단위 테스트로 작성
- 또는, `@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)`로 지정해 포트 충돌 방지

---

## 3. 최종 테스트 코드 예시

### (1) Spring Boot 컨텍스트 테스트

```kotlin
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class NotesimpleApplicationTests {
    @Test
    fun contextLoads() {
        // 컨텍스트 로드 테스트
    }
}
```

### (2) main 함수 출력 테스트 (단위 테스트)

```kotlin
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class NotesimpleApplicationUnitTests {
    @Test
    fun testHelloWorldOutput() {
        val outputStream = ByteArrayOutputStream()
        val originalOut = System.out
        System.setOut(PrintStream(outputStream))

        main(arrayOf())

        System.setOut(originalOut)
        val output = outputStream.toString().trim()
        assertTrue(output.contains("hello world"))
    }
}
```

---

## 4. 결론 및 팁

- 테스트 환경에서는 실제 DB 대신 H2 인메모리 DB를 사용하자.
- `@SpringBootTest`와 `main()` 직접 호출을 동시에 사용하지 말자.
- 환경별 프로퍼티 파일에서 `spring.profiles.active`는 제거하고, 실행 시점에만 지정하자.
- 포트 충돌이 발생하면 `webEnvironment = RANDOM_PORT` 옵션을 활용하자.

---

이 문서를 프로젝트의 `docs/` 폴더 등에 저장해두면,  
향후 비슷한 문제를 겪을 때 빠르게 해결할 수 있습니다!
