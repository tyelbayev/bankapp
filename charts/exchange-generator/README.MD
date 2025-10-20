Accounts Service
Описание

Микросервис accounts — отвечает за управление учетными записями пользователей и связанную бизнес-логику.
Разворачивается в кластере Kubernetes с помощью Helm.

Состав

Java 21 / Spring Boot 3.2.5

PostgreSQL 15

Helm Chart для деплоя (charts/accounts)

Docker-образ accounts:dev

Перед запуском

Запустить Minikube

minikube start --driver=docker --cpus=2 --memory=3500m


Создать namespace

kubectl create ns dev


Создать секреты

kubectl -n dev create secret generic accounts-secrets \
--from-literal=db-password='bankpass' \
--from-literal=oauth-client-secret='hh7djDEJH2DuZYaagbabbWRDzu7NubBN'


Запустить PostgreSQL (Bitnami чарт)

helm upgrade --install accounts-pg oci://registry-1.docker.io/bitnamicharts/postgresql \
-n dev --set auth.username=bankuser \
--set auth.password=bankpass \
--set auth.database=bankdb

Сборка и загрузка образа
docker build -t accounts:dev ./accounts-service
minikube image load accounts:dev --overwrite

Деплой
helm upgrade --install accounts ./charts/accounts -n dev \
--set image.repository=accounts \
--set image.tag=dev \
--set image.pullPolicy=IfNotPresent \
--set autoscaling.enabled=false \
--set replicaCount=1 \
--set serviceAccount.create=false \
--set serviceAccount.name=default \
--set livenessProbe=null \
--set readinessProbe=null

Проверка
minikube kubectl -- get deploy,pods,svc -n dev
minikube kubectl -- logs -n dev deploy/accounts


Ожидается, что pod будет в статусе:

READY   STATUS    RESTARTS   AGE
1/1     Running   0          ...

Удаление
helm uninstall accounts -n dev
helm uninstall accounts-pg -n dev
