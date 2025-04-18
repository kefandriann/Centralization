package com.restaurant_management.Centralization.controller.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateDishOrder {
    private String dishName;
    private Integer dishQuantity;
}
