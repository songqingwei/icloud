package cn.isqing.icloud.common.utils.event.example;

import cn.isqing.icloud.common.utils.annotation.RouteType;
import cn.isqing.icloud.common.utils.constants.TableOperationEventConstants;
import cn.isqing.icloud.common.utils.event.TableOperationEventMsg;
import cn.isqing.icloud.common.utils.event.TableOperationEventSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 用户表插入事件订阅者示例
 * 演示如何使用@RouteType注解订阅特定表的特定操作事件
 *
 * @author songqingwei
 * @version 1.0
 */
@Slf4j
@Component
@RouteType(
    r1 = TableOperationEventConstants.R1_TABLE_OPERATION,  // 第一级：表操作（固定前缀）
    r2 = TableOperationEventConstants.R2_INSERT,            // 第二级：插入操作
    r3 = "sys_user"                                          // 第三级：表名
)
public class SysUserInsertEventSubscriber implements TableOperationEventSubscriber {

    @Override
    public void onEvent(TableOperationEventMsg eventMsg) {
        log.info("========== 接收到用户插入事件 ==========");
        log.info("事件ID: {}", eventMsg.getEventId());
        log.info("表名: {}", eventMsg.getTableName());
        log.info("数据ID: {}", eventMsg.getDataId());
        log.info("操作时间: {}", eventMsg.getOperateTime());
        log.info("操作结果: {}", eventMsg.getSuccess() ? "成功" : "失败");
        
        if (eventMsg.getSuccess()) {
            // 在这里处理你的业务逻辑
            handleUserInserted(eventMsg);
        }
    }

    /**
     * 处理用户插入后的业务逻辑
     */
    private void handleUserInserted(TableOperationEventMsg eventMsg) {
        log.info("处理新用户: ID={}", eventMsg.getDataId());
        
        // 示例业务逻辑：
        // 1. 发送欢迎邮件
        sendWelcomeEmail(eventMsg.getDataId());
        
        // 2. 初始化用户配置
        initUserConfig(eventMsg.getDataId());
        
        // 3. 记录审计日志
        recordAuditLog(eventMsg);
        
        log.info("用户插入后处理完成: ID={}", eventMsg.getDataId());
    }

    private void sendWelcomeEmail(Long userId) {
        log.info("发送欢迎邮件给用户: {}", userId);
        // 实现发送邮件逻辑
    }

    private void initUserConfig(Long userId) {
        log.info("初始化用户配置: {}", userId);
        // 实现初始化配置逻辑
    }

    private void recordAuditLog(TableOperationEventMsg eventMsg) {
        log.info("记录审计日志: {}", eventMsg.getEventId());
        // 实现记录日志逻辑
    }
}
