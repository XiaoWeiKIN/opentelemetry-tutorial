package com.xiaowei;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;

public final class TracingConfiguration {

    private TracingConfiguration() {
    }

    public static OpenTelemetry initOpenTelemetry() {
        return AutoConfiguredOpenTelemetrySdk.initialize().getOpenTelemetrySdk();
    }
}
