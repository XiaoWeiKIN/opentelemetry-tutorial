package com.xiaowei;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.semconv.trace.attributes.SemanticAttributes;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import static io.opentelemetry.api.common.AttributeKey.stringKey;

public class HttpServer {
    // 在应用程序的生命周期中尽早初始化OpenTelemetry SDK是非常重要的。
    private static final OpenTelemetry openTelemetry = TracingConfiguration.initOpenTelemetry();

    private static final Tracer tracer =
            openTelemetry.getTracer("com.xiaowei.opentelemetry.http.HttpServer");
    private static final int port = 8080;
    private final com.sun.net.httpserver.HttpServer server;
    // Extract the context from http headers
    private static final TextMapGetter<HttpExchange> getter =
            new TextMapGetter<HttpExchange>() {

                @Override
                public Iterable<String> keys(HttpExchange carrier) {
                    return carrier.getRequestHeaders().keySet();
                }

                @Nullable
                @Override
                public String get(@Nullable HttpExchange carrier, String key) {
                    if (carrier.getRequestHeaders().containsKey(key)) {
                        return carrier.getRequestHeaders().get(key).get(0);
                    }
                    return "";
                }
            };

    private HttpServer() throws IOException {
        this(port);
    }

    private HttpServer(int port) throws IOException {
        server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port), 0);
        // Test urls
        server.createContext("/resource", new HelloHandler());
        server.start();
        System.out.println("Server ready on http://127.0.0.1:" + port);
    }

    private static class HelloHandler implements HttpHandler {
        public static final TextMapPropagator TEXT_MAP_PROPAGATOR =
                openTelemetry.getPropagators().getTextMapPropagator();

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Extract the context from the HTTP request
            Context context = TEXT_MAP_PROPAGATOR.extract(Context.current(), exchange, getter);
            try (Scope scope = context.makeCurrent()) {
                // Automatically use the extracted SpanContext as parent.
                Span span = tracer.spanBuilder("GET /resource")
                        .setSpanKind(SpanKind.SERVER)
                        .startSpan();
                try {
                    // Add the attributes defined in the Semantic Conventions
                    span.setAttribute(SemanticAttributes.HTTP_METHOD, "GET");
                    span.setAttribute(SemanticAttributes.HTTP_SCHEME, "http");
                    span.setAttribute(SemanticAttributes.HTTP_HOST, "localhost:" + HttpServer.port);
                    span.setAttribute(SemanticAttributes.HTTP_TARGET, "/resource");
                    // Process the request
                    answer(exchange, span);
                } finally {
                    span.end();
                }
            }
        }
        private void answer(HttpExchange exchange, Span span) throws IOException {
            // Generate an Event
            span.addEvent("Start Processing");

            // Process the request
            String response = "Hello World!";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes(Charset.defaultCharset()));
            os.close();
            System.out.println("Served Client: " + exchange.getRemoteAddress());

            // Generate an Event with an attribute
            Attributes eventAttributes = Attributes.of(stringKey("answer"), response);
            span.addEvent("Finish Processing", eventAttributes);
        }
    }

    private void stop() {
        server.stop(0);
    }

    public static void main(String[] args) throws Exception {
        final HttpServer s = new HttpServer();
        // Gracefully close the server
        Runtime.getRuntime().addShutdownHook(new Thread(s::stop));
    }

}
