package io.kimmking.rpcfx.client;

import org.springframework.aop.framework.ProxyFactoryBean;

public class RpcProxyFactoryBean extends ProxyFactoryBean {

    public RpcProxyFactoryBean(String proxyClassName, String host, int port) {
        super();
        try {
            setTargetClass(Class.forName(proxyClassName));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("cannot find class");
        }
        addAdvice(new RpcMethodInterceptor(proxyClassName, host, port));
    }

}
