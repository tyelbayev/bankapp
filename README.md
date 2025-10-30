# Bank Microservices – v3.0

## 1. Локальный запуск разработки

```bash
# Запуск Kafka и PostgreSQL локально
docker-compose up -d kafka zookeeper postgres

# Запуск отдельных сервисов в профиле dev
./mvnw spring-boot:run -pl accounts-service -Dspring-boot.run.profiles=dev
./mvnw spring-boot:run -pl notifications-service -Dspring-boot.run.profiles=dev
```

Конфигурация Kafka хранится в `application-kafka.yaml` или в ConfigMap при запуске в Kubernetes.
Для тестов используется `@EmbeddedKafka`.

---

## 2. Запуск в Kubernetes через Helm

```bash
# Запуск Minikube
minikube start

# Установка Kafka
helm upgrade --install kafka charts/kafka -n dev -f charts/kafka/values-dev.yaml

# Установка всех микросервисов
helm dependency build charts/umbrella
helm upgrade --install bank charts/umbrella -n dev -f charts/umbrella/values-dev.yaml
```

Порядок запуска:

1. Сначала Kafka (чарт `charts/kafka`),
2. затем микросервисы (umbrella-чарт `charts/umbrella`).

Все параметры Kafka (bootstrap-servers, топики) задаются через ConfigMap каждого сервиса.

---

## 3. Jenkins деплой

В корне проекта расположены два файла пайплайна:

* `Jenkinsfile.kafka` – отдельный pipeline для Kafka-инфраструктуры.
  Выполняет `helm upgrade --install charts/kafka`, использует `values-ENV.yaml`, топики берёт из `autoprovision.topics`.

* `Jenkinsfile.umbrella` – pipeline для всех микросервисов.
  Сначала деплоит Kafka (subset), затем деплоит все сервисы через umbrella-чарт.

**Как применить:**

1. В Jenkins job указать путь к файлу (`Jenkinsfile.kafka` или `Jenkinsfile.umbrella`).
2. Задать параметры:

    * `ENV`: dev / test / prod
    * `NAMESPACE`: окружение (например, dev)
    * `RELEASE`: имя Helm-релиза (`bank` или `kafka`)
3. Jenkins выполнит `helm dependency build` и `helm upgrade --install` с нужными values.

---

## 4. Kafka топики и стратегии доставки

| Топик             | Продюсер(ы)                    | Консьюмер       | Семантика     | Порядок      | Описание                              |
| ----------------- | ------------------------------ | --------------- | ------------- | ------------ | ------------------------------------- |
| `notifications`   | `accounts`, `cash`, `transfer` | `notifications` | At least once | Неупорядочен | Уведомления о действиях пользователей |
| `exchange-events` | `exchange-generator`           | `exchange`      | At most once  | Упорядочен   | Поток событий обменных курсов         |

**At least once (unordered)**

* Ручное подтверждение (`ack.acknowledge()`), при ошибке сообщение придёт снова.
* Конкурентность допускается (`concurrency > 1`).

**At most once (ordered)**

* Авто-коммит до обработки (`enable-auto-commit=true`).
* Только одна партиция и `concurrency=1`.
* При рестарте consumer позиционируется в конец (`ConsumerSeekAware.seekToEnd()`).

---
