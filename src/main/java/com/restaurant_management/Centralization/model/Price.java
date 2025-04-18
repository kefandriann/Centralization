package com.restaurant_management.Centralization.model;

import lombok.*;


import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Builder
public class Price {
    private Long id;
    private Ingredient ingredient;
    private Double amount;
    private LocalDateTime beginDate;
}