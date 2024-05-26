package com.kfc.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientMenu {
    private Long id;
    private Menu menu;
    private Ingredient ingredient;
    private double quantite;
}
