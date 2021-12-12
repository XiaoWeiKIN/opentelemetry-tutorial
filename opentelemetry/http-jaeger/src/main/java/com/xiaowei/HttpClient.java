package com.xiaowei;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.context.propagation.TextMapSetter;
import io.opentelemetry.semconv.trace.attributes.SemanticAttributes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

public class HttpClient {
    // 在应用程序的生命周期中尽早初始化OpenTelemetry SDK是非常重要的。
    private static final OpenTelemetry openTelemetry = TracingConfiguration.initOpenTelemetry("otel-http-client");

    private static final Tracer tracer =
            openTelemetry.getTracer("com.xiaowei.opentelemetry.http.HttpClient");
    // 传播器
    private static final TextMapPropagator textMapPropagator =
            openTelemetry.getPropagators().getTextMapPropagator();

    private static final TextMapSetter<HttpURLConnection> setter = HttpURLConnection::setRequestProperty;

    private void makeRequest() throws IOException, URISyntaxException {
        int port = 8080;
        URL url = new URL("http://127.0.0.1:" + port + "/resource");
        int status = 0;
        StringBuilder content = new StringBuilder();

        Span span = tracer.spanBuilder("/resource")
                .setSpanKind(SpanKind.CLIENT)
                .startSpan();
        // put the span into the current Context
        // active span
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute(SemanticAttributes.HTTP_METHOD, "GET");
            span.setAttribute(SemanticAttributes.HTTP_URL, url.toString());

            HttpURLConnection transportLayer = (HttpURLConnection) url.openConnection();
            // 将 Context 添加 Inject 到 http header
            textMapPropagator.inject(Context.current(), transportLayer, setter);
            try {
                // Process the request
                transportLayer.setRequestMethod("GET");
                status = transportLayer.getResponseCode();
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(transportLayer.getInputStream(), Charset.defaultCharset()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
            } catch (Exception e) {
                span.setStatus(StatusCode.ERROR, "HTTP Code: " + status);
            } finally {
                span.end();
            }

            // Output the result of the request
            System.out.println("Response Code: " + status);
            System.out.println("Response Msg: " + content);
        }
    }

        public static void main (String[]args){
            HttpClient httpClient = new HttpClient();

            // Perform request every 5s
            Thread t =
                    new Thread(
                            () -> {
                                while (true) {
                                    try {
                                        httpClient.makeRequest();
                                        Thread.sleep(5000);
                                    } catch (Exception e) {
                                        System.out.println(e.getMessage());
                                    }
                                }
                            });
            t.start();
        }
    }
