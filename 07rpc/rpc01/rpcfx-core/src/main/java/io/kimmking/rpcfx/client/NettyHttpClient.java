package io.kimmking.rpcfx.client;

import com.alibaba.fastjson.JSON;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class NettyHttpClient {

    private ChannelFuture f;

    private HttpClientInboundHandler handler;

    public NettyHttpClient() {
        handler = new HttpClientInboundHandler();
    }

    public void connect(String host, int port) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap b;
        try {
            b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
                    ch.pipeline().addLast(new HttpResponseDecoder());
                    // 客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
                    ch.pipeline().addLast(new HttpRequestEncoder());
                    ch.pipeline().addLast(handler);
                }
            });
            f = b.connect(host, port).sync();
        } finally {
//            workerGroup.shutdownGracefully();
        }

    }

    public ChannelFuture req(RpcfxRequest rpcfxRequest) throws Exception {
        URI uri = new URI("/");
//        String msg = "{\"method\":\"findById\",\"params\":[1],\"serviceClass\":\"io.kimmking.rpcfx.demo.api.UserService\"}";
        String msg = JSON.toJSONString(rpcfxRequest);
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
                uri.toASCIIString(), Unpooled.wrappedBuffer(msg.getBytes(StandardCharsets.UTF_8)));

        // 构建http请求
        request.headers().set(HttpHeaderNames.HOST, "localhost");
        request.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=utf-8");
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderNames.KEEP_ALIVE);
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
        // 发送http请求
        ChannelFuture cf = f.channel().write(request);
        f.channel().flush();
        return cf;
    }

    public RpcfxResponse getResp(String requestId) {
        return handler.getResp(requestId);
    }

    public static class HttpClientInboundHandler extends ChannelInboundHandlerAdapter {

        private final ConcurrentHashMap<String, RpcfxResponse> respMap = new ConcurrentHashMap<>();

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof HttpResponse) {
                HttpResponse response = (HttpResponse) msg;
                System.out.println("CONTENT_TYPE:" + response.headers().get(HttpHeaders.Names.CONTENT_TYPE));
            }
            if(msg instanceof HttpContent) {
                HttpContent content = (HttpContent)msg;
                ByteBuf buf = content.content();
                String respJson = buf.toString(io.netty.util.CharsetUtil.UTF_8);
                System.out.println(respJson);
                RpcfxResponse resp = JSON.parseObject(respJson, RpcfxResponse.class);
                if (resp != null && resp.getRequestId() != null) {
                    respMap.put(resp.getRequestId(), resp);
                }
                buf.release();
            }
        }

        public RpcfxResponse getResp(String requestId) {
            return respMap.get(requestId);
        }

    }

    public static void main(String[] args) throws Exception {
        NettyHttpClient nettyHttpClient = new NettyHttpClient();
        nettyHttpClient.connect("localhost", 8080);
        for (int i = 0; i < 5; i++) {
            System.out.println("======");
            RpcfxRequest request = new RpcfxRequest();
            request.setRequestId(UUID.randomUUID().toString());
            request.setMethod("findById");
            request.setServiceClass("io.kimmking.rpcfx.demo.api.UserService");
            request.setParams(new Object[]{1});
            nettyHttpClient.req(request);
            RpcfxResponse resp = null;
            while (nettyHttpClient.getResp(request.getRequestId()) == null) {
                Thread.sleep(2000L);
            }
            resp = nettyHttpClient.getResp(request.getRequestId());
            System.out.println(JSON.toJSONString(resp));

        }
    }

}
