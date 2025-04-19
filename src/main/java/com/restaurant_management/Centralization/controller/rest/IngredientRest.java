package com.restaurant_management.Centralization.controller.rest;

import com.restaurant_management.Centralization.model.enums.StockMovementType;
import com.restaurant_management.Centralization.model.enums.Unit;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class IngredientRest {
    private Long id;
    private String name;
    private LocalDateTime latestModification = LocalDateTime.now();
    private Unit unit;
    private List<PriceRest> prices = new ArrayList<>();
    private List<StockMovementRest> stocksMovement = new ArrayList<>();

    public Double getActualPrice() {
        if(this.prices == null || this.prices.isEmpty()){
            return 0.0;
        }
        return prices.stream().max(Comparator.comparing(PriceRest::getBeginDate)).get().getAmount();
    }

    public Double getAvailableQuantity() {
        if(stocksMovement.isEmpty() || prices.isEmpty()){
            return 0.0;
        }
        Double totalStockIn = stocksMovement.stream()
                .filter(stockMovement -> stockMovement.getMovementType() == StockMovementType.IN)
                .map(StockMovementRest::getQuantity)
                .reduce(Double::sum)
                .orElse(0.0);

        Double totalStockOut = stocksMovement.stream()
                .filter(stockMovement -> stockMovement.getMovementType() == StockMovementType.OUT)
                .map(StockMovementRest::getQuantity)
                .reduce(Double::sum)
                .orElse(0.0);

        return totalStockIn - totalStockOut;
    }
}
