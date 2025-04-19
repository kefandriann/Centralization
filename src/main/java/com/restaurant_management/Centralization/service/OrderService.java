package com.restaurant_management.Centralization.service;

import com.restaurant_management.Centralization.controller.rest.OrderRest;
import io.github.cdimascio.dotenv.Dotenv;
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
        Dotenv dotenv = Dotenv.load();
        String url = dotenv.get("SALES_POINT_IP")+"/orders/"+reference;

        ResponseEntity<OrderRest> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                OrderRest.class
        );

        return response.getBody();
    }
}
