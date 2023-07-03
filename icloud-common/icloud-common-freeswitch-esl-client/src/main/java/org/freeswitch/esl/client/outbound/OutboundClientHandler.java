/*
 * Copyright 2010 david varnes.
 *
 * Licensed under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.freeswitch.esl.client.outbound;

import com.google.common.util.concurrent.Futures;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.freeswitch.esl.client.internal.AbstractEslClientHandler;
import org.freeswitch.esl.client.internal.Context;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.freeswitch.esl.client.transport.message.EslMessage;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Specialised {@link AbstractEslClientHandler} that implements the base connecction logic for an
 * 'Outbound' FreeSWITCH Event Socket connection.  The responsibilities for this class are:
 * <ul><li>
 * To send a 'connect' command when the FreeSWITCH server first establishes a new connection with
 * the socket client in Outbound mode.  This will result in an incoming {@link EslMessage} that is
 * transformed into an {@link EslEvent} that sub classes can handle.
 * </ul>
 * Note: implementation requirement is that an {@link ExecutionHandler} is placed in the processing
 * pipeline prior to this handler. This will ensure that each incoming message is processed in its
 * own thread (although still guaranteed to be processed in the order of receipt).
 */
class OutboundClientHandler extends AbstractEslClientHandler {

    private final IClientHandler clientHandler;
    private final ExecutorService callbackExecutor;
    // 新增用于并发处理onConnect的多线程池
    private final ExecutorService onConnectExecutor;

    public OutboundClientHandler(IClientHandler clientHandler, ExecutorService callbackExecutor, ExecutorService onConnectExecutor) {
        this.clientHandler = clientHandler;
        this.callbackExecutor = callbackExecutor;
        this.onConnectExecutor = onConnectExecutor;
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);

        // Have received a connection from FreeSWITCH server, send connect response
        log.debug("Received new connection from server, sending connect message");

        sendApiSingleLineCommand(ctx.channel(), "connect")
                .thenAccept(
                        response ->
                                // 这里改为线程池执行
                                onConnectExecutor.execute(() -> clientHandler.onConnect(
                                        new Context(ctx.channel(), OutboundClientHandler.this),
                                        new EslEvent(response, true)))
                )
                .exceptionally(throwable -> {
                    ctx.channel().close();
                    handleDisconnectionNotice();
                    return null;
                });
        /**
         * 在outbound的onConnect事件里，如果尝试跟freeswitch发命令，会阻塞，后面的代码无法执行
         * 为什么thenAccept方法内部改为线程池执行就可以了?
         * 这个问题可能和 CompletableFuture 的特性有关。当你使用 thenAccept 方法时，它会在同一个线程里执行回调函数，也就是说，它会等待 sendApiSingleLineCommand 的结果返回后，再在同一个线程里调用 clientHandler.onConnect 方法。如果这个方法里有阻塞的操作，比如发送另一个命令给 freeswitch，那么就会导致整个线程被占用，无法处理其他的事件。如果你把 thenAccept 方法内部改为线程池执行，那么就相当于把回调函数放到了另一个线程里执行，这样就不会阻塞原来的线程，也就可以继续处理其他的事件了。
         * {@link AbstractEslClientHandler#sendApiMultiLineCommand(Channel, List)}
         * apiCalls 的 future 是一个 CompletableFuture 对象，它表示一个异步的操作，可以在完成时返回一个 EslMessage 结果。这个 future 是由 sendApiMultiLineCommand 方法创建并返回的，它会被添加到 apiCalls 这个队列里，等待被处理。
         * 上面的方法返回里future不会阻塞，但是{@link Futures#getUnchecked(Future)} 会调用future.get()等待执行完成
         * 当 channel 写入命令并刷新后，freeswitch 会返回一个响应，这个响应会被 channelRead0 方法接收并解析成一个 EslMessage 对象。然后，channelRead0 方法会从 apiCalls 队列里取出第一个 future，并调用它的 complete 方法，把 EslMessage 作为结果传递给 future。这样，future 就完成了。
         * 问题应该就是这里收到响应，却没有线程执行！改成线程池，在异步线程阻塞，本线程完成future。
         * 为什么是本线程，这就需要继续梳理整个执行逻辑了......
          */
    }

    @Override
    protected void handleEslEvent(final ChannelHandlerContext ctx, final EslEvent event) {
        callbackExecutor.execute(() -> clientHandler.onEslEvent(
                new Context(ctx.channel(), OutboundClientHandler.this), event));
    }

    @Override
    protected void handleAuthRequest(io.netty.channel.ChannelHandlerContext ctx) {
        // This should not happen in outbound mode
        log.warn("Auth request received in outbound mode, ignoring");
    }

    @Override
    protected void handleDisconnectionNotice() {
        log.debug("Received disconnection notice");
    }
}
