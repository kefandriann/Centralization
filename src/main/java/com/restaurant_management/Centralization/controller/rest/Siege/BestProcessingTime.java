package com.restaurant_management.Centralization.controller.rest.Siege;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@Setter
public class BestProcessingTime {
    private LocalDateTime updatedAt;
    private List<BestProcessingTimeElement> bestProcessingTimes;
}
