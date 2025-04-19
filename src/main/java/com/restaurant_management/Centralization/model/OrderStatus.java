package com.restaurant_management.Centralization.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restaurant_management.Centralization.model.enums.Status;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Data
public class OrderStatus {
    private Long id;
    private Status status;
    private LocalDateTime statusDate;
    private Order order;
}
