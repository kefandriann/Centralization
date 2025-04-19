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
public class OrderStatus extends EntityStatus {
    @JsonIgnore
    private Order order;
    public OrderStatus(Long id, Status enumStatus, LocalDateTime dateTime, Order order) {
        super(id, enumStatus, dateTime);
        this.order = order;
    }
}
