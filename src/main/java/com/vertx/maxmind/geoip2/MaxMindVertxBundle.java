package com.vertx.maxmind.geoip2;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.maxmind.db.NodeCache;
import com.maxmind.geoip2.DatabaseProvider;
import com.maxmind.geoip2.DatabaseReader;
import com.vertx.maxmind.geoip2.config.MaxMindConfig;
import com.vertx.maxmind.geoip2.filter.MaxMindFilter;
import com.vertx.maxmind.geoip2.filter.MaxMindVertxGeoIpRequestFilter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Singleton
public class MaxMindVertxBundle {
    public static final String MAX_MIND_CONTEXT_KEY = "com.vertx.maxmind.geoip2.CONTEXT";

    private final MaxMindConfig config;

    @Getter
    private MaxMindFilter maxMindFilter;

    private DatabaseProvider databaseProvider;

    @Inject
    public MaxMindVertxBundle(final MaxMindConfig config) {
        this.config = config;
    }

    public void init() {
        initializeDataBase();
        maxMindFilter = new MaxMindVertxGeoIpRequestFilter(config, databaseProvider);
        log.info("Maxmind bundle init success");
    }

    private void initializeDataBase() {
        try {
            databaseProvider = new DatabaseReader.Builder(new File(config.getDatabaseFilePath()))
                    .withCache(new NodeCache() {
                        private Cache<Integer, JsonNode> cache = CacheBuilder.newBuilder()
                                .expireAfterAccess(config.getCacheTTL(), TimeUnit.SECONDS)
                                .maximumSize(config.getCacheMaxEntries())
                                .recordStats()
                                .build();

                        @Override
                        public JsonNode get(int i, Loader loader) throws IOException {
                            try {
                                return cache.get(i, () -> loader.load(i));
                            } catch (ExecutionException e) {
                                return null;
                            }
                        }
                    })
                    .build();
        } catch (IOException e) {
            final String message = "Error initializing GeoIP database";
            log.error(message, e);
            throw new RuntimeException(message);
        }
    }
}
