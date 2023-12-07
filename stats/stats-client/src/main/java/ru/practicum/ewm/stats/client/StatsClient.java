package ru.practicum.ewm.stats.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.EndpointHitInDto;
import ru.practicum.ewm.stats.dto.ViewStatsDto;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class StatsClient {
    protected final RestTemplate rest;

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, @NotNull RestTemplateBuilder builder) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public List<ViewStatsDto> getStats(String startDate,
                                       String endDate,
                                       List<String> uris,
                                       Boolean unique) {

        Map<String, Object> parameters = Map.of(
            "start", startDate,
            "end", endDate,
            "uris", String.join(",", uris),
            "unique", unique
        );

        try {
            return rest.exchange(
                    "/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                    HttpMethod.GET,
                    new HttpEntity<>(defaultHeaders()),
                    new ParameterizedTypeReference<List<ViewStatsDto>>(){},
                    parameters
            ).getBody();
        } catch (RestClientException ex) {
            log.error("Unable make GET /stats request to stats-svc, cause {}", ex.getMessage());
            return Collections.emptyList();
        }
    }

    public void addHit(EndpointHitInDto hitInDto) {
        try {
            rest.exchange(
                    "/hit",
                    HttpMethod.POST,
                    new HttpEntity<>(hitInDto, defaultHeaders()),
                    EndpointHitDto.class);
        } catch (RestClientException ex) {
            log.error("Unable make POST /hit request to stats-svc, cause {}", ex.getMessage());
        }
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
