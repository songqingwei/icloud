package cn.isqing.icloud.app.freeswitch.service.inbound.impl;

import cn.isqing.icloud.app.freeswitch.common.dto.BridgeDto;
import cn.isqing.icloud.app.freeswitch.service.inbound.InboundService;
import cn.isqing.icloud.app.freeswitch.utils.ClientUtil;
import cn.isqing.icloud.common.utils.uuid.UuidUtil;
import lombok.extern.slf4j.Slf4j;
import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class InboundServiceImpl implements InboundService {

    @Autowired
    private ClientUtil clientUtil;

    @Override
    public void call(String serverId, String caller, String callee) {
        Client client = ClientUtil.client;
        if (client.canSend()) {
            String uuid = UuidUtil.uuid();
            BridgeDto dto = new BridgeDto();
            dto.setCaller(caller);
            dto.setCallee(callee);
            dto.setBusiUuid(uuid);
            ClientUtil.CALL_MAP.put(uuid, dto);
            CompletableFuture<EslEvent> future = client.sendBackgroundApiCommand("originate", "{busi_uuid=" + uuid + "}user/" + callee + " &playback(my/turandeziwo.wav)");
            future.thenAccept(ClientUtil.asyncEslEventConsumer());
        } else {
            log.error("连接不可用");
        }
    }
}
