package org.freeswitch.esl.client.outbound;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import org.freeswitch.esl.client.transport.message.EslFrameDecoder;

import java.util.concurrent.*;

public class OutboundChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final IClientHandlerFactory clientHandlerFactory;
    private ExecutorService callbackExecutor = Executors.newSingleThreadExecutor();

    private static ThreadFactory onConnectThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("outbound-onConnect-pool-%d").build();

    // 用于处理onConnect的多线程池
    private static ExecutorService onConnectExecutor = new ThreadPoolExecutor(50, 600,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(2000), onConnectThreadFactory);

    public OutboundChannelInitializer(IClientHandlerFactory clientHandlerFactory) {
        this.clientHandlerFactory = clientHandlerFactory;
    }

    public OutboundChannelInitializer setCallbackExecutor(ExecutorService callbackExecutor) {
        this.callbackExecutor = callbackExecutor;
        return this;
    }

    public static void setOnConnectExecutor(ExecutorService executor) {
        onConnectExecutor = executor;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // Add the text line codec combination first
        pipeline.addLast("encoder", new StringEncoder());
        // Note that outbound mode requires the decoder to treat many 'headers' as body lines
        pipeline.addLast("decoder", new EslFrameDecoder(8092, true));

        // now the outbound client logic
        pipeline.addLast("clientHandler",
                new OutboundClientHandler(
                        clientHandlerFactory.createClientHandler(),
                        callbackExecutor, onConnectExecutor));
    }
}
