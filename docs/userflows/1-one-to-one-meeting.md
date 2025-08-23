## Встреча one-to-one

Алиса и Боб планируют часовую встречу one-to-one, чтобы обсудить результаты Боба за полугодие, и хотят договориться о 
дате и времени такой встречи. Для этого они используют наш сервис, где Алиса указывает время, когда она доступна, а Боб
выбирает удобный слот. После этого у Алиса и Боб видят, когда состоится встреча, и могут внести её в свои календари.

#### Шаг 1. Алиса регистрируется в сервисе.

Алиса через главную страницу сервиса либо в приложении выбирает вариант "Зарегистрироваться", вводит в открывшейся форме
свой email и желаемые имя пользователя и пароль, и фронтенд отправляет в бэкенд приложения запрос на регистрацию нового 
пользователя:

```bash
curl -X 'POST' \
  'http://api.example.com:8080/api/v1/public/auth/register' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "username": "alice",
  "password": 12345,
  "email": "alice@mail.com"
}'
```

В случае успешной регистрации ответ будет содержать токен для доступа к приложению и дополнительный токен для обновления 
основного токена:

```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmNmRkZWY5Ni0wZjA4LTQxZjgtOGY2ZC1mY2U5MjU1ODk0MzUiLCJ1c2VybmFtZSI6ImFsaWNlIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3NTU5NTUwNzB9.z_8EE21p6nlwWwgtO-ZxP8c52A0fVOKCIOLmylfAc7yWgXQNk8d8LVLE0RYfDWiJQAa0Zb5kgwcoK4YU9xyUow",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmNmRkZWY5Ni0wZjA4LTQxZjgtOGY2ZC1mY2U5MjU1ODk0MzUiLCJ1c2VybmFtZSI6ImFsaWNlIiwiZXhwIjoxNzU2MDM3ODcwfQ.UiYk98blxZph52Ux13yxQ2JfHPSyYcZjZEL7Bw19dXRva0F1sS5B6G0QDg67djBcntBrk0K3QlgDdH49a9SVbQ"
}
```

#### Шаг 2. Алиса указывает свой часовой пояс и информацию о себе

Для того чтобы Боб мог убедиться, что его приглашает на встречу именно Алиса, Алиса указывает в сервисе своё имя, 
краткую информацию о себе. аватарку. Также Алиса указывает свой часовой пояс, чтобы у Боба была возможность 
автоматически перевести время встречи в его локальное время.

Фронтенд отправляет в бэкенд приложения запрос на создание профиля зарегистрированного пользователя:
```bash
curl -X 'POST' \
  'http://api.example.com:8080/profiles' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmNmRkZWY5Ni0wZjA4LTQxZjgtOGY2ZC1mY2U5MjU1ODk0MzUiLCJ1c2VybmFtZSI6ImFsaWNlIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3NTU5NTUwNzB9.z_8EE21p6nlwWwgtO-ZxP8c52A0fVOKCIOLmylfAc7yWgXQNk8d8LVLE0RYfDWiJQAa0Zb5kgwcoK4YU9xyUow' \
  -H 'x-user-id: f6ddef96-0f08-41f8-8f6d-fce925589435' \
  -H 'Content-Type: application/json' \
  -d '{
  "fullName": "Alice Arno",
  "timezone": "Europe/Paris",
  "description": "Representative of a BigTech.",
  "logo": "https://cdn.example.com/alice.jpg"
}'
```

В случае успешного создания профиля ответ будет содержать код `201 Created`.

#### Шаг 3. Алиса указывает своё рабочее время

Для того чтобы встреча состоялась в рабочее время Алисы, Алиса вводит информацию о своём рабочем графике.

Фронтенд отправляет в бэкенд приложения запросы следующего вида, где указаны интервалы рабочего времени Алисы для 
соответствующего дня недели:
```bash
curl -X 'POST' \
  'http://api.example.com:8080/availability-rules' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmNmRkZWY5Ni0wZjA4LTQxZjgtOGY2ZC1mY2U5MjU1ODk0MzUiLCJ1c2VybmFtZSI6ImFsaWNlIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3NTU5NTUwNzB9.z_8EE21p6nlwWwgtO-ZxP8c52A0fVOKCIOLmylfAc7yWgXQNk8d8LVLE0RYfDWiJQAa0Zb5kgwcoK4YU9xyUow' \
  -H 'x-user-id: f6ddef96-0f08-41f8-8f6d-fce925589435' \
  -H 'Content-Type: application/json' \
  -d '{
  "weekday": "MONDAY",
  "startTime": "10:00",
  "endTime": "19:00"
}'
```

Для каждого такого запроса ответ в случае успеха будет содержать код `201 Created`.

####  Шаг 4. Алиса создаёт встречу

Алиса создаёт встречу и указывает её длительность, тему, предполагаемые даты для встречи и другие параметры.

