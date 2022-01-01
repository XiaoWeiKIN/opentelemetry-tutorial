FROM openjdk:8-jdk-alpine as builder
MAINTAINER "wangxw"
WORKDIR app
COPY *.jar app.jar

RUN java -Djarmode=layertools -jar app.jar extract

FROM openjdk:8-jdk-alpine
WORKDIR app
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./

RUN wget https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.9.1/opentelemetry-javaagent.jar

ENV JVM_OPTS=""
ENV JAVA_OPTS=""

ENTRYPOINT ["java", "-javaagent:./opentelemetry-javaagent.jar"]
CMD ["$JVM_OPTS $JAVA_OPTS org.springframework.boot.loader.JarLauncher"]


