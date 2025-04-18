package com.restaurant_management.Centralization.controller;

import com.restaurant_management.Centralization.controller.rest.DishRest;
import com.restaurant_management.Centralization.controller.rest.SaleRest;
import com.restaurant_management.Centralization.controller.rest.Siege.*;
import com.restaurant_management.Centralization.model.DishOrderStatus;
import com.restaurant_management.Centralization.model.enums.Status;
import com.restaurant_management.Centralization.service.DishService;
import com.restaurant_management.Centralization.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
public class DishRestController {
    @Autowired
    private final SaleService saleService;
    @Autowired
    private final DishService dishService;

    @GetMapping("/bestSales")
    public ResponseEntity<Object> getBestSales(){
        try {
            List<DishRest> dishes = dishService.fetchDishes();
            List<SaleRest> sales = saleService.fetchSales();
            sales.sort(Comparator.comparing(SaleRest::getQuantitySold).reversed());

            BestSale bestSales = new BestSale();
            bestSales.setUpdatedAt(LocalDateTime.now());

            for (SaleRest sale : sales) {
                SalesElement salesElement = new SalesElement();
                salesElement.setSalesPoint("sales_point_1");
                salesElement.setDish(sale.getDish());
                salesElement.setQuantitySold(sale.getQuantitySold());
                String dishName = sale.getDish();
                DishRest correspondingDish = dishes.stream().filter(dishRest -> Objects.equals(dishRest.getName(), dishName)).findFirst().get();
                salesElement.setTotalAmount(correspondingDish.getUnitPrice() * salesElement.getQuantitySold());

                bestSales.getSales().add(salesElement);
            }

            return ResponseEntity.ok().body(bestSales);
        } catch (com.example.demo.exception.ClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (com.example.demo.exception.NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (com.example.demo.exception.ServerException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/dishes/{id}/bestProcessingTime")
    public ResponseEntity<Object> getDishBestProcessingTimes(@PathVariable Long id){
        try {
            List<DishOrderStatus> dishOrderStatuses = dishService.fetchDishOrderStatusesByDishId(id);
            DishRest dishRest = dishService.fetchDishes().stream().filter(d -> d.getId().equals(id)).findFirst().get();

            BestProcessingTimeElement bestProcessingTimeElement = new BestProcessingTimeElement();
            bestProcessingTimeElement.setSalesPoint("sales_point_1");
            bestProcessingTimeElement.setDish(dishRest.getName());

            DishOrderStatus startDishOrderStatus = dishOrderStatuses.stream().filter(d -> d.getStatus() == Status.IN_PREPARATION).findFirst().get();
            DishOrderStatus endDishOrderStatus = dishOrderStatuses.stream().filter(d -> d.getStatus() == Status.DONE).findFirst().get();

            LocalDateTime start = startDishOrderStatus.getStatusDate();
            LocalDateTime end = endDishOrderStatus.getStatusDate();

            Duration duration = Duration.between(start, end);

            bestProcessingTimeElement.setPreparationDuration((double) duration.toMinutes());
            bestProcessingTimeElement.setDurationUnit(DurationUnit.MINUTES);

            return ResponseEntity.ok().body(new BestProcessingTime(LocalDateTime.now(), List.of(bestProcessingTimeElement)));
        } catch (com.example.demo.exception.ClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (com.example.demo.exception.NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (com.example.demo.exception.ServerException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
