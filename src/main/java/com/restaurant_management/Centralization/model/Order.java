package com.restaurant_management.Centralization.model;

import com.restaurant_management.Centralization.model.enums.Status;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Builder
public class Order {
    private Long id;
    private LocalDateTime orderDate;
    private Long reference;

    private List<OrderStatus> orderStatus = new ArrayList<>();
    private List<DishOrder> dishOrders = new ArrayList<>();

    public Double getTotalAmount () {
        return dishOrders.stream().map(dishOrder -> dishOrder.getDish().getUnitPrice() * dishOrder.getDishQuantity())
                .reduce(0.0, Double::sum);
    }

    public void setDishOrders(List<DishOrder> dishOrders) {
        if(this.getActualStatus().getStatus() != Status.CREATED) {
            throw new RuntimeException("CAN'T ADD DISH ORDER BECAUSE STATUS IS NOT CREATED");
        }
        List<DishOrder> createdDishOrders = dishOrders.stream().filter(dishOrder -> dishOrder.getActualStatus().getStatus() == Status.CREATED).toList();
        createdDishOrders.forEach(this.dishOrders::remove);
        dishOrders.forEach(dishOrder -> dishOrder.setOrder(this));
        this.dishOrders.addAll(dishOrders);
    }

    public void addDishOrder(DishOrder dishOrder) {
        if(this.getActualStatus().getStatus() != Status.CREATED) {
            throw new RuntimeException("CAN'T ADD DISH ORDER BECAUSE STATUS IS NOT CREATED");
        }
        dishOrder.setOrder(this);
        this.dishOrders.add(dishOrder);
    }

    public void addAndUpdateStatus(OrderStatus newOrderStatus) {
        Status actualStatus = this.getActualStatus()!=null? this.getActualStatus().getStatus() : null;
        Status newStatus = newOrderStatus.getStatus();
        switch (actualStatus){
            case Status.CREATED -> {
                switch (newStatus) {
                    case Status.CONFIRMED -> this.orderStatus.add(newOrderStatus);
                    default -> throw new RuntimeException("CAN'T UPDATE BECAUSE ACTUAL STATUS IS: " + actualStatus);
                }
            }
            case Status.CONFIRMED -> {
                switch (newStatus) {
                    case Status.IN_PREPARATION -> this.orderStatus.add(newOrderStatus);
                    default -> throw new RuntimeException("CAN'T UPDATE ACTUAL STATUS IS: " + actualStatus);
                }
            }
            case Status.IN_PREPARATION -> {
                switch (newStatus) {
                    case Status.DONE-> this.orderStatus.add(newOrderStatus);
                    default -> throw new RuntimeException("CAN'T UPDATE ACTUAL STATUS IS: " + actualStatus);
                }
            }
            case Status.DONE -> {
                switch (newStatus) {
                    case Status.SERVED-> this.orderStatus.add(newOrderStatus);
                    default -> throw new RuntimeException("CAN'T UPDATE ACTUAL STATUS IS: " + actualStatus);
                }
            }
            case Status.SERVED ->{
                switch (newStatus) {
                    default -> throw new RuntimeException("CAN'T UPDATE ACTUAL STATUS IS: " + actualStatus);
                }
            }
            case null, default -> {
                switch (newStatus) {
                    case Status.CREATED -> this.orderStatus.add(newOrderStatus);
                    default -> throw new RuntimeException("CAN'T UPDATE ACTUAL STATUS IS: " + actualStatus);
                }
            }
        }
    }

    public OrderStatus getActualStatus() {
        OrderStatus defaultStatus = new OrderStatus();
        defaultStatus.setStatus(Status.CREATED);
        return orderStatus.stream().max(Comparator.comparing(OrderStatus::getStatusDate))
                .orElse(defaultStatus);
    }
}
