package cn.isqing.icloud.common.utils.constants;

import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public abstract class DateConstants {

   private DateConstants() {
   }

   public static final ZoneId ZONE_ID = ZoneId.of("GMT+08:00");
   public static final ZoneOffset ZONE_OFFSET = ZoneOffset.of("+8");

   public static final String DATE_FORMAT = "yyyy-MM-dd";
   public static final String SIMPLE_DATE_FORMAT = "yyyyMMdd";

   public static final String YEAR_FORMAT ="yyyy";
   public static final String MONTH_FORMAT ="MM";

   public static final String YEAR_MONTH_FORMAT ="yyyy-MM";
   public static final String SIMPLE_YEAR_MONTH_FORMAT ="yyyyMM";

   public static final String DATE_TIME_FORMAT ="yyyy-MM-dd HH:mm:ss";
   public static final String SIMPLE_DATE_TIME_FORMAT ="yyyyMMddHHmmss";
   public static final String SIMPLE_TIME_FORMAT ="HHmmss";

}
