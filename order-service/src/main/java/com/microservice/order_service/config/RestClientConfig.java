package com.microservice.order_service.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.microservice.order_service.client.InventoryClient;

@Configuration
public class RestClientConfig {
    
    @Value("${inventory.url}")
    private String inventoryServiceUrl;

    @Bean
    public InventoryClient inventoryClient() {
        // 1. Create a modern settings container for timeouts
        ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.defaults()
                                                    .withConnectTimeout(Duration.ofSeconds(3))
                                                    .withReadTimeout(Duration.ofSeconds(3));

// 2. Pass the settings directly into the build method
        ClientHttpRequestFactory requestFactory = ClientHttpRequestFactoryBuilder.detect()
                                                    .build(settings);

        RestClient restClient = RestClient.builder()
                .baseUrl(inventoryServiceUrl)
                .requestFactory(requestFactory)
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        
        return httpServiceProxyFactory.createClient(InventoryClient.class);
    }

    
}
