FROM eclipse-temurin:21-jre-jammy

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+UseContainerSupport"

WORKDIR /app

EXPOSE 8081

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
