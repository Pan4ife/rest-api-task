User Manager + REST API
Bootstrap 사용자 관리 앱에 REST API를 도입하고, 관리자 화면을 JavaScript(fetch)로 API를 호출하는 방식으로 전환한 프로젝트입니다.
Habsida 교육 과정 과제로 진행했습니다.
주요 기능
사용자 CRUD REST API (`/api/users`), 역할 조회 API (`/api/roles`)
관리자 화면은 서버 렌더링 대신 JavaScript `fetch`로 API 호출 후 화면을 갱신 (목록·등록·수정·삭제, 모달 창)
`/api/\*\*`는 `ADMIN`만 접근 가능
입력 검증(`@Valid`) 실패, 아이디 중복, 비밀번호 누락 시 `400 Bad Request`와 오류 메시지 반환
등록 성공 시 `201 Created`와 `Location` 헤더 반환
Spring Security 인증/인가, Bootstrap UI — 이전 단계 기능 유지
REST API
Method	URL	설명	응답
GET	`/api/users`	사용자 목록	200
GET	`/api/users/{id}`	사용자 단건 조회	200 / 404
POST	`/api/users`	사용자 등록	201 / 400
PUT	`/api/users/{id}`	사용자 수정 (비밀번호 미입력 시 기존 값 유지)	200 / 400 / 404
DELETE	`/api/users/{id}`	사용자 삭제	204 / 404
GET	`/api/roles`	역할 목록	200
요청 본문(`UserDto`): `name`, `lastName`, `age`, `username`, `password`, `roleIds`
응답 본문(`UserApiResponseDto`): `id`, `name`, `lastName`, `age`, `username`, `roles` (비밀번호는 응답에 포함하지 않음)
구현 내용
`AdminRestController`, `RoleRestController` (`@RestController`) 작성
엔티티를 직접 노출하지 않도록 요청/응답 DTO 분리 (`UserDto`, `UserApiResponseDto`, `RoleDto`)
`ResponseEntity`로 상태 코드와 본문을 명시적으로 제어
`WebSecurityConfig`에 `/api/\*\*` 접근 규칙 추가
`admin.html`의 JavaScript(`fetch`, `async/await`)로 사용자 목록·역할 조회, 등록·수정·삭제 처리
기술 스택
구분	기술
언어	Java 17, JavaScript
프레임워크	Spring Boot 4.0.6, Spring Security, Spring Web MVC
데이터	Spring Data JPA, Hibernate, MySQL 8
화면	Thymeleaf, Bootstrap 5.3
검증	Bean Validation
빌드	Maven
실행 방법
MySQL 8에서 데이터베이스를 생성합니다.
```sql
   CREATE DATABASE mvc\_hibernate\_db;
   ```
`src/main/resources/application.properties`에서 `spring.datasource.password`를 본인 비밀번호로 수정합니다.
애플리케이션을 실행합니다.
```bash
   ./mvnw spring-boot:run
   ```
(Windows: `mvnw.cmd spring-boot:run`)
http://localhost:8080 에 접속해 아래 데모 계정으로 로그인합니다.
계정	비밀번호	역할
`admin`	`admin`	ADMIN
`user`	`user`	USER
> 학습용 설정으로 `spring.jpa.hibernate.ddl-auto=create`를 사용하므로 실행할 때마다 테이블이 재생성됩니다. 데모 계정은 개발용입니다.
발전 과정
단계	저장소	내용
1	mvc-hibernate	Spring MVC + Hibernate CRUD
2	spring-boot-task	Spring Boot 마이그레이션, Spring Data JPA
3	spring-security-task	Spring Security 인증/인가
4	bootstrap-task	Bootstrap 5 UI
5	rest-api-task (현재)	REST API + JavaScript
