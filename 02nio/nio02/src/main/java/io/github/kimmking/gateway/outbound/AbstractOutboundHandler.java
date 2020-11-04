package io.github.kimmking.gateway.outbound;

import io.github.kimmking.gateway.filter.HttpRequestFilter;
import io.github.kimmking.gateway.inbound.ProxyServerInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public abstract class AbstractOutboundHandler implements OutboundHandler {

    private ProxyServerInfo proxyServerInfo;

    //过滤器链
    private HttpRequestFilterChain httpRequestFilterChain;

    protected ProxyServerInfo getProxyServerInfo() {
        return proxyServerInfo;
    }

    @Override
    public void init(ProxyServerInfo proxyServerInfo, HttpRequestFilter... filters) {
        this.proxyServerInfo = proxyServerInfo;
        httpRequestFilterChain = new HttpRequestFilterChain(filters);
    }

    @Override
    public final void handle(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        httpRequestFilterChain.doFilter(fullRequest, ctx, 1);
        subHandle(fullRequest, ctx);
    }

}
