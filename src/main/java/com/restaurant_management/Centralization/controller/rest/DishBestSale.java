package com.restaurant_management.Centralization.controller.rest;

import com.restaurant_management.Centralization.model.Dish;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DishBestSale extends Dish {
    private String name;
    private int salesQuantity;
    private Double totalAmount;
}
