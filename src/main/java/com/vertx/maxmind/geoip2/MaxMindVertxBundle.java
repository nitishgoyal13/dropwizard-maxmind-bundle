package com.vertx.maxmind.geoip2;

import com.vertx.maxmind.geoip2.config.MaxMindConfig;
import com.vertx.maxmind.geoip2.filter.MaxMindFilter;
import com.vertx.maxmind.geoip2.filter.MaxMindVertxGeoIpRequestFilter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Singleton
public class MaxMindVertxBundle {
    public static final String MAX_MIND_CONTEXT_KEY = "com.vertx.maxmind.geoip2.CONTEXT";

    private final MaxMindConfig config;

    @Getter
    private MaxMindFilter maxMindFilter;

    @Inject
    public MaxMindVertxBundle(final MaxMindConfig config) {
        this.config = config;
    }

    public void init() {
        maxMindFilter = new MaxMindVertxGeoIpRequestFilter(config);
        log.info("Maxmind bundle init success");
    }
}
