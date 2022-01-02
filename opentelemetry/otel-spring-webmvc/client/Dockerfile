FROM openjdk:8-jdk-alpine as builder
MAINTAINER "wangxw"
WORKDIR app
COPY *.jar app.jar

RUN java -Djarmode=layertools -jar app.jar extract

FROM openjdk:8-jdk-alpine

ENV JVM_OPTS="" \
    JAVA_OPTS="" \
    LOG_PATH="/opt/log"

WORKDIR app

COPY --from=builder app/dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./

RUN wget https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.9.1/opentelemetry-javaagent.jar &&\
    mkdir -p "$LOG_PATH"


ENTRYPOINT ["sh", "-c", "java -javaagent:./opentelemetry-javaagent.jar $JVM_OPTS org.springframework.boot.loader.JarLauncher $JAVA_OPTS"]


