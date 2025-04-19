package com.restaurant_management.Centralization.service;

import com.restaurant_management.Centralization.controller.rest.SaleRest;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleService {
    private final RestTemplate restTemplate;

    public List<SaleRest> fetchSales(){
        Dotenv dotenv = Dotenv.load();
        String url = dotenv.get("SALES_POINT_IP")+"/sales";

        ResponseEntity<List<SaleRest>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<SaleRest>>() {}
        );

        return response.getBody();
    }
}
