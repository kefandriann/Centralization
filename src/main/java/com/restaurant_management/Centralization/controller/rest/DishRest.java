package com.restaurant_management.Centralization.controller.rest;

import lombok.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Setter
public class DishRest {
    private Long id;
    private String name;
    private Double unitPrice;
    private Double avalaibleQuantity;
    private List<DishIngredientRest> ingredients = new ArrayList<>();
}