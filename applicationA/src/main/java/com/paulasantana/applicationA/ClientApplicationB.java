package com.paulasantana.applicationA;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.rmi.ConnectException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

@Component
public class ClientApplicationB {

    private static final Logger logger = LoggerFactory.getLogger(ClientApplicationB.class);

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

    public String fallback(ConnectException e) {
        return "PENDING";
    }

    public String fallback(WebClientResponseException e){
        logger.error(e.getResponseBodyAsString());
        throw new ClientApplicationBException(e);
    }


    @TimeLimiter(name = "applicationB", fallbackMethod = "fallbackTimeLimiter")
    public CompletableFuture<String> methodCallWithTimeLimiterExample() {
        return webClient.get()
                .uri(applicationBUrl)
                .retrieve()
                .bodyToMono(String.class)
                .toFuture();
    }

    public CompletableFuture<String> fallbackTimeLimiter(TimeoutException e){
        throw new ClientApplicationBException(e);
    }

    @CircuitBreaker(name = "applicationB", fallbackMethod = "fallbackCircuitBreaker")
    public String methodCallWithCircuitBreakerExample() {

        return webClient.get()
                .uri(applicationBUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String fallbackCircuitBreaker(CallNotPermittedException e) {
        logger.error(e.getMessage());
        return "AUTOMATIC-APPROVAL";
    }

    public String fallbackCircuitBreaker(WebClientResponseException e) {
        logger.error(e.getMessage());
        throw new ClientApplicationBException(e);
    }

}
