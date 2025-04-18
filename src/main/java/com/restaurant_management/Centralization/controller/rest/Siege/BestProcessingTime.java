package com.restaurant_management.Centralization.controller.rest.Siege;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BestProcessingTime {
    private LocalDateTime updatedAt;
    private List<BestProcessingTimeElement> bestProcessingTimes;
}
