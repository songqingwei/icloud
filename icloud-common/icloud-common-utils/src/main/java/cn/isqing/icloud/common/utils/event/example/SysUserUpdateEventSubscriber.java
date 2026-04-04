package cn.isqing.icloud.common.utils.event.example;

import cn.isqing.icloud.common.utils.annotation.RouteType;
import cn.isqing.icloud.common.utils.constants.TableOperationEventConstants;
import cn.isqing.icloud.common.utils.event.TableOperationEventMsg;
import cn.isqing.icloud.common.utils.event.TableOperationEventSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 用户表更新事件订阅者示例
 *
 * @author songqingwei
 * @version 1.0
 */
@Slf4j
@Component
@RouteType(
    r1 = TableOperationEventConstants.R1_TABLE_OPERATION,
    r2 = TableOperationEventConstants.R2_UPDATE,
    r3 = "sys_user"
)
public class SysUserUpdateEventSubscriber implements TableOperationEventSubscriber {

    @Override
    public void onEvent(TableOperationEventMsg eventMsg) {
        log.info("========== 接收到用户更新事件 ==========");
        log.info("事件ID: {}", eventMsg.getEventId());
        log.info("数据ID: {}", eventMsg.getDataId());
        
        if (eventMsg.getSuccess()) {
            log.info("用户信息已更新: ID={}", eventMsg.getDataId());
            // 处理更新后的业务逻辑
            handleUserUpdated(eventMsg);
        }
    }

    private void handleUserUpdated(TableOperationEventMsg eventMsg) {
        log.info("处理用户更新: ID={}", eventMsg.getDataId());
        // 实现你的业务逻辑
    }
}
