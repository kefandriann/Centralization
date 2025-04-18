package com.restaurant_management.Centralization.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Builder
public class DishIngredient {
    private Long id;
    private String name;
    private Double requiredQuantity;
    private Ingredient ingredient;
    private Dish dish;
}
