package com.example.pantrypal;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pantrypal.domain.RecipeItem;

import java.util.List;

@Dao
public interface RecipeItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(RecipeItem item);

    @Update
    void update(RecipeItem item);

    @Query("SELECT * from RecipeItem_table ORDER By name Asc")
    LiveData<List<RecipeItem>> getAllRecipeItems();

    @Query("DELETE from RecipeItem_table")
    void deleteAll();
}
