import org.freeswitch.esl.client.dptools.Execute;
import org.freeswitch.esl.client.dptools.ExecuteException;
import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.internal.Context;
import org.freeswitch.esl.client.internal.IModEslApi;
import org.freeswitch.esl.client.outbound.IClientHandler;
import org.freeswitch.esl.client.outbound.IClientHandlerFactory;
import org.freeswitch.esl.client.outbound.SocketClient;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

import static com.google.common.base.Throwables.throwIfUnchecked;

public class CustomEventTest {
    private static Logger logger = LoggerFactory.getLogger(CustomEventTest.class);
    private static String music = "/usr/local/freeswitch/sounds/music/8000/ponce-preludio-in-e-major.wav";

    public static void main(String[] args) {
        new CustomEventTest();
    }

    public CustomEventTest() {
        try {
            //outbound test
            final SocketClient outboundServer = new SocketClient(
                    new InetSocketAddress("localhost", 8086),
                    new OutboundHandlerFactory());
            outboundServer.startAsync();

            //            inboundClient.setEventSubscriptions(IModEslApi.EventFormat.PLAIN, "CHANNEL_HANGUP_COMPLETE CUSTOM");

//inbound test
            final Client inboundClient = new Client();
            inboundClient.connect(new InetSocketAddress("localhost", 8021), "ClueCon", 10);
//            inboundClient.setEventSubscriptions(IModEslApi.EventFormat.PLAIN, "ALL");
            inboundClient.setEventSubscriptions(IModEslApi.EventFormat.PLAIN, "CHANNEL_CREATE CHANNEL_HANGUP CHANNEL_HANGUP_COMPLETE CHANNEL_DESTROY CUSTOM");
            inboundClient.addEventListener((ctx, event) ->
                    {
//                        String eventName = event.getEventName();
//                        if (eventName.equalsIgnoreCase("CUSTOM") || eventName.contains("HANGUP_COMPLETE")) {
                        String ani = event.getEventHeaders().get("Caller-ANI");
                        String myvar = event.getEventHeaders().get("MY-VAR-1");
                        String myvar2 = event.getEventHeaders().get("variable_MY-VAR-2");
                        System.out.println("INBOUND=> eventName: " + event.getEventName() + ", ani = " + ani + ", myvar = " + myvar + " , myvar2=" + myvar2);
//                        }
                    }
            );

        } catch (Throwable t) {
            throwIfUnchecked(t);
        }
    }


    public class OutboundHandlerFactory implements IClientHandlerFactory {

        @Override
        public IClientHandler createClientHandler() {
            return new OutboundHandler();
        }
    }


    public class OutboundHandler implements IClientHandler {


        @Override
        public void onConnect(Context context, EslEvent eslEvent) {
            try {
                Execute exe = new Execute(context, null);
                StringBuilder sbEvent = new StringBuilder();
                sbEvent.append("Event-Name=").append("CUSTOM").append(",");
                sbEvent.append("Event-Subclass=").append("callcenter::info").append(",");
                //自定义事件中的变量（根据业务需求，可自行添加，注：系统变量并不能覆盖，比如下面的Caller-ANI）
                sbEvent.append("Caller-ANI=").append("999999").append(",");
                //只有业务新增的变量，赋值才有意义
                sbEvent.append("MY-VAR-1=").append("abcdefg").append(",");
                //触发自定义事件
                exe.event(sbEvent.toString());

                //导出变量（会在后续所有事件中都一直存在）
                exe.export("MY-VAR-2", "something", true);

                //其它处理（这里只是示例调用了echo）
                exe.echo();
            } catch (ExecuteException e) {
                e.printStackTrace();
            } finally {
                context.closeChannel();
            }
        }


        @Override
        public void onEslEvent(Context ctx, EslEvent event) {

        }
    }
}
