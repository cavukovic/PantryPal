package com.example.pantrypal;

import static com.example.pantrypal.PantryPalRoomDatabase.getDatabase;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.pantrypal.domain.FoodItem;

import java.util.List;

public class FoodItemRepository {
    PantryPalRoomDatabase pantryPalRoomDatabase;
    FoodItemDao foodItemDao;
    private final LiveData<List<FoodItem>> listFoodItem;

    public FoodItemRepository(Application application) {
        pantryPalRoomDatabase = getDatabase(application);
        foodItemDao = pantryPalRoomDatabase.foodItemDao();
        listFoodItem = foodItemDao.getAllFoodItems();
    }

    public void insertItem(FoodItem item) {
        PantryPalRoomDatabase.databaseWriteExecutor.execute(() -> foodItemDao.insert(item));
    }

    public LiveData<List<FoodItem>> getAllFoodItems() {
        return listFoodItem;
    }

    public void deleteAll(){
        PantryPalRoomDatabase.databaseWriteExecutor.execute(() -> foodItemDao.deleteAll());
    }
}
