package com.restaurant_management.Centralization.controller.rest;

import com.restaurant_management.Centralization.model.enums.StockMovementType;
import com.restaurant_management.Centralization.model.enums.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CreateStockMovement {
    private StockMovementType movementType;
    private LocalDateTime moveDate;
    private Double quantity;
    private Unit unit;
}
