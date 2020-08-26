package com.vertx.maxmind.geoip2.mocks;

import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.*;
import io.vertx.core.net.NetSocket;
import io.vertx.core.net.SocketAddress;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.security.cert.X509Certificate;
import java.util.Map;

public class MockHttpRequest implements HttpServerRequest {
    private MultiMap headers;

    public MockHttpRequest() {
        this.headers =  MultiMap.caseInsensitiveMultiMap();
        this.headers.add("X-FORWARDED-FOR", "122.166.149.166");
    }

    @Override
    public HttpServerRequest exceptionHandler(final Handler<Throwable> handler) {
        return null;
    }

    @Override
    public HttpServerRequest handler(final Handler<Buffer> handler) {
        return null;
    }

    @Override
    public HttpServerRequest pause() {
        return null;
    }

    @Override
    public HttpServerRequest resume() {
        return null;
    }

    @Override
    public HttpServerRequest fetch(final long amount) {
        return null;
    }

    @Override
    public HttpServerRequest endHandler(final Handler<Void> endHandler) {
        return null;
    }

    @Override
    public HttpVersion version() {
        return null;
    }

    @Override
    public HttpMethod method() {
        return null;
    }

    @Override
    public String rawMethod() {
        return null;
    }

    @Override
    public boolean isSSL() {
        return false;
    }

    @Override
    public String scheme() {
        return null;
    }

    @Override
    public String uri() {
        return null;
    }

    @Override
    public String path() {
        return null;
    }

    @Override
    public String query() {
        return null;
    }

    @Override
    public String host() {
        return null;
    }

    @Override
    public long bytesRead() {
        return 0;
    }

    @Override
    public HttpServerResponse response() {
        return null;
    }

    @Override
    public MultiMap headers() {
        return headers;
    }

    @Override
    public String getHeader(final String headerName) {
        return null;
    }

    @Override
    public String getHeader(final CharSequence headerName) {
        return null;
    }

    @Override
    public MultiMap params() {
        return null;
    }

    @Override
    public String getParam(final String paramName) {
        return null;
    }

    @Override
    public SocketAddress remoteAddress() {
        return null;
    }

    @Override
    public SocketAddress localAddress() {
        return null;
    }

    @Override
    public SSLSession sslSession() {
        return null;
    }

    @Override
    public X509Certificate[] peerCertificateChain() throws SSLPeerUnverifiedException {
        return new X509Certificate[0];
    }

    @Override
    public String absoluteURI() {
        return null;
    }

    @Override
    public NetSocket netSocket() {
        return null;
    }

    @Override
    public HttpServerRequest setExpectMultipart(final boolean expect) {
        return null;
    }

    @Override
    public boolean isExpectMultipart() {
        return false;
    }

    @Override
    public HttpServerRequest uploadHandler(final Handler<HttpServerFileUpload> uploadHandler) {
        return null;
    }

    @Override
    public MultiMap formAttributes() {
        return null;
    }

    @Override
    public String getFormAttribute(final String attributeName) {
        return null;
    }

    @Override
    public ServerWebSocket upgrade() {
        return null;
    }

    @Override
    public boolean isEnded() {
        return false;
    }

    @Override
    public HttpServerRequest customFrameHandler(final Handler<HttpFrame> handler) {
        return null;
    }

    @Override
    public HttpConnection connection() {
        return null;
    }

    @Override
    public HttpServerRequest streamPriorityHandler(final Handler<StreamPriority> handler) {
        return null;
    }

    @Override
    public Cookie getCookie(final String name) {
        return null;
    }

    @Override
    public int cookieCount() {
        return 0;
    }

    @Override
    public Map<String, Cookie> cookieMap() {
        return null;
    }
}
