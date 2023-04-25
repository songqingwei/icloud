package cn.isqing.icloud.starter.variable.service.msg.impl;

import cn.isqing.icloud.starter.variable.service.msg.MsgParserService;
import cn.isqing.icloud.starter.variable.service.msg.dto.EventMsg;
import com.alibaba.fastjson2.JSON;
import org.springframework.stereotype.Service;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Service
public class MsgParserServiceImpl implements MsgParserService {
    @Override
    public String assembleMsg(Object dto) {
        return JSON.toJSONString(dto);
    }

    @Override
    public EventMsg parseEventMsg(String msg) {
        return JSON.parseObject(msg, EventMsg.class);
    }
}
