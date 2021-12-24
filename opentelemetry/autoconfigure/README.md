This is a simple example that demonstrates the usage of the [OpenTelemetry SDK Autoconfigure](https://github.com/open-telemetry/opentelemetry-java/tree/main/sdk-extensions/autoconfigure) module.

# Jaeger Server

-Dotel.traces.exporter=jaeger,logging -Dotel.exporter.jaeger.endpoint=http://localhost:14250 -Dotel.service.name=otel-autosdk-server

# Jaeger Client

-Dotel.traces.exporter=jaeger,logging -Dotel.exporter.jaeger.endpoint=http://localhost:14250 -Dotel.service.name=otel-autosdk-client


# Otel Server

-Dotel.traces.exporter=otlp  -Dotel.exporter.otlp.endpoint=http://localhost:4317 -Dotel.service.name=otel-autosdk-server

# Otel Client

-Dotel.traces.exporter=otlp  -Dotel.exporter.otlp.endpoint=http://localhost:4317 -Dotel.service.name=otel-autosdk-client