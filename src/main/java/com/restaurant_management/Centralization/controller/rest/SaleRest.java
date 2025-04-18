package com.restaurant_management.Centralization.controller.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SaleRest {
    private Long dishIdentifier;
    private String dish;
    private int quantitySold;
}
