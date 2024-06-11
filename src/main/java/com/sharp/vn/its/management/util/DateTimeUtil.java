package com.sharp.vn.its.management.util;

import java.time.LocalDateTime;

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
}
