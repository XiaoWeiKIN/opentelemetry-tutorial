This is a simple example that demonstrates the usage of the [OpenTelemetry SDK Autoconfigure](https://github.com/open-telemetry/opentelemetry-java/tree/main/sdk-extensions/autoconfigure) module.

# Server

-Dotel.traces.exporter=jaeger,logging -Dotel.exporter.jaeger.endpoint=http://localhost:14250 -Dotel.service.name=otel-autosdk-server

# Client

-Dotel.traces.exporter=jaeger,logging -Dotel.exporter.jaeger.endpoint=http://localhost:14250 -Dotel.service.name=otel-autosdk-client