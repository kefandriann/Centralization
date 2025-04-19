package com.restaurant_management.Centralization.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restaurant_management.Centralization.model.enums.Status;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Getter
@Setter
@Data
public class DishOrderStatus extends EntityStatus{
    @JsonIgnore
    private DishOrder dishOrder;
    public DishOrderStatus(Long id, Status enumStatus, LocalDateTime dateTime, DishOrder dishOrder) {
        super(id, enumStatus, dateTime);
        this.dishOrder = dishOrder;
    }
}
