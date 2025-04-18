package com.restaurant_management.Centralization.controller.rest;

import com.restaurant_management.Centralization.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class CreateOrder {
    private OrderStatus orderStatus;
    private List<CreateDishOrder> dishOrders = new ArrayList<>();
}
