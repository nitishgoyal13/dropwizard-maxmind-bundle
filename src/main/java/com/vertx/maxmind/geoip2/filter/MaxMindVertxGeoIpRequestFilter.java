package com.vertx.maxmind.geoip2.filter;

import com.google.common.base.Strings;
import com.maxmind.geoip2.DatabaseProvider;
import com.maxmind.geoip2.model.AnonymousIpResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.model.EnterpriseResponse;
import com.maxmind.geoip2.record.*;
import com.vertx.maxmind.geoip2.config.MaxMindConfig;
import com.vertx.maxmind.geoip2.MaxMindVertxBundle;
import com.vertx.maxmind.geoip2.model.MaxMindInfo;
import com.vertx.maxmind.geoip2.util.MaxMindConstants;
import com.vertx.maxmind.geoip2.util.MaxMindHeaders;
import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.InetAddressValidator;

import java.io.IOException;
import java.net.InetAddress;

@Slf4j
public class MaxMindVertxGeoIpRequestFilter implements MaxMindFilter {
    private final MaxMindConfig config;
    private final DatabaseProvider databaseProvider;

    public MaxMindVertxGeoIpRequestFilter(final MaxMindConfig config, final DatabaseProvider databaseProvider) {
        this.config = config;
        this.databaseProvider = databaseProvider;
    }

    @Override
    public void handle(final RoutingContext routingContext) {
        try {
            addMaxMindHeaders(routingContext);

            if(config.isMaxMindContext()) {
                addMaxMindContext(routingContext);
            }
        } catch (Exception e) {
            log.error("Exception while adding maxmind headers", e);
        }
    }

    private void addMaxMindHeaders(final RoutingContext routingContext) throws IOException {
        final MultiMap headers = routingContext.request().headers();
        final String clientAddress = headers.get(config.getRemoteIpHeader());

        if (Strings.isNullOrEmpty(clientAddress)) {
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("Header: {} | Value: {}", config.getRemoteIpHeader(), clientAddress);
        }

        //Multiple Client ip addresses are being sent in case of multiple people stamping the request
        final String[] addresses = clientAddress.split(",");
        final InetAddressValidator inetAddressValidator = InetAddressValidator.getInstance();
        if(!inetAddressValidator.isValid(addresses[0])) {
            log.warn("Invalid IP Address: {}", addresses[0]);
            return;
        }
        final String clientIp = addresses[0];
        InetAddress address;
        if (!Strings.isNullOrEmpty(clientIp)) {
            try {
                address = InetAddress.getByName(clientIp);
            } catch (Exception e) {
                log.warn("Cannot resolve address: {} | Error: {}", clientIp, e.getMessage());
                return;
            }
            //Short circuit if there is no ip address
            if (address == null) {
                log.warn("Cannot resolve address: {}", clientIp);
                return;
            }
            try {
                if (config.isEnterprise()) {
                    final EnterpriseResponse enterpriseResponse = databaseProvider.enterprise(address);
                    if (enterpriseResponse == null) {
                        return;
                    }

                    if (enterpriseResponse.getCountry() != null) {
                        addCountryInfo(enterpriseResponse.getCountry(), routingContext);
                    }

                    if (enterpriseResponse.getMostSpecificSubdivision() != null) {
                        addStateInfo(enterpriseResponse.getMostSpecificSubdivision(), routingContext);
                    }

                    if (enterpriseResponse.getCity() != null) {
                        addCityInfo(enterpriseResponse.getCity(), routingContext);
                    }

                    if (enterpriseResponse.getPostal() != null) {
                        addPostalInfo(enterpriseResponse.getPostal(), routingContext);
                    }

                    if (enterpriseResponse.getLocation() != null) {
                        addLocationInfo(enterpriseResponse.getLocation(), routingContext);
                    }

                    if (enterpriseResponse.getTraits() != null) {
                        addTraitsInfo(enterpriseResponse.getTraits(), routingContext);
                    }

                    final AnonymousIpResponse anonymousIpResponse = databaseProvider.anonymousIp(address);
                    if (anonymousIpResponse != null) {
                        anonymousInfo(anonymousIpResponse, routingContext);
                    }
                } else {
                    switch (config.getType()) {
                        case MaxMindConstants.COUNTRY_TEXT:
                            final CountryResponse countryResponse = databaseProvider.country(address);
                            if (countryResponse != null && countryResponse.getCountry() != null) {
                                addCountryInfo(countryResponse.getCountry(), routingContext);
                            }
                            break;

                        case MaxMindConstants.CITY_TEXT:
                            final CityResponse cityResponse = databaseProvider.city(address);
                            if (cityResponse != null) {
                                if (cityResponse.getCountry() != null) {
                                    addCountryInfo(cityResponse.getCountry(), routingContext);
                                }

                                if (cityResponse.getMostSpecificSubdivision() != null) {
                                    addStateInfo(cityResponse.getMostSpecificSubdivision(), routingContext);
                                }

                                if (cityResponse.getCity() != null) {
                                    addCityInfo(cityResponse.getCity(), routingContext);
                                }

                                if (cityResponse.getPostal() != null) {
                                    addPostalInfo(cityResponse.getPostal(), routingContext);
                                }

                                if (cityResponse.getLocation() != null) {
                                    addLocationInfo(cityResponse.getLocation(), routingContext);
                                }
                            }
                            break;

                        case MaxMindConstants.ANONYMOUS_TEXT:
                            final AnonymousIpResponse anonymousIpResponse = databaseProvider.anonymousIp(address);
                            if (anonymousIpResponse != null) {
                                anonymousInfo(anonymousIpResponse, routingContext);
                            }
                            break;
                    }
                }
            } catch (Exception e) {
                log.warn("GeoIP Error: {}", e.getMessage());
            }
        }
    }

