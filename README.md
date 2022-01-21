# QChain

Экспериментальная блокчейн БД работающая на основе библиотек Kalkan.

## Зависимости

Для сборки необходимы библиотеки kalkanCrypt:

```
commons-logging-1.1.1.jar
kalkancrypt-0.6.1.jar
kalkancrypt_xmldsig-0.3.jar
knca_provider_util-0.7.1.jar
xmlsec-1.5.8.jar
```

Их необходимо поместить в папку `libs`.

## Запуск

```bash
./gradlew bootRun
```

Все методы будут доступны по адресу: http://localhost:8080/swagger-ui.html

## Добавление транзакцции в блокчейн

Пример запроса тут: [requests/transactions_post.http](requests/transactions_post.http)

## Лицензия

MIT

## Разработчик

Malik Zharykov (https://github.com/malikzh)