package com.restaurant_management.Centralization.service;

import com.restaurant_management.Centralization.controller.rest.IngredientRest;
import io.github.cdimascio.dotenv.Dotenv;
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
        Dotenv dotenv = Dotenv.load();
        String url = dotenv.get("SALES_POINT_IP")+"/ingredients";

        ResponseEntity<List<IngredientRest>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<IngredientRest>>() {}
        );

        return response.getBody();
    }
}
