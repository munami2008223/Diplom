# Дипломный проект по профессии «Инженер по тестированию»
***Цель проекта***:   автоматизация тестирования комплексного сервиса, взаимодействующего с СУБД и API Банка.

***Задача*** — автоматизировать позитивные и негативные сценарии покупки тура.

*Приложение* — это веб-сервис, который предлагает купить тур по определённой цене двумя способами:

1. Обычная оплата по дебетовой карте.
2. Уникальная технология: выдача кредита по данным банковской карты.

Само приложение не обрабатывает данные по картам, а пересылает их банковским сервисам:

1. Сервису платежей - Payment Gate;
2. Кредитному сервису - Credit Gate.

Приложение в собственной СУБД должно сохранять информацию о том, успешно ли был совершён платёж и каким способом. Данные карт при этом сохранять не допускается.

Заявлена поддержка двух СУБД:

- **MySQL**;
- **PostgreSQL**.

[Ссылка на Дипломное задание](https://github.com/netology-code/qa-diploma)

## Тестовая документация
[Планирование автоматизации тестирования](https://github.com/munami2008223/Diplom/blob/main/documents/Plan.md)

[Отчёт по итогам тестирования](https://github.com/munami2008223/Diplom/blob/main/documents/Report.md)

[Отчет по итогам автоматизации](https://github.com/munami2008223/Diplom/blob/main/documents/Summary.md)

### Запуск приложения
#### Подготовительный этап
- Установать и запустить Docker Desktop;
- Установить и запустить IntelliJ IDE;
- Открыть новый проект, выбрав язык - Java, систему сборки - Gradle
- Склонировать удалённый репозиторий, выполнив в терминале команду
```copy
git clone https://github.com/munami2008223/Diplom.git
```

#### Запуск тестового приложения
Запустить контейнеры с MySQL, PostgreSQL, NodeJS через терминал командой:
```copy
docker-compose up
```
В новой вкладке терминала запустить тестируемое приложение:
- [x] Для **MySQL**:
```copy
java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar artifacts/aqa-shop.jar
```
- [x] Для **PostgreSQL**:
```copy 
java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar
```

Чтобы убедиться, что приложение запущено и готово к работе, перейти по [ссылке](http://localhost:8080/)

### Запуск тестов
В новой вкладке терминала запустить тесты:

- [x] Для **MySQL**:
```copy
./gradlew clean test "-D db.url=jdbc:mysql://localhost:3306/app"
```

- [x] Для **PostgreSQL**:
```copy 
./gradlew clean test "-D db.url=jdbc:postgresql://localhost:5432/app"
```

### Перезапуск тестов и приложения
Для остановки приложения в окне терминала ввести команду ```Ctrl+С```

Для остановки контейнеров в окне терминала ввести команду:
```copy
docker-compose down
```
Повторить шаги по запуску тестового приложения и тестов

### Формирование отчёта о тестировании
Для формирования отчётности через Allure ввести в окне терминала команду
```copy
./gradlew allureServe
```

[![Java CI with Gradle](https://github.com/munami2008223/Diplom/actions/workflows/gradle.yml/badge.svg)](https://github.com/munami2008223/Diplom/actions/workflows/gradle.yml)