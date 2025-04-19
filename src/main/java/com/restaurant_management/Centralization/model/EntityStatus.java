package com.restaurant_management.Centralization.model;

import com.restaurant_management.Centralization.model.enums.Status;
import lombok.*;


import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
public class EntityStatus {
    private Long id;
    private Status status;
    private LocalDateTime statusDate;
}
