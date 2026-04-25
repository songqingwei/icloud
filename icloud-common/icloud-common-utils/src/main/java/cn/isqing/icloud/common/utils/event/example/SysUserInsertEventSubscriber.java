package cn.isqing.icloud.common.utils.event.example;

import cn.isqing.icloud.common.utils.annotation.RouteType;
import cn.isqing.icloud.common.utils.constants.TableOperationEventConstants;
import cn.isqing.icloud.common.utils.event.EventMsg;
import cn.isqing.icloud.common.utils.event.EventSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 用户表插入事件订阅者示例
 *
 * @author songqingwei
 * @version 1.0
 */
@Slf4j
//@Component
//@RouteType(
//    r1 = TableOperationEventConstants.R1_TABLE_OPERATION,
//    r2 = TableOperationEventConstants.R2_INSERT,
//    r3 = "sys_user"
//)
public class SysUserInsertEventSubscriber implements EventSubscriber {

    @Override
    public void onEvent(EventMsg eventMsg) {
        log.info("========== 接收到用户插入事件 ==========");
        log.info("事件ID: {}", eventMsg.getEventId());
        log.info("表名: {}", eventMsg.getTableName());
        log.info("数据ID: {}", eventMsg.getDataId());
        log.info("操作时间: {}", eventMsg.getOperateTime());
        log.info("操作结果: {}", Boolean.TRUE.equals(eventMsg.getSuccess()) ? "成功" : "失败");

        if (Boolean.TRUE.equals(eventMsg.getSuccess())) {
            handleUserInserted(eventMsg);
        }
    }

    private void handleUserInserted(EventMsg eventMsg) {
        log.info("处理新用户: ID={}", eventMsg.getDataId());
        sendWelcomeEmail(eventMsg.getDataId());
        initUserConfig(eventMsg.getDataId());
        recordAuditLog(eventMsg);
        log.info("用户插入后处理完成: ID={}", eventMsg.getDataId());
    }

    private void sendWelcomeEmail(Long userId) {
        log.info("发送欢迎邮件给用户: {}", userId);
    }

    private void initUserConfig(Long userId) {
        log.info("初始化用户配置: {}", userId);
    }

    private void recordAuditLog(EventMsg eventMsg) {
        log.info("记录审计日志: {}", eventMsg.getEventId());
    }
}
