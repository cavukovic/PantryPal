package com.example.pantrypal.domain;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "FoodItem_table")
public class FoodItem {

    //Should we change primary key to UPC?
    @NonNull
    @PrimaryKey
    private String name;

    private int quantity;

    private String unit;
    //Maybe should be enum?
    private String category;

    @Ignore
    public FoodItem(@NonNull String name) {
        this.name = name;
    }

    public FoodItem(@NonNull String name, int quantity, String unit){
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // can add other properties and methods later
}
