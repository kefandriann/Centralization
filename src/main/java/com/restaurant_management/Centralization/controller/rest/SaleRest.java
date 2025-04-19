package com.restaurant_management.Centralization.controller.rest;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class SaleRest {
    private Long dishIdentifier;
    private String dish;
    private Integer quantitySold;
}
