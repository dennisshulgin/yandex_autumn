<h3>Вступительное задание в школу бэкенд-разработки Яндекс.Академия 2022</h3>
<br/>
<b>Описание: </b> Разработать REST API сервис сбора информации о файлах.
<br/>
<b>Технологии: </b> Java 8, Spring Boot, Spring JPA, Maven, Docker, Postgres.
<br/>
<b>Реализованные функции: </b>
<br/>
- POST /imports - импортирует или обновляет текущие элементы.
<br/>
- GET /nodes/{id} - выводит элемент и его подэлементы.
<br/>
- DELETE /delete/{id} - удаляет элемент и вложенные в него элементы.
<br/>
- GET /updates?date={date} - выводи элементы измененные за последние 24 часа с момента входящей даты.
<br/>
- GET /node/{id}/history - выводит информацию об истории изменения элемента.
<br/>
<br/>

<b>Сборка проекта: </b>
<br/>
./mvnv package
<br/>
docker build -t denisshulgin/yandex_autumn .
<br/>
docker-compose up
<br/>
<br/>
<b>Запуск приложения без сборки:</b>
<br/>
Скопировать файл docker-compose.yaml на сервер
<br/>
docker-compose up
<br/>
Образ с приложением выгружен на Docker Hub https://hub.docker.com/repository/docker/denisshulgin/yandex_autumn