Фронтенд отправляет в бэкенд приложения запрос на создание новой встречи:
```bash
curl -X 'POST' \
  'http://api.example.com:8080/api/events' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmNmRkZWY5Ni0wZjA4LTQxZjgtOGY2ZC1mY2U5MjU1ODk0MzUiLCJ1c2VybmFtZSI6ImFsaWNlIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3NTU5NTUwNzB9.z_8EE21p6nlwWwgtO-ZxP8c52A0fVOKCIOLmylfAc7yWgXQNk8d8LVLE0RYfDWiJQAa0Zb5kgwcoK4YU9xyUow' \
  -H 'x-user-id: f6ddef96-0f08-41f8-8f6d-fce925589435' \
  -H 'Content-Type: application/json' \
  -d '{
  "title": "Alice + Bob",
  "description": "2026H1 results",
  "eventType": "ONE2ONE",
  "maxParticipants": 1,
  "durationMinutes": 60,
  "bufferBeforeMinutes": 10,
  "bufferAfterMinutes": 15,
  "startDate": "2026-07-20T00:00:00Z",
  "endDate": "2026-07-24T23:59:59Z"
}'
```

В случае успешного создания встречи ответ будет содержать её id и публичную ссылку для доступа к встрече 
незарегистрированных пользователей (в нашем случае — Боба:
```json
{
  "id": "a3330b9a-d044-4f1d-a388-5239116b1681",
  "shareLink": "/api/v1/public/event/6c5a580d-af1d-4cb4-81e9-ef079ecd6925"
}
```

#### Шаг 5. Алиса создаёт слоты для встречи 

После того, как указаны предполагаемые даты для встречи, Алиса запускает генерацию слотов для встречи — необходимой 
длительности и с учётом рабочего графика Алисы (Возможен вариант, когда фронтенд автоматически запускает генерацию 
слотов после успешного создания встречи).

Фронтенд отправляет в бэкенд приложения запрос на создание слотов для встречи:
```bash
curl -X 'POST' \
  'http://api.example.com:8080/api/events/a3330b9a-d044-4f1d-a388-5239116b1681/generate-slots' \
  -H 'accept: */*' \
  -H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmNmRkZWY5Ni0wZjA4LTQxZjgtOGY2ZC1mY2U5MjU1ODk0MzUiLCJ1c2VybmFtZSI6ImFsaWNlIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3NTU5NTUwNzB9.z_8EE21p6nlwWwgtO-ZxP8c52A0fVOKCIOLmylfAc7yWgXQNk8d8LVLE0RYfDWiJQAa0Zb5kgwcoK4YU9xyUow' \
  -H 'x-user-id: f6ddef96-0f08-41f8-8f6d-fce925589435' \
  -d ''
```

В случае успешного создания слотов ответ будет содержать код `201 Created`.

#### Шаг 6. Алиса отправляет Бобу публичную ссылку на встречу

Алиса отправляет Бобу публичную ссылку на встречу: по email либо в мессенджере. 

#### Шаг 7. Боб получает информацию о встрече и доступных слотах

Боб открывает в браузере ссылку от Алисы и получает информацию о планируемой встрече. 

Фронтенд отправляет в бэкенд приложения запрос на получение общей информации о встрече:
```bash
curl -X 'GET' \
  'http://api.example.com:8080/api/v1/public/event/6c5a580d-af1d-4cb4-81e9-ef079ecd6925' \
  -H 'accept: application/json'
```

В случае указания Бобом корректной ссылки ответ будет содержать общую информацию о встрече: 
```json
{
  "title": "Alice + Bob",
  "duration": 60,
  "groupEvent": "ONE2ONE",
  "timeZone": "Europe/Paris"
}
```

Параллельно фронтенд отправляет в бэкенд приложения запрос на получение информации об организаторе встречи:
> [!WARNING]
> Not implemented yet

Параллельно фронтенд отправляет в бэкенд приложения запрос на получение списка доступных слотов:
> [!WARNING]
> Not implemented yet

#### Шаг 8. Боб бронирует слот

Боб бронирует подходящий ему слот для встречи и указывает свои имя и email.

Фронтенд отправляет в бэкенд приложения запрос на создание бронирования:

```bash
curl -X 'POST' \
  'http://api.example.com:8080/api/v1/public/booking' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "eventId": "a3330b9a-d044-4f1d-a388-5239116b1681",
  "slotId": "689ebbfb-1fc4-462c-a07c-95a13b1c9d23",
  "email": "bob@mail.com",
  "name": "Bob"
}'
```

В случае успешного бронирования ответ будет содержать код `200 Ok` и информацию о бронировании.
```json
{
  "id": "b77f6564-e02d-478d-abb8-49fc4e12c819",
  "eventId": "a3330b9a-d044-4f1d-a388-5239116b1681",
  "slotId": "689ebbfb-1fc4-462c-a07c-95a13b1c9d23",
  "startTime": "2026-07-20T10:00:00Z",
  "endTime": "2026-07-20T11:00:00Z",
  "isCanceled": false
}
```

#### Шаг 9. Алиса получает список информацию бронировании Боба

Алиса получает список бронирований на организованные ею встречи и видит информацию о бронировании Боба.

Фронтенд отправляет в бэкенд приложения запрос на получение списка бронирований:
> [!WARNING]
> Not implemented yet

 
#### Шаг 10. Алиса и Боб вносят встречу в свои календари

Алиса и Боб добавляют информацию о согласованных дате и времени встречи в свои календари.
