package com.restaurant_management.Centralization.controller.rest.Siege;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class BestProcessingTimeElement {
    private String salesPoint;
    private String dish;
    private Double preparationDuration;
    private DurationUnit durationUnit;
}
