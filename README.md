# User Manager — CRUD на Spring Boot

Учебное веб-приложение для управления пользователями. Реализация классического CRUD-сценария на современном Spring Boot стеке.

## Описание

Админ-панель управления пользователями. Поддерживает просмотр списка, создание, редактирование и удаление записей с серверной валидацией полей.

Проект мигрирован с предыдущей итерации на Spring MVC + Hibernate (без Boot) — итоговая версия использует автоконфигурацию Spring Boot, Spring Data JPA вместо ручного DAO-слоя и встроенный Tomcat вместо внешнего деплоя.

## Стек

- **Spring Boot 4.0.6** — автоконфигурация, встроенный Tomcat, управление зависимостями
- **Spring Data JPA** — репозиторий с автогенерацией CRUD-операций
- **Hibernate 7** + Jakarta Persistence — ORM
- **Hibernate Validator 9** — серверная валидация (Bean Validation 3)
- **Thymeleaf 3** — серверный рендеринг HTML
- **MySQL 8** — БД
- **Maven** — сборка
- **Java 17**

## Архитектура

```
Controller (@Controller)
        ↓
Service (@Service @Transactional)   ← бизнес-логика, транзакции
        ↓
Repository (extends JpaRepository)  ← автогенерируемый CRUD
        ↓
Entity (@Entity)                    ← модель данных
        ↓
MySQL
```

В отличие от предыдущей версии проекта, DAO-слой убран целиком — `JpaRepository` от Spring Data JPA сам предоставляет реализации для `save` / `findAll` / `findById` / `deleteById`.

## Endpoints

| Метод | URL                  | Описание                  |
|-------|----------------------|---------------------------|
| GET   | `/`                  | редирект на `/users`      |
| GET   | `/users`             | список пользователей      |
| GET   | `/users/new`         | форма создания            |
| POST  | `/users`             | создать пользователя      |
| GET   | `/users/{id}/edit`   | форма редактирования      |
| POST  | `/users/{id}`        | сохранить изменения       |
| POST  | `/users/{id}/delete` | удалить пользователя      |

## Валидация

Серверная через Bean Validation (`@Valid` + `BindingResult`):
- **Имя / Фамилия** — непустые, 1–50 символов, только буквы (русские или английские)
- **Возраст** — обязательное, от 1 до 150

Клиентская дополнительно — через HTML5-атрибуты (`pattern`, `required`) в Thymeleaf-шаблонах.

## Запуск

1. Установи MySQL 8 и создай базу:
```sql
   CREATE DATABASE mvc_hibernate_db;
```

2. В `src/main/resources/application.properties` укажи свой пароль:
```properties
   spring.datasource.password=YOUR_PASSWORD
```

3. Запусти приложение:
```bash
   mvn spring-boot:run
```
   или через IntelliJ IDEA — кнопкой **Run** на классе `UserManagerApplication`.

4. Открой в браузере: <http://localhost:8080/users>

При первом старте Hibernate создаст таблицу `users` автоматически (`spring.jpa.hibernate.ddl-auto=update`).

## Структура проекта

```
src/main/
├── java/io/slava/usermanager/
│   ├── controller/        — HTTP-эндпойнты
│   ├── service/           — бизнес-логика, транзакции
│   ├── repository/        — Spring Data JPA репозиторий
│   ├── model/             — JPA-сущности
│   └── UserManagerApplication.java
└── resources/
    ├── application.properties
    └── templates/         — Thymeleaf-шаблоны
```

## Что изменилось при миграции с Spring MVC + Hibernate

| Было                                                            | Стало                                            |
|-----------------------------------------------------------------|--------------------------------------------------|
| `WAR`-сборка под внешний Tomcat                                 | `JAR` с встроенным Tomcat                        |
| Ручная JavaConfig (`AppConfig`, `WebConfig`, `AppInit`)         | `@SpringBootApplication` + `application.properties` |
| Hibernate 5.6 + `javax.persistence`                             | Hibernate 7 + `jakarta.persistence`              |
| `UserDaoImpl` с `EntityManager`                                 | `UserRepository extends JpaRepository`           |
| Ручная конфигурация `DataSource`, `EntityManagerFactory`, `TransactionManager` | автоконфигурация Spring Boot |
| `@Autowired` field injection                                    | constructor injection                            |
