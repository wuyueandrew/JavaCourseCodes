package io.github.kimmking.gateway.outbound.netty4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyHttpClientOutboundHandler extends ChannelInboundHandlerAdapter {

    private ChannelHandlerContext preCtx;

    public NettyHttpClientOutboundHandler(ChannelHandlerContext preCtx) {
        this.preCtx = preCtx;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        preCtx.write(msg);
        preCtx.flush();
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}