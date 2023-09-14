package cn.isqing.icloud.app.freeswitch.service.outbound;

import lombok.extern.slf4j.Slf4j;
import org.freeswitch.esl.client.dptools.Execute;
import org.freeswitch.esl.client.dptools.ExecuteException;
import org.freeswitch.esl.client.internal.Context;
import org.freeswitch.esl.client.outbound.IClientHandler;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class OutboundHandler implements IClientHandler {

    @Override
    public void onConnect(Context context, EslEvent eslEvent) {
        try {
            // todo 防止重连
            long threadId = Thread.currentThread().getId();
            Map<String, String> headers = eslEvent.getEventHeaders();
            String uuid = headers.get("Unique-ID");
            log.info("Outbound onConnect:uuid {},threadId {}", uuid, threadId);
            String dest = headers.get("Channel-Destination-Number");
            Execute exe = new Execute(context, uuid);
            // 订阅事件
            // EslMessage eslMessage = context.sendCommand("event plain ALL");
            // if (eslMessage.getHeaderValue(EslHeaders.Name.REPLY_TEXT).startsWith("+OK")) {
            //     log.info("subscribe event success!");
            // }
            exe.answer();
            //头节点播放 尾节点接通后结束头节点的播放
            //exe.playback("my/turandeziwo.wav");
            exe.set("hangup_after_bridge", "true");
            exe.set("inherit_codec", "true");
            // dest如果以0开头则走网关,如0 01 1001就是使用gw-01网关拨打1001
            if(dest.startsWith("0")){
                String destStr = String.format("{rtp_secure_media=forbidden,media_mix_inbound_outbound_codecs=true}sofia/gateway/gw-%s/%s", dest.substring(1,3), dest.substring(3));
                exe.bridge(destStr);
            }else {
                exe.export("rtp_secure_media", "optional", false);
                exe.bridge("{media_mix_inbound_outbound_codecs=true}user/" + dest);
            }
            // exe.hangup("exec over,auto hangup");
        } catch (ExecuteException e) {
            log.error("Could not prompt for digits", e);
        } finally {
            context.closeChannel();
        }
    }

    @Override
    public void onEslEvent(Context ctx, EslEvent event) {
        // long threadId = Thread.currentThread().getId();
        // log.info("OUTBOUND onEslEvent: {},threadId:" + threadId, event.getEventName());
        log.info("outbound事件:" + event.getEventName());
    }
}
