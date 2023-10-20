package com.example.pantrypal.domain;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FoodItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(FoodItem item);

    @Update
    void update(FoodItem item);

    @Query("SELECT * from FoodItem_table ORDER By name Asc")
    LiveData<List<FoodItem>> getAllFoodItems();

    @Query("DELETE from FoodItem_table")
    void deleteAll();
}
