package com.restaurant_management.Centralization.controller.rest.Siege;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BestSale {
    private LocalDateTime updatedAt;
    private List<SalesElement> sales = new ArrayList<>();
}
