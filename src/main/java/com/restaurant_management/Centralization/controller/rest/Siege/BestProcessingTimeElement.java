package com.restaurant_management.Centralization.controller.rest.Siege;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BestProcessingTimeElement {
    private String salesPoint;
    private String dish;
    private Double preparationDuration;
    private DurationUnit durationUnit;
}
