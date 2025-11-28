package cn.isqing.icloud.starter.variable.service.msg;

import cn.isqing.icloud.starter.variable.service.msg.dto.EventMsg;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public interface MsgParserService {

    /**
     * 通用组装消息-默认转为json
     *
     * @param dto 消息传输对象
     * @return 组装后的消息字符串
     */
    String assembleMsg(Object dto);

    /**
     * 通用事件消息解析
     *
     * @param msg 消息字符串
     * @return 解析后的事件消息对象
     */
    EventMsg parseEventMsg(String msg);

}