    private void addMaxMindContext(final RoutingContext routingContext) {
        final MultiMap headers = routingContext.request().headers();

        final String anonymousIp = headers.get(MaxMindHeaders.X_ANONYMOUS_IP);
        final String anonymousVpn = headers.get(MaxMindHeaders.X_ANONYMOUS_VPN);
        final String tor = headers.get(MaxMindHeaders.X_TOR);
        final String city = headers.get(MaxMindHeaders.X_CITY);
        final String state = headers.get(MaxMindHeaders.X_STATE);
        final String stateIso = headers.get(MaxMindHeaders.X_STATE_ISO);
        final String postal = headers.get(MaxMindHeaders.X_POSTAL);
        final String connectionType = headers.get(MaxMindHeaders.X_CONNECTION_TYPE);
        final String userType = headers.get(MaxMindHeaders.X_USER_TYPE);
        final String country = headers.get(MaxMindHeaders.X_COUNTRY);
        final String countryIso = headers.get(MaxMindHeaders.X_COUNTRY_ISO);
        final String isp = headers.get(MaxMindHeaders.X_ISP);
        final String latitude = headers.get(MaxMindHeaders.X_LATITUDE);
        final String longitude = headers.get(MaxMindHeaders.X_LONGITUDE);
        final String accuracy = headers.get(MaxMindHeaders.X_LOCATION_ACCURACY);

        final String unknownValue = "UNKNOWN";

        final MaxMindInfo maxMindInfo = MaxMindInfo.builder()
                .anonymousIp(Strings.isNullOrEmpty(anonymousIp) ? false : Boolean.valueOf(anonymousIp))
                .anonymousVpn(Strings.isNullOrEmpty(anonymousVpn) ? false : Boolean.valueOf(anonymousVpn))
                .tor(Strings.isNullOrEmpty(tor) ? false : Boolean.valueOf(tor))
                .city(Strings.isNullOrEmpty(city) ? unknownValue : city)
                .state(Strings.isNullOrEmpty(state) ? unknownValue : state)
                .stateIso(Strings.isNullOrEmpty(state) ? unknownValue : stateIso)
                .country(Strings.isNullOrEmpty(country) ? unknownValue : country)
                .countryIso(Strings.isNullOrEmpty(country) ? unknownValue : countryIso)
                .postal(Strings.isNullOrEmpty(postal) ? unknownValue : postal)
                .connectionType(Strings.isNullOrEmpty(connectionType) ? unknownValue : connectionType)
                .userType(Strings.isNullOrEmpty(userType) ? unknownValue : userType)
                .isp(Strings.isNullOrEmpty(isp) ? unknownValue : isp)
                .latitude(Strings.isNullOrEmpty(latitude) ? 0 : Double.valueOf(latitude))
                .longitude(Strings.isNullOrEmpty(longitude) ? 0 : Double.valueOf(longitude))
                .accuracy(Strings.isNullOrEmpty(accuracy) ? 0 : Integer.valueOf(accuracy))
                .build();

        routingContext.put(MaxMindVertxBundle.MAX_MIND_CONTEXT_KEY, maxMindInfo);
    }

