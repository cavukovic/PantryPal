package com.example.pantrypal.domain;

import static com.example.pantrypal.domain.PantryPalRoomDatabase.getDatabase;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RecipeItemRepository {
    PantryPalRoomDatabase pantryPalRoomDatabase;
    RecipeItemDao recipeItemDao;
    private final LiveData<List<RecipeItem>> listRecipeItem;

    public RecipeItemRepository(Application application) {
        pantryPalRoomDatabase = getDatabase(application);
        recipeItemDao = pantryPalRoomDatabase.recipeItemDao();
        listRecipeItem = recipeItemDao.getAllRecipeItems();
    }

    public void insertItem(RecipeItem item) {
        PantryPalRoomDatabase.databaseWriteExecutor.execute(() -> recipeItemDao.insert(item));
    }

    public LiveData<List<RecipeItem>> getAllRecipeItems() {
        return listRecipeItem;
    }

    public void deleteAll(){
        PantryPalRoomDatabase.databaseWriteExecutor.execute(() -> recipeItemDao.deleteAll());
    }

    public void deleteRecipeItem(String itemName) {
        PantryPalRoomDatabase.databaseWriteExecutor.execute(() -> recipeItemDao.deleteRecipeItem(itemName));
    }
}
