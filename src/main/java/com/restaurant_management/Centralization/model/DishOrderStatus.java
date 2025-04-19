package com.restaurant_management.Centralization.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restaurant_management.Centralization.model.enums.Status;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class DishOrderStatus {
    private Long id;
    private Status status;
    private LocalDateTime statusDate;
    private DishOrder dishOrder;
}
