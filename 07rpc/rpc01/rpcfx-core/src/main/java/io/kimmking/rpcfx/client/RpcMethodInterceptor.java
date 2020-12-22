package io.kimmking.rpcfx.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.netty.channel.ChannelFuture;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.io.IOException;
import java.util.UUID;

public class RpcMethodInterceptor implements MethodInterceptor {

    static {
        ParserConfig.getGlobalInstance().addAccept("io.kimmking");
    }

    public static final MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");

    private final String proxyClassName;

    private final String url;

    private NettyHttpClient nettyHttpClient;

    public RpcMethodInterceptor(String proxyClassName, String host, int port) {
        this.proxyClassName = proxyClassName;
        this.nettyHttpClient = new NettyHttpClient();
        try {
            nettyHttpClient.connect(host, port);
        } catch (Exception e) {
            throw new RuntimeException("cannot connect server");
        }
        this.url = String.format("http://%s:%d", host, port);
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        RpcfxRequest request = new RpcfxRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setServiceClass(this.proxyClassName);
        request.setMethod(methodInvocation.getMethod().getName());
        request.setParams(methodInvocation.getArguments());

//        RpcfxResponse response = post(request, url);
        nettyHttpClient.req(request);
        while (nettyHttpClient.getResp(request.getRequestId()) == null) {
            Thread.sleep(100L);
        }
        RpcfxResponse response = nettyHttpClient.getResp(request.getRequestId());

        // 这里判断response.status，处理异常
        // 考虑封装一个全局的RpcfxException

        return JSON.parse(response.getResult().toString());
    }

    private RpcfxResponse post(RpcfxRequest req, String url) throws IOException {
        String reqJson = JSON.toJSONString(req);
        System.out.println("req json: "+reqJson);

        // 1.可以复用client
        // 2.尝试使用httpclient或者netty client
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSONTYPE, reqJson))
                .build();
        String respJson = client.newCall(request).execute().body().string();
        System.out.println("resp json: "+respJson);
        return JSON.parseObject(respJson, RpcfxResponse.class);
    }

}
