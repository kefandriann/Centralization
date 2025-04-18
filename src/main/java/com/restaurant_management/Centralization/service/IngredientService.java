package com.restaurant_management.Centralization.service;

import com.restaurant_management.Centralization.controller.rest.IngredientRest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final RestTemplate restTemplate;

    public List<IngredientRest> fetchIngredients(){
        String url = "http://:8080/ingredients";

        ResponseEntity<List<IngredientRest>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<IngredientRest>>() {}
        );

        return response.getBody();
    }
}
