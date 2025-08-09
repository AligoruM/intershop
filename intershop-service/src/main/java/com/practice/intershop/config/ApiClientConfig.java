package com.practice.intershop.config;

import com.practice.intershop.api.ApiClient;
import com.practice.intershop.api.client.BalanceApi;
import com.practice.intershop.api.client.PaymentApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiClientConfig {

    @Value("${payment.service.base-url:http://payment-service:8081}")
    private String paymentServiceBaseUrl;

    @Bean
    public ApiClient paymentApiClient() {
        return new ApiClient()
                .setBasePath(paymentServiceBaseUrl)
                .addDefaultHeader("Accept", "application/json");
    }

    @Bean
    public PaymentApi paymentApi(@Qualifier("paymentApiClient") ApiClient paymentApiClient) {
        return new PaymentApi(paymentApiClient);
    }

    @Bean
    public BalanceApi balanceApi(@Qualifier("paymentApiClient") ApiClient paymentApiClient) {
        return new BalanceApi(paymentApiClient);
    }
}
