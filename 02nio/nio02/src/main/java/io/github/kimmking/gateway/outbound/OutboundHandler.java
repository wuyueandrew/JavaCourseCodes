package io.github.kimmking.gateway.outbound;

import io.github.kimmking.gateway.filter.HttpRequestFilter;
import io.github.kimmking.gateway.inbound.ProxyServerInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public interface OutboundHandler {

    /**
     * 初始化后端服务信息和过滤器信息
     * @param proxyServerInfo
     * @param filters
     */
    void init(ProxyServerInfo proxyServerInfo, HttpRequestFilter[] filters);

    /**
     * 处理请求
     * @param fullRequest
     * @param ctx
     */
    void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx);

    /**
     * 处理请求，包含过滤器处理
     * @param fullRequest
     * @param ctx
     */
    void subHandle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx);
}
