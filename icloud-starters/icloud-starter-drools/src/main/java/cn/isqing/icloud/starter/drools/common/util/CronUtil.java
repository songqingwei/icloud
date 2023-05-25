package cn.isqing.icloud.starter.drools.common.util;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.drools.core.time.impl.CronExpression;
import sun.util.resources.LocaleData;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Slf4j
public class CronUtil {

    private CronUtil(){
    }

    @Getter
    @Setter
    private static int capacity = 100;

    private static final Map<String, CronExpression> MAP = new LinkedHashMap<String, CronExpression>(capacity,0.75F,true){
        @Override
        protected boolean removeEldestEntry(java.util.Map.Entry<String, CronExpression> eldest) {
            //当链表元素大于容量时，移除最老（最久未被使用）的元素
            return size() > capacity;
        }
    };
    private static final Map<String, Object> ERROR_MAP = new LinkedHashMap<String, Object>(10,0.75F,true){
        @Override
        protected boolean removeEldestEntry(java.util.Map.Entry<String, Object> eldest) {
            //当链表元素大于容量时，移除最老（最久未被使用）的元素
            return size() > capacity;
        }
    };

    public static boolean isSatisfied(String cron, LocalDate localDate) {
        try {
            CronExpression cronExpression = MAP.get(cron);
            if(cronExpression==null){
                cronExpression = new CronExpression(cron);
                MAP.put(cron,cronExpression);
            }
            // date缺少时分秒 自动使用当前时分秒
            LocalDateTime localDateTime = localDate.atTime(LocalTime.now());
            Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            return cronExpression.isSatisfiedBy(date);
        } catch (ParseException e) {
            log.error("解析cron表达式[{}]异常", cron);
            return false;
        }
    }

    public static boolean parse(String cron) {
        CronExpression cronExpression;
        try {
            if(ERROR_MAP.containsKey(cron)){
                return false;
            }
            cronExpression = new CronExpression(cron);
            MAP.put(cron,cronExpression);
            return true;
        } catch (ParseException e) {
            log.error("解析cron表达式[{}]异常", cron);
            ERROR_MAP.put(cron,"");
            return false;
        }
    }

}
