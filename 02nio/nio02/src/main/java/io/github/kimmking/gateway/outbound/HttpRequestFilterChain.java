package io.github.kimmking.gateway.outbound;

import io.github.kimmking.gateway.filter.HttpRequestFilter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public class HttpRequestFilterChain {

    private int filterCnt;

    private HttpRequestFilter[] requestFilters;

    public HttpRequestFilterChain(HttpRequestFilter[] requestFilters) {
        this.requestFilters = requestFilters;
        if (this.requestFilters == null) {
            this.requestFilters = new HttpRequestFilter[]{};
        }
        filterCnt = requestFilters.length;
    }

    public void doFilter(FullHttpRequest fullRequest, ChannelHandlerContext ctx, int cur) {
        if (cur > filterCnt) {
            return;
        }
        HttpRequestFilter httpRequestFilter = requestFilters[cur - 1];
        httpRequestFilter.filter(fullRequest, ctx);
        doFilter(fullRequest, ctx, ++cur);
    }

}
