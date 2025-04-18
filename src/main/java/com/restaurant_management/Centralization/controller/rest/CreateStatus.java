package com.restaurant_management.Centralization.controller.rest;

import com.restaurant_management.Centralization.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateStatus {
    private Status status;
}
