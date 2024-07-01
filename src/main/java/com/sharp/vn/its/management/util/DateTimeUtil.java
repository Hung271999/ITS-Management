package com.sharp.vn.its.management.util;

import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

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
        return localDateTime.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
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
        return  LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneOffset.UTC);
    }

    public static LocalDateTime toLocalDateTime(String date,  DateTimeFormatter dateTimeFormatter) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }
        return LocalDateTime.parse(date + " 00:00", dateTimeFormatter);
    }

}
