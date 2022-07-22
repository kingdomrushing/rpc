package com.xxx.rpc.transport.netty.server;

import com.xxx.rpc.common.enums.CompressTypeEnum;
import com.xxx.rpc.common.enums.RPCResponseEnum;
import com.xxx.rpc.common.enums.SerializationTypeEnum;
import com.xxx.rpc.common.factory.SingletonFactory;
import com.xxx.rpc.proto.RPCMessage;
import com.xxx.rpc.proto.RPCRequest;
import com.xxx.rpc.proto.RPCResponse;
import com.xxx.rpc.transport.config.TransportConstants;
import com.xxx.rpc.transport.handler.RPCRequestHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Customize the ChannelHandler of the server to process the data sent by the client.
 * <p>
 * 如果继承自 SimpleChannelInboundHandler 的话就不要考虑 ByteBuf 的释放 ，{@link SimpleChannelInboundHandler} 内部的
 * channelRead 方法会替你释放 ByteBuf ，避免可能导致的内存泄露问题。详见《Netty进阶之路 跟着案例学 Netty》
 *
 */
@Slf4j
public class NettyRPCServerHandler extends ChannelInboundHandlerAdapter {

    private final RPCRequestHandler rpcRequestHandler;

    public NettyRPCServerHandler() {
        this.rpcRequestHandler = SingletonFactory.getInstance(RPCRequestHandler.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            if (msg instanceof RPCMessage) {
                log.info("server receive msg: [{}] ", msg);
                byte messageType = ((RPCMessage) msg).getMessageType();
                RPCMessage rpcMessage = new RPCMessage();
                rpcMessage.setCodec(SerializationTypeEnum.HESSIAN.getCode());
                rpcMessage.setCompress(CompressTypeEnum.GZIP.getCode());
                if (messageType == TransportConstants.HEARTBEAT_REQUEST_TYPE) {
                    rpcMessage.setMessageType(TransportConstants.HEARTBEAT_RESPONSE_TYPE);
                    rpcMessage.setData(TransportConstants.PONG);
                } else {
                    RPCRequest rpcRequest = (RPCRequest) ((RPCMessage) msg).getData();
                    // Execute the target method (the method the client needs to execute) and return the method result
                    Object result = rpcRequestHandler.handle(rpcRequest);
                    log.info(String.format("server get result: %s", result.toString()));
                    rpcMessage.setMessageType(TransportConstants.RESPONSE_TYPE);
                    if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                        RPCResponse<Object> rpcResponse = RPCResponse.success(result, rpcRequest.getRequestId());
                        rpcMessage.setData(rpcResponse);
                    } else {
                        RPCResponse<Object> rpcResponse = RPCResponse.fail(RPCResponseEnum.FAIL);
                        rpcMessage.setData(rpcResponse);
                        log.error("not writable now, message dropped");
                    }
                }
                ctx.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        } finally {
            //Ensure that ByteBuf is released, otherwise there may be memory leaks
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                log.info("idle check happen, so close the connection");
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("server catch exception");
        cause.printStackTrace();
        ctx.close();
    }
}
