package com.restaurant_management.Centralization.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restaurant_management.Centralization.model.enums.StockMovementType;
import com.restaurant_management.Centralization.model.enums.Unit;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Builder
public class StockMovement {
    private Long id;
    @JsonIgnore
    private Ingredient ingredient;
    private StockMovementType movementType;
    private Double quantity;
    private Unit unit;
    private LocalDateTime moveDate;
}
