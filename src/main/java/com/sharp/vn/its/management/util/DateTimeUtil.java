package com.sharp.vn.its.management.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * The type Date util.
 */
public final class DateTimeUtil {
    /**
     * To epoch seconds long.
     *
     * @param localDateTime the local date time
     * @return the long
     */
    public static Long toEpochSeconds(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.toEpochSecond(java.time.ZoneOffset.UTC);
    }

    /**
     * To local date time local date time.
     *
     * @param millis the millis
     * @return the local date time
     */
    public static LocalDateTime toLocalDateTime(Long millis) {
        if (millis == null) {
            return null;
        }
        return Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDateTime();
    }

}
