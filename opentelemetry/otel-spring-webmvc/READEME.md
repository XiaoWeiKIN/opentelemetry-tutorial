java -javaagent:./opentelemetry-javaagent.jar \
-Dotel.traces.exporter=otlp,logging \
-Dotel.metrics.exporter=otlp \
-Dotel.exporter.otlp.endpoint=http://localhost:4317 \
-Dotel.service.name=user-client \
-jar otel-spring-webmvc-client.jar
