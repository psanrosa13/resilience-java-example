package com.paulasantana.applicationA;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.rmi.ConnectException;

@Component
public class ClientApplicationB {

    @Value("${clients.applicationB.url}")
    private String applicationBUrl;

    private final WebClient webClient;

    public ClientApplicationB(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Retry(name = "applicationB", fallbackMethod = "fallback")
    public String methodCallWithRetryExample() {
        return webClient.get()
                .uri(applicationBUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @TimeLimiter(name = "applicationB", fallbackMethod = "fallback")
    public String methodCallWithTimeLimiterExample() {
        return webClient.get()
                .uri(applicationBUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }



    @CircuitBreaker(name = "applicationB", fallbackMethod = "fallback")
    public String methodCallWithCircuitBreakerExample() {

        return webClient.get()
                .uri(applicationBUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String fallback(ConnectException e) {
        return "PENDING";
    }

    public String fallback(WebClientResponseException e){
        return "FAIL";
    }


}
