# Weather Sensor REST API

RESTful веб-сервис для регистрации метеорологических сенсоров, приёма показаний (температура, наличие осадков) и получения статистики по погодным данным.

## 🛠 Технологический стек

* **Язык:** Java 21
* **Фреймворк:** Spring Boot (Web MVC, Data JPA, Validation)
* **База данных:** PostgreSQL
* **Инструменты:** Maven, ModelMapper, Hibernate Validator
* **Архитектура:** Слоистая архитектура (`Controller` → `Service` → `Repository`), разделение сущностей БД и транспортных объектов через паттерн `DTO`, централизованная обработка ошибок и кастомная валидация.

---

## 📌 Основные возможности и API эндпоинты

### 1. Управление сенсорами (`/sensors`)

| Метод | Эндпоинт | Описание | Статусы ответа |
| :--- | :--- | :--- | :--- |
| `POST` | `/sensors/registration` | Регистрация нового сенсора в системе (с проверкой уникальности имени) | `200 OK`, `400 Bad Request` |
| `GET` | `/sensors` | Получение списка всех зарегистрированных сенсоров | `200 OK` |
| `GET` | `/sensors/{id}` | Получение информации о сенсоре по его ID | `200 OK`, `404 Not Found` |
| `PATCH` | `/sensors/{id}` | Обновление данных сенсора | `200 OK`, `400 Bad Request` |
| `DELETE` | `/sensors/{id}` | Удаление сенсора по ID | `204 No Content` |

**Пример запроса на регистрацию сенсора (`POST /sensors/registration`):**
```json
{
  "name": "WeatherSensorNorth"
}
```
> *Правила валидации:* название должно начинаться с заглавной латинской буквы и содержать от 3 до 30 символов.

---

### 2. Измерения и статистика (`/measurements`)

| Метод | Эндпоинт | Описание | Статусы ответа |
| :--- | :--- | :--- | :--- |
| `POST` | `/measurements/add` | Добавление нового измерения от зарегистрированного сенсора | `200 OK`, `400 Bad Request`, `404 Not Found` |
| `GET` | `/measurements` | Получение истории всех измерений | `200 OK` |
| `GET` | `/measurements/rainyDays` | Получение списка всех измерений, когда фиксировался дождь | `200 OK` |
| `GET` | `/measurements/rainyDays/count` | Получение общего количества дождливых записей | `200 OK` |

**Пример запроса на добавление измерения (`POST /measurements/add`):**
```json
{
  "value": 21.5,
  "raining": true,
  "sensor": {
    "name": "WeatherSensorNorth"
  }
}
```
> *Правила валидации:* температура (`value`) от `-100.0` до `100.0`, обязательное поле `raining` (`true`/`false`), сенсор должен быть предварительно зарегистрирован в базе.

---

## ⚙️ Запуск проекта локально

### 1. Требования
* JDK 21+
* PostgreSQL 14+
* Maven 3.8+ (или встроенный `./mvnw`)

### 2. Настройка базы данных
Создайте базу данных в PostgreSQL и укажите параметры подключения в файле `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/sensor_db
spring.datasource.username=postgres
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 3. Сборка и запуск
```bash
git clone https://github.com
cd Sensor
./mvnw spring-boot:run
```
После запуска сервис будет доступен по адресу: `http://localhost:8080`.
