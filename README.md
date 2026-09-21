User Manager — REST API
학습용 사용자 관리 웹 애플리케이션입니다. Bootstrap 앱에 REST API를 도입하고, 관리자 화면이 JavaScript(`fetch`)로 API를 호출하도록 전환했습니다. (Habsida 교육 과정 과제)
설명
관리자 화면은 페이지 이동 없이 REST API를 호출해 사용자 목록을 불러오고, 등록·수정·삭제 후 화면을 갱신합니다. Spring Security 인증/인가와 Bootstrap UI는 이전 단계 그대로 유지합니다.
`/api/**`는 ADMIN만 접근할 수 있습니다.
검증 실패, 아이디 중복, 비밀번호 누락 시 `400 Bad Request`와 오류 메시지를 반환합니다.
등록 성공 시 `201 Created`와 `Location` 헤더를 반환합니다.
스택
Spring Boot 4.0.6 — 자동 설정, 내장 Tomcat
Spring Web MVC (`@RestController`, `ResponseEntity`) — REST API
Spring Security — 인증/인가, `BCryptPasswordEncoder`
Spring Data JPA + Hibernate — 데이터 접근
Bean Validation — 요청 본문 검증 (`@Valid @RequestBody`)
Thymeleaf + Bootstrap 5.3 — 화면
JavaScript (`fetch`, `async/await`) — API 호출 및 화면 갱신
MySQL 8 — 데이터베이스
Maven — 빌드
Java 17
아키텍처
```
브라우저 (admin.html + JavaScript fetch)
        ↓  JSON
RestController (/api/users, /api/roles)   ← DTO 변환, 상태 코드 결정
        ↓
Service (@Service @Transactional)         ← 비즈니스 로직, 비밀번호 암호화
        ↓
Repository (extends JpaRepository)
        ↓
Entity (User ↔ Role)
        ↓
MySQL
```
REST API
Method	URL	설명	응답
GET	`/api/users`	사용자 목록	200
GET	`/api/users/{id}`	사용자 단건 조회	200 / 404
POST	`/api/users`	사용자 등록	201 / 400
PUT	`/api/users/{id}`	사용자 수정 (비밀번호 비워 두면 기존 값 유지)	200 / 400 / 404
DELETE	`/api/users/{id}`	사용자 삭제	204 / 404
GET	`/api/roles`	역할 목록	200
요청 본문 (`UserDto`): `name`, `lastName`, `age`, `username`, `password`, `roleIds`
응답 본문 (`UserApiResponseDto`): `id`, `name`, `lastName`, `age`, `username`, `roles` — 비밀번호는 응답에 포함하지 않습니다.
화면 Endpoints
Method	URL	설명
GET	`/login`	로그인 페이지
GET	`/user`	사용자 페이지
GET	`/admin/users`	관리자 화면 (JS가 API 호출)
검증
서버 측 Bean Validation (`@Valid` + `BindingResult`):
이름 / 성 — 필수, 1~50자, 문자만 허용 (러시아어 또는 영어)
나이 — 필수, 1~150
로그인 ID — 필수, 1~50자, 중복 불가
비밀번호 — 4~50자 (등록 시 필수, 수정 시 비워 두면 유지)
실행
MySQL 8을 설치하고 데이터베이스를 생성합니다.
```sql
   CREATE DATABASE mvc_hibernate_db;
```
`src/main/resources/application.properties`에 본인의 비밀번호를 입력합니다.
```properties
   spring.datasource.password=YOUR_PASSWORD
```
애플리케이션을 실행합니다.
```bash
   ./mvnw spring-boot:run
```
Windows에서는 `mvnw.cmd spring-boot:run`, 또는 IntelliJ IDEA에서 Run 버튼을 사용합니다.
http://localhost:8080 에 접속해 아래 데모 계정으로 로그인합니다.
계정	비밀번호	역할
`admin`	`admin`	`ADMIN`
`user`	`user`	`USER`
> 학습용 설정(`spring.jpa.hibernate.ddl-auto=create`)이므로 실행할 때마다 테이블이 재생성됩니다. 데모 계정은 개발용입니다.
프로젝트 구조
```
src/main/
├── java/io/slava/usermanager/
│   ├── config/            — WebSecurityConfig, SuccessUserHandler, DataInitializer
│   ├── controller/        — AdminRestController, RoleRestController (API)
│   │                        AdminController, UserController, HomeController (화면)
│   ├── dto/               — UserDto, UserApiResponseDto, RoleDto
│   ├── service/           — UserService, UserDetailServiceImpl
│   ├── repository/        — UserRepository, RoleRepository
│   └── model/             — User, Role
└── resources/
    ├── application.properties
    └── templates/         — admin (JavaScript 포함), user, login, fragments
```
이전 버전(Bootstrap)에서 변경된 점
이전	이후
폼 전송(POST) 후 redirect로 화면 갱신	`fetch`로 API 호출 후 페이지 이동 없이 화면 갱신
`AdminController`가 CRUD 요청 처리	`AdminController`는 화면만, CRUD는 `AdminRestController`
수정 폼 전용 `UserEditDto`	요청·응답 DTO 분리 (`UserDto`, `UserApiResponseDto`, `RoleDto`)
검증 오류를 서버 렌더링 화면에 표시	`400` 상태 코드와 오류 메시지를 JSON 응답으로 반환
삭제 후 목록 화면으로 redirect	`204 No Content` 반환 후 JS가 목록 갱신
`/admin/**`만 접근 규칙 적용	`/api/**`도 `ADMIN` 전용 접근 규칙 추가
발전 과정
단계	저장소	내용
1	mvc-hibernate	Spring MVC + Hibernate CRUD
2	spring-boot-task	Spring Boot 마이그레이션, Spring Data JPA
3	spring-security-task	Spring Security 인증/인가
4	bootstrap-task	Bootstrap 5 UI
5	rest-api-task (현재)	REST API + JavaScript
