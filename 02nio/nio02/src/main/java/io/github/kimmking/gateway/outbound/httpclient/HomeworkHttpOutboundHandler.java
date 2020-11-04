package io.github.kimmking.gateway.outbound.httpclient;

import io.github.kimmking.gateway.outbound.AbstractOutboundHandler;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HomeworkHttpOutboundHandler extends AbstractOutboundHandler {

    @Override
    public void subHandle(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        doGet(fullRequest, ctx);
    }

    private void doGet(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) {
        String service = getProxyServerInfo().getProxyServer() + fullRequest.uri();
        try (final CloseableHttpClient httpclient = HttpClients.createDefault();
             final CloseableHttpResponse response = httpclient.execute(new HttpGet(service))) {
            handleResp(response.getEntity(), ctx);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleResp(HttpEntity httpEntity, final ChannelHandlerContext ctx) {
        FullHttpResponse fullHttpResponse = null;
        try {
            byte[] body = EntityUtils.toByteArray(httpEntity);
            fullHttpResponse = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ctx.write(fullHttpResponse);
            ctx.flush();
            ctx.close();
        }
    }

}
