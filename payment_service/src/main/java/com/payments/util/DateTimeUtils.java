package com.payments.util;

import java.time.Duration;
import java.time.LocalDateTime;

public final class DateTimeUtils {

    private DateTimeUtils() {
    }

    public static long fullHoursBetween(LocalDateTime from, LocalDateTime to) {
        if (to.isBefore(from)) {
            return 0L;
        }
        return Duration.between(from, to).toHours();
    }
}
