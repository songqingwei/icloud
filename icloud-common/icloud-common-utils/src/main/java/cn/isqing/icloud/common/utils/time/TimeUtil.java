package cn.isqing.icloud.common.utils.time;

import cn.isqing.icloud.common.utils.constants.DateConstants;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author songqingwei
 * @version 1.0
 **/
@Slf4j
public class TimeUtil {

    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateConstants.DATE_TIME_FORMAT).withZone(DateConstants.ZONE_ID);

    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT).withZone(DateConstants.ZONE_ID);
    public static final DateTimeFormatter simpleDateFormatter = DateTimeFormatter.ofPattern(DateConstants.SIMPLE_DATE_FORMAT).withZone(DateConstants.ZONE_ID);


    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            log.info("睡眠异常:{}", e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private static volatile LocalDateTime currentTime = null;

    public static void setCurrentTime(LocalDateTime time) {
        currentTime = time;
    }

    public static LocalDateTime now() {
        if (currentTime == null) {
            return LocalDateTime.now();
        }
        return currentTime;
    }

    public static Date nowDate() {
        if (currentTime == null) {
            return new Date();

        }
        return Date.from(currentTime.atZone(DateConstants.ZONE_ID).toInstant());
    }

}
