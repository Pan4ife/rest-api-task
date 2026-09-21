User Manager — Bootstrap 5
학습용 사용자 관리 웹 애플리케이션입니다. Spring Security 앱에 Bootstrap 5 기반 UI를 적용했습니다. (Habsida 교육 과정 과제)
설명
기능은 이전 단계(Spring Security 인증/인가)와 같고, 화면을 Bootstrap으로 다시 구성했습니다.
ADMIN 화면 — 사용자 목록 테이블, 모달 창으로 사용자 등록·수정, 삭제
USER 화면 — 사용자 정보 카드
공통 — 상단 네비게이션 바(로그인 계정·역할 표시, 로그아웃), 역할에 따라 달라지는 사이드바 메뉴, Bootstrap 로그인 페이지
스택
Spring Boot 4.0.6 — 자동 설정, 내장 Tomcat
Spring Security — 인증/인가, `BCryptPasswordEncoder`
Spring Data JPA + Hibernate — 데이터 접근
Thymeleaf — 서버 사이드 렌더링, fragment로 공통 레이아웃 구성
thymeleaf-extras-springsecurity6 — 템플릿에서 `sec:authorize`, `sec:authentication` 사용
Bootstrap 5.3 (CDN) — 카드, 테이블, 폼, 모달, 네비게이션 컴포넌트
MySQL 8 — 데이터베이스
Maven — 빌드
Java 17
화면 구성
```
fragments.html   ← 공통 네비게이션 바(main-nav), 사이드바(sidebar)
     ↑ th:replace
admin.html       ← 사용자 목록 + 등록/수정 모달  (ADMIN)
user.html        ← 사용자 정보 카드              (USER, ADMIN)
login.html       ← 로그인 페이지
```
Endpoints
Method	URL	설명
GET	`/login`	로그인 페이지
GET	`/user`	사용자 페이지
GET	`/admin/users`	사용자 목록 (관리자, 모달 폼 포함)
POST	`/admin/users`	사용자 등록
POST	`/admin/users/{id}`	사용자 수정 (아이디 중복 시 오류 표시)
POST	`/admin/users/{id}/delete`	사용자 삭제
접근 규칙: `/admin/**`은 `ADMIN`, `/user/**`는 `USER`·`ADMIN`, 로그인 후 역할에 따라 이동합니다.
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
이전 버전(Spring Security)에서 변경된 점
이전	이후
Bootstrap 미적용	Bootstrap 5.3 컴포넌트(카드, 테이블, 폼, 모달, 네비게이션)
등록·수정 별도 페이지 (`new-user`, `edit-user`)	`admin.html` 안의 모달 창으로 통합
공통 fragment 없음	`fragments.html`로 네비게이션 바·사이드바 공통화
사이드바 메뉴 없음	`sec:authorize`로 역할별 메뉴 표시
로그인 사용자 정보 표시 없음	`sec:authentication`으로 계정·역할을 네비게이션 바에 표시
발전 과정
단계	저장소	내용
1	mvc-hibernate	Spring MVC + Hibernate CRUD
2	spring-boot-task	Spring Boot 마이그레이션, Spring Data JPA
3	spring-security-task	Spring Security 인증/인가
4	bootstrap-task (현재)	Bootstrap 5 UI
5	rest-api-task	REST API + JavaScript
