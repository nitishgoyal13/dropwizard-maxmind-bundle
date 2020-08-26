package com.vertx.maxmind.geoip2.mocks;

import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("deprecation")
public class MockRoutingContext implements RoutingContext {
    private MockHttpRequest httpRequest = new MockHttpRequest();
    private Map<String, Object> internalMap = new HashMap<>();

    @Override
    public HttpServerRequest request() {
        return httpRequest;
    }

    @Override
    public HttpServerResponse response() {
        return null;
    }

    @Override
    public void next() {

    }

    @Override
    public void fail(final int i) {

    }

    @Override
    public void fail(final Throwable throwable) {

    }

    @Override
    public void fail(final int i, final Throwable throwable) {

    }

    @Override
    public RoutingContext put(final String s, final Object o) {
        internalMap.put(s, o);
        return this;
    }

    @Override
    public <T> T get(final String s) {
        return (T) internalMap.get(s);
    }

    @Override
    public <T> T remove(final String s) {
        return null;
    }

    @Override
    public Map<String, Object> data() {
        return null;
    }

    @Override
    public Vertx vertx() {
        return null;
    }

    @Override
    public String mountPoint() {
        return null;
    }

    @Override
    public Route currentRoute() {
        return null;
    }

    @Override
    public String normalisedPath() {
        return null;
    }

    @Override
    public Cookie getCookie(final String s) {
        return null;
    }

    @Override
    public RoutingContext addCookie(final io.vertx.core.http.Cookie cookie) {
        return null;
    }

    @Override
    public RoutingContext addCookie(final Cookie cookie) {
        return null;
    }

    @Override
    public Cookie removeCookie(final String s, final boolean b) {
        return null;
    }

    @Override
    public int cookieCount() {
        return 0;
    }

    @Override
    public Set<Cookie> cookies() {
        return null;
    }

    @Override
    public Map<String, io.vertx.core.http.Cookie> cookieMap() {
        return null;
    }

    @Override
    public String getBodyAsString() {
        return null;
    }

    @Override
    public String getBodyAsString(final String s) {
        return null;
    }

    @Override
    public JsonObject getBodyAsJson() {
        return null;
    }

    @Override
    public JsonArray getBodyAsJsonArray() {
        return null;
    }

    @Override
    public Buffer getBody() {
        return null;
    }

    @Override
    public Set<FileUpload> fileUploads() {
        return null;
    }

    @Override
    public Session session() {
        return null;
    }

    @Override
    public User user() {
        return null;
    }

    @Override
    public Throwable failure() {
        return null;
    }

    @Override
    public int statusCode() {
        return 0;
    }

    @Override
    public String getAcceptableContentType() {
        return null;
    }

    @Override
    public ParsedHeaderValues parsedHeaders() {
        return null;
    }

    @Override
    public int addHeadersEndHandler(final Handler<Void> handler) {
        return 0;
    }

    @Override
    public boolean removeHeadersEndHandler(final int i) {
        return false;
    }

    @Override
    public int addBodyEndHandler(final Handler<Void> handler) {
        return 0;
    }

    @Override
    public boolean removeBodyEndHandler(final int i) {
        return false;
    }

    @Override
    public boolean failed() {
        return false;
    }

    @Override
    public void setBody(final Buffer buffer) {

    }

    @Override
    public void setSession(final Session session) {

    }

    @Override
    public void setUser(final User user) {

    }

    @Override
    public void clearUser() {

    }

    @Override
    public void setAcceptableContentType(final String s) {

    }

    @Override
    public void reroute(final HttpMethod httpMethod, final String s) {

    }

    @Override
    public List<Locale> acceptableLocales() {
        return null;
    }

    @Override
    public Map<String, String> pathParams() {
        return null;
    }

    @Override
    public String pathParam(final String s) {
        return null;
    }

    @Override
    public MultiMap queryParams() {
        return null;
    }

    @Override
    public List<String> queryParam(final String s) {
        return null;
    }
}
