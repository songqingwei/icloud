package cn.isqing.icloud.app.freeswitch.utils;

import cn.isqing.icloud.app.freeswitch.common.dto.BridgeDto;
import cn.isqing.icloud.app.freeswitch.service.outbound.OutboundHandler;
import cn.isqing.icloud.common.utils.uuid.UuidUtil;
import lombok.extern.slf4j.Slf4j;
import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.inbound.IEslEventListener;
import org.freeswitch.esl.client.inbound.InboundConnectionFailure;
import org.freeswitch.esl.client.internal.IModEslApi;
import org.freeswitch.esl.client.outbound.SocketClient;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Service
@Slf4j
public class ClientUtil {

    @Value("${i.outbound.port:9091}")
    public int port;

    public static SocketClient outboundServer;
    public static Client client;

    // uuid,dto
    public static final Map<String, BridgeDto> CALL_MAP = new ConcurrentHashMap<>();

    public void initClient() {
        Client client = new Client();
        ClientUtil.client = client;
        try {
            // 连接freeswitch
            client.connect(new InetSocketAddress("127.0.0.1", 8021), "ClueCon", 10);
            client.setEventSubscriptions(IModEslApi.EventFormat.PLAIN, "ALL");
            client.addEventListener(getListener());
        } catch (InboundConnectionFailure e) {
            log.error("连接失败！", e);
        }
    }

    private static IEslEventListener getListener() {
        return (ctx, event) -> {
            MDC.put("X-B3-SpanId", UuidUtil.randomNum_6());
            String eventName = event.getEventName();
            String busiUuid = event.getEventHeaders().get("variable_busi_uuid");
            String uuid = event.getEventHeaders().get("Unique-ID");
            if (busiUuid == null) {
                log.info("监听到事件:{}", eventName);
            } else {
                log.info("监听到事件:{}[busiUuid:{}]", eventName, busiUuid);
            }
            // CHANNEL_开头的常用事件
            if (eventName.startsWith("CHANNEL_")) {
                switch (eventName) {
                    case "CHANNEL_CREATE":
                        String calleeNumber = event.getEventHeaders().get("Caller-Callee-ID-Number");
                        String callerNumber = event.getEventHeaders().get("Caller-Caller-ID-Number");
                        log.info("发起呼叫, 主叫：" + callerNumber + " , 被叫：" + calleeNumber);
                        break;
                    case "CHANNEL_BRIDGE":
                        channelBridge(event, busiUuid, uuid);
                        break;
                    case "CHANNEL_ANSWER":
                        channelAnswer(event, busiUuid, uuid);
                        break;
                    case "CHANNEL_HANGUP":
                        channelHangup(event, busiUuid);
                        break;
                    default:
                        break;
                }
            }
            MDC.clear();
        };
    }

    private static void channelBridge(EslEvent event, String busiUuid, String uuid) {
        String calleeNumber = event.getEventHeaders().get("Caller-Callee-ID-Number");
        String callerNumber = event.getEventHeaders().get("Caller-Caller-ID-Number");
        log.info("用户转接, 主叫：" + callerNumber + " , 被叫：" + calleeNumber);
        if (busiUuid != null) {
            String command = "uuid_break " + uuid;
            CompletableFuture<EslEvent> future = client.sendBackgroundApiCommand(command, null);
            future.thenAccept(asyncEslEventConsumer());
        }
    }

    public static Consumer<EslEvent> asyncEslEventConsumer() {
        return eslEvent -> {
            MDC.put("X-B3-SpanId", UuidUtil.randomNum_6());
            try {
                String jobUuid = eslEvent.getEventHeaders().get("Job-UUID");
                String eventName = eslEvent.getEventName();
                log.info("监听到异步事件:{}[jobUuid:{}]", eventName, jobUuid);
                if (eslEvent.getEventBodyLines().get(0).startsWith("-ERR")) {
                    log.error("结果:{}", eslEvent.getEventBodyLines());
                } else {
                    log.info("结果:{}", eslEvent.getEventBodyLines());
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            MDC.clear();
        };
    }


    private static void channelHangup(EslEvent event, String busiUuid) {
        String calleeNumber = event.getEventHeaders().get("Caller-Callee-ID-Number");
        String callerNumber = event.getEventHeaders().get("Caller-Caller-ID-Number");
        String response = event.getEventHeaders().get("variable_current_application_response");
        String hangupCause = event.getEventHeaders().get("Hangup-Cause");
        log.info("用户挂断, 主叫：" + callerNumber + " , 被叫：" + calleeNumber + " , response:" + response + " ,hangup cause:" + hangupCause);
        if (busiUuid != null && CALL_MAP.containsKey(busiUuid)) {
            CALL_MAP.remove(busiUuid);
        }
        // if (busiUuid == null) {
        //    String otherLegUid = event.getEventHeaders().get("Other-Leg-Unique-ID");
        //    if (otherLegUid != null) {
        //        String jobUuid = client.sendBackgroundApiCommand("sched_hangup", "0 " + otherLegUid);
        //        log.info("jobUuid:" + jobUuid);
        //    }
        // }
    }



    private static void channelAnswer(EslEvent event, String busiUuid, String uuid) {
        String calleeNumber = event.getEventHeaders().get("Caller-Callee-ID-Number");
        String callerNumber = event.getEventHeaders().get("Caller-Caller-ID-Number");
        log.info("用户应答, 主叫：" + callerNumber + " , 被叫：" + calleeNumber);
        if (busiUuid != null) {
            BridgeDto dto = CALL_MAP.get(busiUuid);
            if (dto != null) {
                String command;
                if (calleeNumber.equals(dto.getCallee())) {
                    dto.setCalleeOk(true);
                    dto.setCalleeUuid(uuid);
                    // String command = String.format("uuid_transfer %s  {busi_uuid=%s,execute_on_transfer=stop_music,hangup_after_bridge=true}user/%s", uuid, busiUuid, dto.getCaller());
                    command = "originate {busi_uuid=" + busiUuid + ",execute_on_bridge=stop_music,hangup_after_bridge=true}user/" + dto.getCaller() + " &playback(my/turandeziwo.wav)";
                } else {
                    dto.setCallerUuid(uuid);
                    command = String.format("uuid_bridge %s %s a_stop_displace=true,b_stop_displace=true,hangup_after_bridge=true", dto.getCallerUuid(), dto.getCalleeUuid());
                }

                CompletableFuture<EslEvent> future = client.sendBackgroundApiCommand(command, null);
                future.thenAccept(asyncEslEventConsumer());
            }
        }
    }

    @PostConstruct
    public void init() {
        initClient();
        SocketClient outboundServer = null;
        try {
            outboundServer = new SocketClient(
                    new InetSocketAddress("localhost", port),
                    () -> new OutboundHandler());
            outboundServer.startAsync();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        ClientUtil.outboundServer = outboundServer;
    }

}
