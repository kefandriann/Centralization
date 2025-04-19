package com.restaurant_management.Centralization.controller.rest.Siege;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@Setter
public class SalesElement {
    private String salesPoint;
    private String dish;
    private Integer quantitySold;
    private Double totalAmount;
}
