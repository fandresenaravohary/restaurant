package com.kfc.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    private Ingredient ingredient;
    private double quantity;
    private Instant lastUpdatedTime;
    private MovementType movementType;
}
