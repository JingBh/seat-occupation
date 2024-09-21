package top.jingbh.seatoccupation.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateTimeUtils {

    public static ZoneId ZONE = ZoneId.of("Asia/Shanghai");

    public static Instant localDateTimeToInstant(LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.atZone(ZONE).toInstant();
    }

    public static LocalDateTime instantToLocalDateTime(Instant instant) {
        return instant == null ? null : LocalDateTime.ofInstant(instant, ZONE);
    }

    public static long minutesFromNow(Instant instant) {
        return instant == null ? 0 : Math.ceilDiv(instant.toEpochMilli() - Instant.now().toEpochMilli(), 60000);
    }
}
