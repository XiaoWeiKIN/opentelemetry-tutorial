java -javaagent:D:\workplace\opentelemetry-tutorial\agent\opentelemetry-javaagent.jar
-Dotel.traces.exporter=otlp,logging
-Dotel.metrics.exporter=otlp
-Dotel.exporter.otlp.endpoint=http://10.168.68.71:4317
-Dotel.service.name=user-client