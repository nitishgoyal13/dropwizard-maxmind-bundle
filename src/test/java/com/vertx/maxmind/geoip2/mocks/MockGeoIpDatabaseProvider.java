package com.vertx.maxmind.geoip2.mocks;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.maxmind.geoip2.DatabaseProvider;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.*;
import com.maxmind.geoip2.record.*;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class MockGeoIpDatabaseProvider implements DatabaseProvider {
    private final List<String> locales = ImmutableList.of("eng");
    private City city = new City(locales, 2, 1001, ImmutableMap.of("eng", "Bangalore"));
    private Continent continent = new Continent(locales, "Asia", 1002,  ImmutableMap.of("eng", "Asis"));
    private Country country = new Country(locales, 2, 1003, "IN", ImmutableMap.of("eng", "India"));
    private Location location = new Location(10, 5000, 12.97, 77.56, 200_000, 1005, "Asia/Kolkata");
    private MaxMind maxmind = new MaxMind();
    private Postal postal = new Postal("560017", 10);
    private RepresentedCountry representedCountry = new RepresentedCountry(locales, 10, 1007, "IN", ImmutableMap.of("eng", "India"), "test");
    private ArrayList<Subdivision> subdivisions;
    private Traits traits = null;

    public MockGeoIpDatabaseProvider() {
        this.subdivisions = new ArrayList<>();
        this.subdivisions.add(new Subdivision(locales, 10, 1009, "IN/KAR",  ImmutableMap.of("eng", "Karnataka")));
    }

    @Override
    public AnonymousIpResponse anonymousIp(final InetAddress ipAddress) throws IOException, GeoIp2Exception {
        return new AnonymousIpResponse("122.166.149.166", true, true, true, true, true);
    }

    @Override
    public AsnResponse asn(final InetAddress ipAddress) throws IOException, GeoIp2Exception {
        return null;
    }

    @Override
    public ConnectionTypeResponse connectionType(final InetAddress ipAddress) throws IOException, GeoIp2Exception {
        return null;
    }

    @Override
    public DomainResponse domain(final InetAddress ipAddress) throws IOException, GeoIp2Exception {
        return null;
    }

    @Override
    public EnterpriseResponse enterprise(final InetAddress ipAddress) throws IOException, GeoIp2Exception {
        return new EnterpriseResponse(city, continent, country, location, maxmind, postal, country, representedCountry, subdivisions, traits);
    }

    @Override
    public IspResponse isp(final InetAddress ipAddress) throws IOException, GeoIp2Exception {
        return null;
    }

    @Override
    public CountryResponse country(final InetAddress ipAddress) throws IOException, GeoIp2Exception {
        return new CountryResponse(continent, country, maxmind, country, representedCountry, traits);
    }

    @Override
    public CityResponse city(final InetAddress ipAddress) throws IOException, GeoIp2Exception {
        return new CityResponse(city, continent, country, location, maxmind, postal, country, representedCountry, subdivisions, traits);
    }
}
