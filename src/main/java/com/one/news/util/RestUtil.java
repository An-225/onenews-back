package com.one.news.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestUtil {
    private static final Logger log = LoggerFactory.getLogger(RestUtil.class);

    public static ResponseEntity<String> doRequest(String url, Map<String, Object> queryString,Map<String, String> headers, Object body, HttpMethod method, Integer timeout)
            throws JsonProcessingException {
        Objects.requireNonNull(url, "URL não pode ser nula");
        Objects.requireNonNull(method, "Método HTTP não pode ser nulo");
        RestTemplate restTemplate;
        restTemplate = new RestTemplate();

        queryString = Optional.ofNullable(queryString).orElse(new HashMap<>());
        headers = Optional.ofNullable(headers).orElse(new HashMap<>());

        final String ACCEPT_HEADER = "Accept";
        final String CONTENT_TYPE_HEADER = "Content-Type";
        final String JSON_CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(ACCEPT_HEADER, JSON_CONTENT_TYPE);
        httpHeaders.set(CONTENT_TYPE_HEADER, JSON_CONTENT_TYPE);
        headers.forEach(httpHeaders::set);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        queryString.forEach(builder::queryParam);

        log.info("REQUEST URL:: [{}] - {}", method, url);
        log.info("REQUEST QUERY STRING:: {}", queryString);
        log.info("REQUEST HEADERS:: {}", headers);

        HttpEntity<?> entity;
        if (body == null) {
            entity = new HttpEntity<>(httpHeaders);
        } else {
            entity = new HttpEntity<>(JsonUtil.toJson(body), httpHeaders);
        }

        try {
            final var exchange = restTemplate.exchange(
                    builder.toUriString(),
                    method,
                    entity,
                    String.class
            );

            log.info("RESPONSE STATUS CODE:: {}", exchange.getStatusCode());
            log.info("RESPONSE BODY:: {}", exchange.getBody());
            return exchange;
        } catch (RestClientException e) {
            // Lidar com exceções específicas aqui
            log.error("Erro durante a solicitação HTTP", e);
            throw new RuntimeException("Erro durante a solicitação HTTP", e);
        }
    }

}
