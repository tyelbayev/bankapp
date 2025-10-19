#practicum
1. Сборка JAR делается Maven’ом из корня, а Dockerfile только пакует его в образ. Это не требует тянуть в контейнер весь репозиторий с родительским pom.xml.

C:\projects\bankapp
mvn -B -DskipTests package -pl accounts-service -am
2. Создать accounts-service/Dockerfile

FROM eclipse-temurin:21-jre-jammy
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+UseContainerSupport"
WORKDIR /app
EXPOSE 8081
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]

3. Создать  .dockerignore рядом
   **/target/*
   !target/*.jar
   .git
   .idea
   node_modules
   *.iml


4) Запускаем кластер
   minikube start --driver=docker --cpus=2 --memory=2000m

5) minikube addons enable ingress

6) сборка образа локально
   docker build -t accounts:dev .\accounts-service\

7) загрузка в кластер
   minikube image load accounts:dev --overwrite

8) проверяем
   minikube ssh -- 'crictl images | grep accounts'

9) Namespace + секреты
   kubectl create ns dev 2>$null
   kubectl -n dev create secret generic accounts-secrets --from-literal=db-password='bankpass' --from-literal=oauth-client-secret='hh7djDEJH2DuZYaagbabbWRDzu7NubBN' --dry-run=client -o yaml | kubectl apply -f -

10) установка helm
    winget install Helm.Helm
    $env:Path = [System.Environment]::GetEnvironmentVariable('Path','Machine') + ';' + [System.Environment]::GetEnvironmentVariable('Path','User')
    helm version

11) подкорректировать values, development, service
12) Ставим Postgres в dev
    helm repo add bitnami https://charts.bitnami.com/bitnami
    helm repo update

helm upgrade --install accounts-pg bitnami/postgresql -n dev `
--create-namespace `
--set auth.username=bankuser `
--set auth.password=bankpass `
--set auth.database=bankdb `
--set primary.persistence.size=1Gi

14) Деплой чарта
    helm upgrade --install accounts .\charts\accounts -n dev `
    --set image.repository=accounts `
    --set image.tag=dev `
    --set image.pullPolicy=IfNotPresent `
    --set autoscaling.enabled=false `
    --set replicaCount=1 `
    --set serviceAccount.create=false `
    --set serviceAccount.name=default
15) проверка
    minikube kubectl -- get pods,svc -n dev
