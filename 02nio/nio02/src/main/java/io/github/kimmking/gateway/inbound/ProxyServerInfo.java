package io.github.kimmking.gateway.inbound;

public class ProxyServerInfo {

    private String proxyServer;

    private String host;

    private int port;

    public ProxyServerInfo(String proxyServer, String host, int port) {
        this.proxyServer = proxyServer;
        this.host = host;
        this.port = port;
    }

    public String getProxyServer() {
        return proxyServer;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

}
