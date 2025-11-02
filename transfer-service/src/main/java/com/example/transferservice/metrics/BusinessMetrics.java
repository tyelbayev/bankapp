package com.example.transferservice.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class BusinessMetrics {

    private final Counter loginSuccess;
    private final Counter loginFailure;
    private final Counter transferFail;
    private final Counter suspiciousBlocked;
    private final Counter notifySendFail;
    private final AtomicLong fxLastUpdateEpoch = new AtomicLong(0);

    public BusinessMetrics(MeterRegistry registry) {
        new ClassLoaderMetrics().bindTo(registry);
        new JvmMemoryMetrics().bindTo(registry);
        new JvmThreadMetrics().bindTo(registry);
        new ProcessorMetrics().bindTo(registry);

        this.loginSuccess = Counter.builder("auth_login_success_total")
                .description("Successful logins")
                .tags("service", "accounts-service")
                .register(registry);

        this.loginFailure = Counter.builder("auth_login_failure_total")
                .description("Failed logins")
                .tags("service", "accounts-service")
                .register(registry);

        this.transferFail = Counter.builder("transfer_failure_total")
                .description("Failed money transfers")
                .tags("service", "accounts-service")
                .register(registry);

        this.suspiciousBlocked = Counter.builder("suspicious_block_total")
                .description("Blocked suspicious operations")
                .tags("service", "accounts-service")
                .register(registry);

        this.notifySendFail = Counter.builder("notification_send_failure_total")
                .description("Notification sending failures")
                .tags("service", "accounts-service")
                .register(registry);

        Gauge.builder("fx_last_update_epoch_seconds", fxLastUpdateEpoch, AtomicLong::get)
                .description("Last FX rates update epoch seconds")
                .tags("service", "accounts-service")
                .register(registry);
    }

    public void incLoginSuccess(String user) {
        loginSuccess.increment();
        Metrics.counter("auth_login_success_total", "user", user).increment();
    }

    public void incLoginFailure(String user) {
        loginFailure.increment();
        Metrics.counter("auth_login_failure_total", "user", user).increment();
    }

    public void incTransferFail(String fromUser, String toUser, String fromAcc, String toAcc) {
        transferFail.increment();
        Metrics.counter("transfer_failure_total",
                "fromUser", fromUser, "toUser", toUser, "fromAcc", fromAcc, "toAcc", toAcc).increment();
    }

    public void incSuspiciousBlocked(String fromUser, String toUser, String fromAcc, String toAcc) {
        suspiciousBlocked.increment();
        Metrics.counter("suspicious_block_total",
                "fromUser", fromUser, "toUser", toUser, "fromAcc", toAcc, "toAcc", toAcc).increment();
    }

    public void incNotifySendFail(String user) {
        notifySendFail.increment();
        Metrics.counter("notification_send_failure_total", "user", user).increment();
    }

    public void setFxLastUpdate(Instant when) {
        fxLastUpdateEpoch.set(when.getEpochSecond());
    }
}
