package cn.isqing.icloud.common.utils.event.example;

import cn.isqing.icloud.common.utils.annotation.RouteType;
import cn.isqing.icloud.common.utils.constants.TableOperationEventConstants;
import cn.isqing.icloud.common.utils.event.TableOperationEventMsg;
import cn.isqing.icloud.common.utils.event.TableOperationEventSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 通用插入事件订阅者示例
 * 监听所有表的插入操作（第三级使用通配符或留空）
 *
 * @author songqingwei
 * @version 1.0
 */
@Slf4j
@Component
@RouteType(
    r1 = TableOperationEventConstants.R1_TABLE_OPERATION,
    r2 = TableOperationEventConstants.R2_INSERT,
    r3 = ""  // 空表示匹配所有表
)
public class GlobalInsertEventSubscriber implements TableOperationEventSubscriber {

    @Override
    public void onEvent(TableOperationEventMsg eventMsg) {
        log.info("========== 接收到全局插入事件 ==========");
        log.info("表名: {}", eventMsg.getTableName());
        log.info("数据ID: {}", eventMsg.getDataId());
        log.info("操作时间: {}", eventMsg.getOperateTime());
        
        // 这里可以处理所有表的新增操作
        // 例如：统一的数据同步、缓存更新等
        handleGlobalInsert(eventMsg);
    }

    private void handleGlobalInsert(TableOperationEventMsg eventMsg) {
        log.info("处理全局插入事件: 表={}, ID={}", 
                eventMsg.getTableName(), eventMsg.getDataId());
        // 实现你的业务逻辑
    }
}
