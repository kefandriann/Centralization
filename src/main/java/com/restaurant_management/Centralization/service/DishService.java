package com.restaurant_management.Centralization.service;

import com.restaurant_management.Centralization.controller.rest.DishRest;
import com.restaurant_management.Centralization.model.DishOrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishService {
    private final RestTemplate restTemplate;

    public List<DishRest> fetchDishes(){
        String url = "http://:8080/dishes";

        ResponseEntity<List<DishRest>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<DishRest>>() {}
        );

        return response.getBody();
    }

    public List<DishOrderStatus> fetchDishOrderStatusesByDishId(Long dishId){
        String url = "http://:8080/dishOrderStatus/"+dishId;

        ResponseEntity<List<DishOrderStatus>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<DishOrderStatus>>() {}
        );

        return response.getBody();
    }
}
