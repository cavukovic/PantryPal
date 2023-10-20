package com.example.pantrypal.domain;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "RecipeItem_table")
public class RecipeItem {
    @PrimaryKey(autoGenerate = true)
    private int recipeId;
    @NonNull
    private String name;
    private String img;
    private int usedIngCount;
    private int missedIngCount;


    public RecipeItem(@NonNull String name, String img, int usedIngCount, int missedIngCount) {
        this.name = name;
        this.img = img;
        this.usedIngCount = usedIngCount;
        this.missedIngCount = missedIngCount;
    }
    @NonNull
    public String getName() {
        return name;
    }

    public String getImg() {
        return img;
    }

    public int getUsedIngCount() { return usedIngCount; }
    public int getMissedIngCount() { return missedIngCount; }

    public int getRecipeId() {
        return recipeId;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setUsedIngCount(int usedIngCount) {
        this.usedIngCount = usedIngCount;
    }

    public void setMissedIngCount(int missedIngCount) {
        this.missedIngCount = missedIngCount;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
}