    private void addCountryInfo(final Country country, final RoutingContext routingContext) {
        if (!Strings.isNullOrEmpty(country.getName())) {
            routingContext.request().headers().add(MaxMindHeaders.X_COUNTRY, toAscii(country.getName()));
        }

        if (!Strings.isNullOrEmpty(country.getIsoCode())) {
            routingContext.request().headers().add(MaxMindHeaders.X_COUNTRY_ISO, country.getIsoCode());
        }
    }

    private void addStateInfo(final Subdivision subdivision, final RoutingContext routingContext) {
        if (!Strings.isNullOrEmpty(subdivision.getName())) {
            routingContext.request().headers().add(MaxMindHeaders.X_STATE, toAscii(subdivision.getName()));
        }

        if (!Strings.isNullOrEmpty(subdivision.getIsoCode())) {
            routingContext.request().headers().add(MaxMindHeaders.X_STATE_ISO, subdivision.getIsoCode());
        }
    }

    private void addCityInfo(final City city, final RoutingContext routingContext) {
        if (!Strings.isNullOrEmpty(city.getName())) {
            routingContext.request().headers().add(MaxMindHeaders.X_CITY, toAscii(city.getName()));
        }
    }

    private void addPostalInfo(final Postal postal, final RoutingContext routingContext) {
        if (!Strings.isNullOrEmpty(postal.getCode())) {
            routingContext.request().headers().add(MaxMindHeaders.X_POSTAL, postal.getCode());
        }
    }

    private void addLocationInfo(final Location location, final RoutingContext routingContext) {
        if (location.getLatitude() != null) {
            routingContext.request().headers().add(MaxMindHeaders.X_LATITUDE, String.valueOf(location.getLatitude()));
        }

        if (location.getLongitude() != null) {
            routingContext.request().headers().add(MaxMindHeaders.X_LONGITUDE, String.valueOf(location.getLongitude()));
        }

        if (location.getAccuracyRadius() != null) {
            routingContext.request().headers().add(MaxMindHeaders.X_LOCATION_ACCURACY, String.valueOf(location.getAccuracyRadius()));
        }
    }

    private void addTraitsInfo(final Traits traits, final RoutingContext routingContext) {
        if (!Strings.isNullOrEmpty(traits.getUserType())) {
            routingContext.request().headers().add(MaxMindHeaders.X_USER_TYPE, toAscii(traits.getUserType()));
        }

        if (!Strings.isNullOrEmpty(traits.getIsp())) {
            routingContext.request().headers().add(MaxMindHeaders.X_ISP, toAscii(traits.getIsp()));
        }

        if (traits.getConnectionType() != null) {
            routingContext.request().headers().add(MaxMindHeaders.X_CONNECTION_TYPE, toAscii(traits.getConnectionType().name()));
        }

        routingContext.request().headers().add(MaxMindHeaders.X_PROXY_LEGAL, String.valueOf(traits.isLegitimateProxy()));
    }

    private void anonymousInfo(final AnonymousIpResponse anonymousIpResponse, final RoutingContext routingContext) {
        routingContext.request().headers().add(MaxMindHeaders.X_ANONYMOUS_IP, String.valueOf(anonymousIpResponse.isAnonymous()));
        routingContext.request().headers().add(MaxMindHeaders.X_ANONYMOUS_VPN, String.valueOf(anonymousIpResponse.isAnonymousVpn()));
        routingContext.request().headers().add(MaxMindHeaders.X_TOR, String.valueOf(anonymousIpResponse.isTorExitNode()));
    }

    private String toAscii(final String input) {
        if(!Strings.isNullOrEmpty(input)) {
            return input.replaceAll("[^\\x20-\\x7e]", "");
        }
        return input;
    }
}
