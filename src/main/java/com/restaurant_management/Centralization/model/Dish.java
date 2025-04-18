package com.restaurant_management.Centralization.model;

import lombok.*;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Builder
public class Dish {
    private Long id;
    private String name;
    private Double unitPrice = 0.0;
    private List<DishIngredient> ingredients = new ArrayList<>();

    public int getAvailableQuantity() {
        return ingredients.stream()
                .map(ingredient -> ingredient.getIngredient().getAvailableQuantity() / ingredient.getRequiredQuantity())
                .max(Comparator.naturalOrder())
                .orElse(0.0)
                .intValue();
    }

    public double getIngredientCost() {
        return ingredients.stream()
                .map(ingredient -> ingredient.getIngredient().getActualPrice())
                .reduce(0.0, Double::sum);
    }

    public double getGrossMargin() {
        return unitPrice - this.getIngredientCost();
    }

    public List<DishIngredient> addDishIngredient(List<DishIngredient> dishIngredients) {
        dishIngredients.forEach(dishIngredient -> dishIngredient.setDish(this));
        ingredients.addAll(dishIngredients);
        return dishIngredients;
    }
}