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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public ResponseEntity<Object> getBestSales(@RequestParam(name = "top", required = false)Integer value){
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
            if (value != null){
                List<SalesElement> saleRests = bestSales.getSales();
                List<SalesElement> firstSales = saleRests.subList(0, Math.min(value, saleRests.size()));
                bestSales.setSales(firstSales);
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
    public ResponseEntity<Object> getDishBestProcessingTimes(@PathVariable Long id,
                                                             @RequestParam(name = "top", required = false)Integer value,
                                                             @RequestParam(name = "durationUnit", required = false)DurationUnit durationUnit,
                                                             @RequestParam(name = "calculationMode", required = false)CalculationMode calculationMode){
        try {
            List<DishOrderStatus> dishOrderStatuses = dishService.fetchDishOrderStatusesByDishId(id);

            List<DishOrderStatus> filtered = new ArrayList<>(dishOrderStatuses.stream().filter(d -> d.getStatus() == Status.IN_PREPARATION || d.getStatus() == Status.DONE).toList());
            filtered.sort(Comparator.comparing(DishOrderStatus::getStatusDate));

            DishRest dishRest = dishService.fetchDishes().stream().filter(d -> d.getId().equals(id)).findFirst().get();

            List<BestProcessingTimeElement> bestProcessingTimeElements = new ArrayList<>();

            List<Duration> durations = new ArrayList<>();

            for (int i=0; i<filtered.size(); i = i+2){
                DishOrderStatus startDishOrderStatus = filtered.get(i);
                DishOrderStatus endDishOrderStatus = filtered.get(i+1);

                LocalDateTime start = startDishOrderStatus.getStatusDate();
                LocalDateTime end = endDishOrderStatus.getStatusDate();

                Duration duration = Duration.between(start, end);
                durations.add(duration);

                BestProcessingTimeElement newElement = new BestProcessingTimeElement();
                newElement.setSalesPoint("Sales_point_1");
                newElement.setDish(dishRest.getName());

                bestProcessingTimeElements.add(newElement);
            }

            /*DishOrderStatus startDishOrderStatus = dishOrderStatuses.stream().filter(d -> d.getStatus() == Status.IN_PREPARATION).findFirst().get();
            DishOrderStatus endDishOrderStatus = dishOrderStatuses.stream().filter(d -> d.getStatus() == Status.DONE).findFirst().get();

            LocalDateTime start = startDishOrderStatus.getStatusDate();
            LocalDateTime end = endDishOrderStatus.getStatusDate();

            Duration duration = Duration.between(start, end);*/

            if (durationUnit == null) {
                for (int i=0; i<bestProcessingTimeElements.size(); i++){
                    bestProcessingTimeElements.get(i).setPreparationDuration((double) durations.get(i).toMinutes());
                    bestProcessingTimeElements.get(i).setDurationUnit(DurationUnit.MINUTES);
                }
            } else {
                if (durationUnit.equals(DurationUnit.SECONDS)){
                    for (int i=0; i<bestProcessingTimeElements.size(); i++){
                        bestProcessingTimeElements.get(i).setPreparationDuration((double) durations.get(i).toSeconds());
                        bestProcessingTimeElements.get(i).setDurationUnit(DurationUnit.SECONDS);
                    }
                } else if (durationUnit.equals(DurationUnit.MINUTES)){
                    for (int i=0; i<bestProcessingTimeElements.size(); i++){
                        bestProcessingTimeElements.get(i).setPreparationDuration((double) durations.get(i).toMinutes());
                        bestProcessingTimeElements.get(i).setDurationUnit(DurationUnit.MINUTES);
                    }
                } else if (durationUnit.equals(DurationUnit.HOUR)){
                    for (int i=0; i<bestProcessingTimeElements.size(); i++){
                        bestProcessingTimeElements.get(i).setPreparationDuration((double) durations.get(i).toHours());
                        bestProcessingTimeElements.get(i).setDurationUnit(DurationUnit.HOUR);
                    }
                }
            }

            if (calculationMode == CalculationMode.MINIMUM){
                bestProcessingTimeElements.sort(Comparator.comparing(BestProcessingTimeElement::getPreparationDuration));
            } else if (calculationMode == CalculationMode.AVERAGE){
                Double sum = 0.0;
                for (BestProcessingTimeElement b : bestProcessingTimeElements){
                    sum += b.getPreparationDuration();
                }
                Double average = sum/ bestProcessingTimeElements.size();
                BestProcessingTimeElement yo = new BestProcessingTimeElement();
                yo.setSalesPoint("Sales_point_1");
                yo.setDish(dishRest.getName());
                yo.setPreparationDuration(average);
                yo.setDurationUnit(durationUnit);
                return ResponseEntity.ok().body(new BestProcessingTime(LocalDateTime.now(), List.of(yo)));
            } else if (calculationMode == CalculationMode.MAXIMUM){
                bestProcessingTimeElements.sort(Comparator.comparing(BestProcessingTimeElement::getPreparationDuration).reversed());
            }

            if (value == null){
                return ResponseEntity.ok().body(new BestProcessingTime(LocalDateTime.now(), bestProcessingTimeElements));
            } else {
                List<BestProcessingTimeElement> bestProcessingTimeElementList = bestProcessingTimeElements.subList(0, Math.min(value, bestProcessingTimeElements.size()));
                return ResponseEntity.ok().body(new BestProcessingTime(LocalDateTime.now(), bestProcessingTimeElementList));
            }
        } catch (com.example.demo.exception.ClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (com.example.demo.exception.NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (com.example.demo.exception.ServerException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
