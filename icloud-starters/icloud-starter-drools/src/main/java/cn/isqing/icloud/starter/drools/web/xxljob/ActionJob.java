package cn.isqing.icloud.starter.drools.web.xxljob;

import cn.isqing.icloud.common.utils.time.TimeUtil;
import cn.isqing.icloud.starter.drools.common.constants.EventTypeConstants;
import cn.isqing.icloud.starter.drools.dao.entity.RunLog;
import cn.isqing.icloud.starter.drools.dao.mapper.RunLogMapper;
import cn.isqing.icloud.starter.drools.service.event.EventPublisher;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author songqingwei
 */
@Component
@Slf4j
public class ActionJob {

    @Autowired
    private EventPublisher eventPublisher;
    @Autowired
    private RunLogMapper logMapper;

    private Integer size = 1000;
    private Integer maxRetry = 8;

    @XxlJob(value = "actionJobToMq")
    public ReturnT<String> execute() {
        AtomicLong start = new AtomicLong(0L);
        AtomicLong end = new AtomicLong(0L);
        int page = 1;
        RunLog lastJob = null;
        String busiDate = TimeUtil.now().format(TimeUtil.dateFormatter);
        do {
            log.info("page:{}", page);
            List<RunLog> list = getPandingJob(lastJob, busiDate, start, end);
            if (list.isEmpty()) {
                break;
            }
            lastJob = list.get(list.size() - 1);
            list.forEach(l -> eventPublisher.publishEvent(l.getId(), EventTypeConstants.OUTPUT));
        } while (true);

        return ReturnT.SUCCESS;
    }

    private List<RunLog> getPandingJob(RunLog lastJob, String busiDate, AtomicLong start, AtomicLong end) {
        calibrateInterval(lastJob, busiDate, start, end);
        if (end.get() == 0L) {
            return Collections.emptyList();
        }
        long pageEnd = start.get() + size;
        while (start.get() < end.get()) {
            List<RunLog> list = logMapper.getPendingSubJobRange(start.get(), pageEnd, maxRetry, busiDate);
            start.set(pageEnd);
            if (!list.isEmpty()) {
                return list;
            }
            pageEnd += size;
        }
        return Collections.emptyList();

    }

    /**
     * 使用二分法校准“区间”参数
     *
     * @param lastJob
     * @param busiDate
     * @param start
     * @param end
     */
    private void calibrateInterval(RunLog lastJob, String busiDate, AtomicLong start, AtomicLong end) {
        Optional.of(lastJob).ifPresent(job -> {
            if (lastJob.getId() > start.get()) {
                start.set(lastJob.getId());
            }
        });
        // 不是第一次，不需要校准区间了
        if (start.get() != 0L) {
            return;
        }
        RunLog lastRecord = logMapper.getLastRecord();
        if (lastRecord == null) {
            return;
        }
        end.set(lastRecord.getId());
        LocalDate preDate = LocalDate.parse(busiDate, TimeUtil.dateFormatter).minusDays(1);
        Long preId = logMapper.getOneIdBusiDate(preDate.format(TimeUtil.dateFormatter));
        if (preId != null) {
            start.set(preId);
        }
        calibrateStart(busiDate, start, end);
        calibrateEnd(busiDate, start, end);
    }

    /**
     * 校准start
     *
     * @param busiDate
     * @param start
     * @param end
     */
    private void calibrateStart(String busiDate, AtomicLong start, AtomicLong end) {
        int i = 0;
        long end1 = end.get();
        long start1 = start.get();
        while (i++ < 1000 && end1 - start1 > size) {
            Long startMatch = logMapper.matchBusiDate(start.get() + 1L, busiDate);
            if (startMatch != null) {
                break;
            }
            long m = start1 + (end1 - start1) / 2;
            Long less = logMapper.lessBusiDate(m, busiDate);
            if (less != null) {
                start1 = m;
            } else {
                end1 = m;
            }
        }
        start.set(start1);
    }

    /**
     * 校准end
     *
     * @param busiDate
     * @param start
     * @param end
     */
    private void calibrateEnd(String busiDate, AtomicLong start, AtomicLong end) {
        int i = 0;
        long end1 = end.get();
        long start1 = start.get();
        while (i++ < 1000 && end1 - start1 > size) {
            long m = end1 - (end1 + start1) / 2;
            Long greater = logMapper.greaterBusiDate(m, busiDate);
            if (greater != null) {
                end1 = m;
            } else {
                start1 = m;
            }
        }
        end.set(start1);
    }

}
