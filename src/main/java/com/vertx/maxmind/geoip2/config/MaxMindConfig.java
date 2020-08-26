package com.vertx.maxmind.geoip2.config;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaxMindConfig {

    @NonNull
    private String databaseFilePath;

    @Builder.Default
    private String remoteIpHeader = "X-FORWARDED-FOR";

    @Builder.Default
    private int cacheTTL = 300;

    @Builder.Default
    private int cacheMaxEntries = 10000;

    @Builder.Default
    private boolean enterprise = false;

    //country, city, anonymous
    private String type;

    @Builder.Default
    private boolean maxMindContext = false;
}