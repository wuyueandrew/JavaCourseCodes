package io.github.kimmking.gateway.outbound.netty4;

import io.github.kimmking.gateway.outbound.AbstractOutboundHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

public class HomeworkNettyOutboundHandler extends AbstractOutboundHandler {

    @Override
    public void subHandle(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
                            socketChannel.pipeline().addLast(new HttpResponseDecoder());
                            //客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
                            socketChannel.pipeline().addLast(new HttpRequestEncoder());
                            socketChannel.pipeline().addLast(new NettyHttpClientOutboundHandler(ctx));
                        }
                    });

            ChannelFuture f = b.connect(getProxyServerInfo().getHost(), getProxyServerInfo().getPort()).sync();
            f.channel().write(fullRequest);
            f.channel().flush();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

}
