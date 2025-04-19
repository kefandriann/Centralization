package com.restaurant_management.Centralization.controller.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restaurant_management.Centralization.model.Price;
import com.restaurant_management.Centralization.model.StockMovement;
import com.restaurant_management.Centralization.model.enums.StockMovementType;
import lombok.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@EqualsAndHashCode
public class DishIngredientRest {
    private Long id;
    private String name;
    private Double requiredQuantity;
    private Double actualPrice;
    private Double avalaibleQuantity;
    private StockMovementRest actualStockMovement;
}
