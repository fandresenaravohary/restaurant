package com.kfc.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Menu {
    private int id;
    private String name;
    private List<IngredientMenu> ingredients = new ArrayList<>();
}
