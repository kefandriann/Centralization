package com.restaurant_management.Centralization.controller.rest;

import com.restaurant_management.Centralization.model.enums.StockMovementType;
import com.restaurant_management.Centralization.model.enums.Unit;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Setter
@EqualsAndHashCode
@ToString
@Getter
public class StockMovementRest {
    private Long id;
    private StockMovementType movementType;
    private LocalDateTime moveDate;
    private Double quantity;
    private Unit unit;
}
