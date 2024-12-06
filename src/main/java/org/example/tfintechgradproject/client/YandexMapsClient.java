package org.example.tfintechgradproject.client;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tfintechgradproject.dto.response.YandexMapsLocationResponse;
import org.example.tfintechgradproject.exception.exceptions.ExternalServiceUnavailableException;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class YandexMapsClient {

    @Value("${yandexMapsApi.apiKey}")
    private String apiKey;

    private final RestClient restClient;
    private final WKTReader wktReader;


    public YandexMapsLocationResponse getLocationInfo(String location) {
        log.info("Fetching location info for: {}", location);

        if (location.matches("^[-+]?\\d+(\\.\\d+)?\\s+[-+]?\\d+(\\.\\d+)?$")) {
            log.debug("Detected coordinates format for location: {}", location);

            return deserialize(acquireLocationInfo(location), location);
        } else {
            log.debug("Detected address format for location: {}", location);

            return deserialize(acquireLocationInfo(location.replaceAll(" ", "+")), null);
        }
    }

    @RateLimiter(name = "yandexMapsApi", fallbackMethod = "rateLimiterFallback")
    @CircuitBreaker(name = "yandexMapsApi", fallbackMethod = "circuitBreakerFallback")
    private JsonNode acquireLocationInfo(String geocode) {
        log.info("Requesting Yandex Maps API for geocode: {}", geocode);

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("apikey", apiKey)
                        .queryParam("geocode", geocode)
                        .queryParam("results", "1")
                        .queryParam("format", "json")
                        .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(JsonNode.class);
    }

    public YandexMapsLocationResponse deserialize(JsonNode node, String coordinates) {
        log.debug("Deserializing response for coordinates: {}", coordinates);

        node = node.at("/response/GeoObjectCollection/featureMember").get(0).path("GeoObject");

        var response = new YandexMapsLocationResponse();
        response.setAddress(node.at("/metaDataProperty/GeocoderMetaData/Address/formatted").textValue());
        if (coordinates == null) {
            coordinates = node.at("/Point/pos").textValue();
        }
        try {
            response.setCoordinates((Point) wktReader.read("POINT (%s)".formatted(coordinates)));
        } catch (ParseException e) {
            log.error("Failed to parse coordinates: {}", coordinates, e);
            throw new RuntimeException(e);
        }
        log.debug("Successfully deserialized response: {}", response);

        return response;
    }

    private YandexMapsLocationResponse rateLimiterFallback(Exception e) {
        log.warn("Rate limiter fallback triggered", e);
        throw new ExternalServiceUnavailableException("Too many requests");
    }

    private YandexMapsLocationResponse circuitBreakerFallback(Exception e) {
        log.warn("Circuit breaker fallback triggered", e);
        throw new ExternalServiceUnavailableException("Service is unavailable");
    }
}
