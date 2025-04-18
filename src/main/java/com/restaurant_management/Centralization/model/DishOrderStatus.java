package com.restaurant_management.Centralization.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Data
public class DishOrderStatus extends EntityStatus{
    @JsonIgnore
    private DishOrder dishOrder;
}
