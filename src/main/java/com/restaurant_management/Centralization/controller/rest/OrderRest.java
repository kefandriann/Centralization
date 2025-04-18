package com.restaurant_management.Centralization.controller.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restaurant_management.Centralization.model.EntityStatus;
import com.restaurant_management.Centralization.model.OrderStatus;
import com.restaurant_management.Centralization.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@AllArgsConstructor
public class OrderRest {
    private Long reference;
    private List<DishOrderRest> dishOrders;
    @JsonIgnore
    private List<OrderStatus> orderStatus = new ArrayList<>();

    public EntityStatus getActualStatus() {
       OrderStatus defaultStatus = new OrderStatus();
        defaultStatus.setStatus(Status.CREATED);
        return orderStatus.stream().max(Comparator.comparing(EntityStatus::getStatusDate))
                .orElse(defaultStatus);
    }
}
