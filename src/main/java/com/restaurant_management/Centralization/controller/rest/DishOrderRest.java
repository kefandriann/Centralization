package com.restaurant_management.Centralization.controller.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restaurant_management.Centralization.model.DishOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@AllArgsConstructor
public class DishOrderRest {
    private String name;
    private Double unitPrice;
    private int dishQuantity;
    @JsonIgnore
    private List<DishOrderStatus> dishOrderStatus = new ArrayList<>();

    public DishOrderStatus getActualStatus() {
        return dishOrderStatus.stream().max(Comparator.comparing(DishOrderStatus::getStatusDate))
                .orElse(null);
    }
}
