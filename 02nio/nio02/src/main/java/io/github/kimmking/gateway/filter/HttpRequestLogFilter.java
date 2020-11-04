package io.github.kimmking.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class HttpRequestLogFilter implements HttpRequestFilter {

    private static Logger logger = LoggerFactory.getLogger(HttpRequestLogFilter.class);

    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        if (fullRequest == null) {
            return;
        }
        logger.info("请求uri:{}, 请求参数:{}", fullRequest.uri(), fullRequest.content().toString(Charset.forName("UTF-8")));
    }

}
