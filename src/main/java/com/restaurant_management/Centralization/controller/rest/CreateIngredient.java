package com.restaurant_management.Centralization.controller.rest;

import com.restaurant_management.Centralization.model.enums.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateIngredient {
    private String name;
    private Unit unit;
    private Double requiredQuantity;
}
