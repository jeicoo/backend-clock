package com.example.clock.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;

import reactor.core.publisher.Mono;
import com.example.clock.TimeResponse;

@Service
public class TimezoneDBService {
    @Value("${ratelimit.message}")
	private String message;

    private final WebClient webClient;

    public TimezoneDBService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.timezonedb.com").build();
    }

    public Mono<String> getManilaTime(String apikey, String fromTimezone, String toTimezone, String time) {
        return this.webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/v2.1/convert-time-zone")
                .queryParam("key", apikey)
                .queryParam("format", "json")
                .queryParam("from", fromTimezone)
                .queryParam("to", toTimezone)
                .queryParam("time", time)
                .build())
            .exchangeToMono(response -> {
                if (response.statusCode().equals(HttpStatus.OK)) {
                    return response.bodyToMono(TimeResponse.class).map(TimeResponse::getToTimestamp);
                } else if (response.statusCode().is4xxClientError()) {
                    return Mono.just(message);
                } else {
                    return response.createException()
                        .flatMap(Mono::error);
                }
            });
    }

}