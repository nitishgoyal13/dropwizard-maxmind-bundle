package com.vertx.maxmind.geoip2;

import com.maxmind.geoip2.DatabaseProvider;
import com.vertx.maxmind.geoip2.config.MaxMindConfig;
import com.vertx.maxmind.geoip2.filter.MaxMindVertxGeoIpRequestFilter;
import com.vertx.maxmind.geoip2.mocks.MockGeoIpDatabaseProvider;
import com.vertx.maxmind.geoip2.mocks.MockRoutingContext;
import com.vertx.maxmind.geoip2.model.MaxMindInfo;
import com.vertx.maxmind.geoip2.util.MaxMindConstants;
import com.vertx.maxmind.geoip2.util.MaxMindHeaders;
import io.vertx.ext.web.RoutingContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GeoIpFilterTest {
    private RoutingContext routingContext;
    private DatabaseProvider databaseProvider;
    private MaxMindConfig maxMindConfig;
    private MaxMindInfo expectedMaxMindInfo;

    private MaxMindVertxGeoIpRequestFilter geoIpRequestFilter;

    @Before
    public void setup() {
        routingContext = new MockRoutingContext();
        databaseProvider = new MockGeoIpDatabaseProvider();
        maxMindConfig = MaxMindConfig.builder()
                .databaseFilePath("/test")
                .type(MaxMindConstants.COUNTRY_TEXT)
                .maxMindContext(true)
                .build();

        final String unknown = "UNKNOWN";
        geoIpRequestFilter = new MaxMindVertxGeoIpRequestFilter(maxMindConfig, databaseProvider);

        expectedMaxMindInfo = MaxMindInfo.builder()
                .anonymousIp(false)
                .anonymousVpn(false)
                .tor(false)
                .city(unknown)
                .state(unknown)
                .stateIso(unknown)
                .country(unknown)
                .countryIso(unknown)
                .postal(unknown)
                .userType(unknown)
                .connectionType(unknown)
                .isp(unknown)
                .latitude(0.0)
                .longitude(0.0)
                .accuracy(0)
                .build();
    }

    @Test
    public void testNonEnterpriseCountry() {
        geoIpRequestFilter.handle(routingContext);

        Assert.assertEquals("India", routingContext.request().headers().get(MaxMindHeaders.X_COUNTRY));
        Assert.assertEquals("IN", routingContext.request().headers().get(MaxMindHeaders.X_COUNTRY_ISO));

        expectedMaxMindInfo.setCountry("India");
        expectedMaxMindInfo.setCountryIso("IN");
        validateMaxMindInfo(routingContext, expectedMaxMindInfo);
    }

    @Test
    public void testNonEnterpriseCity() {
        maxMindConfig.setType(MaxMindConstants.CITY_TEXT);
        geoIpRequestFilter.handle(routingContext);

        Assert.assertEquals("India", routingContext.request().headers().get(MaxMindHeaders.X_COUNTRY));
        Assert.assertEquals("IN", routingContext.request().headers().get(MaxMindHeaders.X_COUNTRY_ISO));

        Assert.assertEquals("IN/KAR", routingContext.request().headers().get(MaxMindHeaders.X_STATE_ISO));
        Assert.assertEquals("Karnataka", routingContext.request().headers().get(MaxMindHeaders.X_STATE));

        Assert.assertEquals("Bangalore", routingContext.request().headers().get(MaxMindHeaders.X_CITY));

        Assert.assertEquals("560017", routingContext.request().headers().get(MaxMindHeaders.X_POSTAL));

        Assert.assertEquals("12.97", routingContext.request().headers().get(MaxMindHeaders.X_LATITUDE));
        Assert.assertEquals("77.56", routingContext.request().headers().get(MaxMindHeaders.X_LONGITUDE));
        Assert.assertEquals("10", routingContext.request().headers().get(MaxMindHeaders.X_LOCATION_ACCURACY));

        expectedMaxMindInfo.setCountry("India");
        expectedMaxMindInfo.setCountryIso("IN");
        expectedMaxMindInfo.setStateIso("IN/KAR");
        expectedMaxMindInfo.setState("Karnataka");
        expectedMaxMindInfo.setCity("Bangalore");
        expectedMaxMindInfo.setPostal("560017");
        expectedMaxMindInfo.setLatitude(12.97);
        expectedMaxMindInfo.setLongitude(77.56);
        expectedMaxMindInfo.setAccuracy(10);
        validateMaxMindInfo(routingContext, expectedMaxMindInfo);
    }

    @Test
    public void testNonEnterpriseAnonIp() {
        maxMindConfig.setType(MaxMindConstants.ANONYMOUS_TEXT);
        geoIpRequestFilter.handle(routingContext);

        Assert.assertEquals("true", routingContext.request().headers().get(MaxMindHeaders.X_ANONYMOUS_IP));
        Assert.assertEquals("true", routingContext.request().headers().get(MaxMindHeaders.X_ANONYMOUS_VPN));
        Assert.assertEquals("true", routingContext.request().headers().get(MaxMindHeaders.X_TOR));

        expectedMaxMindInfo.setAnonymousIp(true);
        expectedMaxMindInfo.setAnonymousVpn(true);
        expectedMaxMindInfo.setTor(true);
        validateMaxMindInfo(routingContext, expectedMaxMindInfo);
    }

    @Test
    public void testEnterprise() {
        maxMindConfig.setEnterprise(true);
        geoIpRequestFilter.handle(routingContext);

        Assert.assertEquals("India", routingContext.request().headers().get(MaxMindHeaders.X_COUNTRY));
        Assert.assertEquals("IN", routingContext.request().headers().get(MaxMindHeaders.X_COUNTRY_ISO));

        Assert.assertEquals("IN/KAR", routingContext.request().headers().get(MaxMindHeaders.X_STATE_ISO));
        Assert.assertEquals("Karnataka", routingContext.request().headers().get(MaxMindHeaders.X_STATE));

        Assert.assertEquals("Bangalore", routingContext.request().headers().get(MaxMindHeaders.X_CITY));

        Assert.assertEquals("560017", routingContext.request().headers().get(MaxMindHeaders.X_POSTAL));

        Assert.assertEquals("12.97", routingContext.request().headers().get(MaxMindHeaders.X_LATITUDE));
        Assert.assertEquals("77.56", routingContext.request().headers().get(MaxMindHeaders.X_LONGITUDE));
        Assert.assertEquals("10", routingContext.request().headers().get(MaxMindHeaders.X_LOCATION_ACCURACY));

        Assert.assertEquals("true", routingContext.request().headers().get(MaxMindHeaders.X_ANONYMOUS_IP));
        Assert.assertEquals("true", routingContext.request().headers().get(MaxMindHeaders.X_ANONYMOUS_VPN));
        Assert.assertEquals("true", routingContext.request().headers().get(MaxMindHeaders.X_TOR));

        expectedMaxMindInfo.setCountry("India");
        expectedMaxMindInfo.setCountryIso("IN");
        expectedMaxMindInfo.setStateIso("IN/KAR");
        expectedMaxMindInfo.setState("Karnataka");
        expectedMaxMindInfo.setCity("Bangalore");
        expectedMaxMindInfo.setPostal("560017");
        expectedMaxMindInfo.setLatitude(12.97);
        expectedMaxMindInfo.setLongitude(77.56);
        expectedMaxMindInfo.setAccuracy(10);
        expectedMaxMindInfo.setAnonymousIp(true);
        expectedMaxMindInfo.setAnonymousVpn(true);
        expectedMaxMindInfo.setTor(true);
        validateMaxMindInfo(routingContext, expectedMaxMindInfo);
    }

    private void validateMaxMindInfo(final RoutingContext routingContext, final MaxMindInfo expected) {
        final MaxMindInfo maxMindInfo = routingContext.get(MaxMindVertxBundle.MAX_MIND_CONTEXT_KEY);
        System.out.println(maxMindInfo);
        Assert.assertEquals(expected, maxMindInfo);
    }

}
