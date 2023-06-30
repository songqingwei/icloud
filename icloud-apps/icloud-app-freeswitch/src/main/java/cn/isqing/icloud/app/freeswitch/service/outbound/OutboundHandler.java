package cn.isqing.icloud.app.freeswitch.service.outbound;

import lombok.extern.slf4j.Slf4j;
import org.freeswitch.esl.client.dptools.Execute;
import org.freeswitch.esl.client.dptools.ExecuteException;
import org.freeswitch.esl.client.internal.Context;
import org.freeswitch.esl.client.outbound.IClientHandler;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.freeswitch.esl.client.transport.message.EslHeaders;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class OutboundHandler implements IClientHandler {

    private Map<String, Boolean> checkMusic = new ConcurrentHashMap<>();

    public static String nameMapToString(Map<EslHeaders.Name, String> map,
                                         List<String> lines) {
        StringBuilder sb = new StringBuilder("\nHeaders:\n");
        for (EslHeaders.Name key : map.keySet()) {
            if (key == null)
                continue;
            sb.append(key);
            sb.append("\n\t\t\t\t = \t ");
            sb.append(map.get(key));
            sb.append("\n");
        }
        if (lines != null) {
            sb.append("Body Lines:\n");
            for (String line : lines) {
                sb.append(line);
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public void onConnect(Context context, EslEvent eslEvent) {

        long threadId = Thread.currentThread().getId();

        String uuid = eslEvent.getEventHeaders().get("Unique-ID");
        log.debug(nameMapToString(eslEvent.getMessageHeaders(), eslEvent.getEventBodyLines()));
        log.info("Creating execute app for uuid {},threadId:" + threadId, uuid);

        try {
            Execute exe = new Execute(context, uuid);
            // subscribe event
            // EslMessage eslMessage = context.sendCommand("event plain ALL");
            // if (eslMessage.getHeaderValue(EslHeaders.Name.REPLY_TEXT).startsWith("+OK")) {
            //     log.info("subscribe event success!");
            // }
            exe.answer();
            exe.playback("my/turandeziwo.wav");
            exe.set("hangup_after_bridge","true");
            String dest = eslEvent.getEventHeaders().get("Channel-Destination-Number");
            exe.bridge("user/" + dest);
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
