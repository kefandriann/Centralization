package com.restaurant_management.Centralization.service;

import com.restaurant_management.Centralization.controller.rest.OrderRest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final RestTemplate restTemplate;

    public OrderRest fetchOrder(String reference){
        String url = "http://:8080/orders/"+reference;

        ResponseEntity<OrderRest> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                OrderRest.class
        );

        return response.getBody();
    }
}
