package cn.isqing.icloud.starter.drools.service.msg;

import cn.isqing.icloud.starter.drools.service.msg.dto.EventMsg;
import cn.isqing.icloud.starter.drools.service.msg.dto.TplChangeMsg;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public interface MsgParserService {

    /**
     * 通用组装消息-默认转为json
     *
     * @param dto
     * @return
     */
    String assembleMsg(Object dto);

    /**
     * 通用事件消息解析
     *
     * @param msg
     * @return
     */
    EventMsg parseEventMsg(String msg);

    TplChangeMsg parseTplChangeEventMsg(String msg);


}
