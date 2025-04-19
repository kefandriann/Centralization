package com.restaurant_management.Centralization.model;

import com.restaurant_management.Centralization.model.enums.Unit;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Builder
public class DishIngredient {
    private Long id;
    private Unit unit;
    private Dish dish;
    private Ingredient ingredient;
    private Double ingredientQuantity;
}
