package com.practice.intershop.config;

import com.practice.intershop.api.ApiClient;
import com.practice.intershop.api.client.BalanceApi;
import com.practice.intershop.api.client.PaymentApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApiClientConfig {

    @Bean
    public ApiClient paymentApiClient(WebClient.Builder webClientBuilder) {
        return new ApiClient(webClientBuilder
                .baseUrl("http://payment-service:8081")
                .defaultHeader("Accept", "application/json")
                .build());
    }

    @Bean
    public PaymentApi paymentApi(ApiClient paymentApiClient) {
        return new PaymentApi(paymentApiClient);
    }

    @Bean
    public BalanceApi balanceApi(ApiClient paymentApiClient) {
        return new BalanceApi(paymentApiClient);
    }
}
