package com.restaurant_management.Centralization;

import com.restaurant_management.Centralization.config.AppConfig;
import com.restaurant_management.Centralization.controller.rest.DishRest;
import com.restaurant_management.Centralization.model.DishOrderStatus;
import com.restaurant_management.Centralization.service.DishService;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        DishService dishService = new DishService(new RestTemplate());
        List<DishOrderStatus> dishOrderStatuses = dishService.fetchDishOrderStatusesByDishId(1L);
        System.out.println(dishOrderStatuses);
    }
}