package com.example.pantrypal;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.pantrypal.domain.FoodItem;
import com.example.pantrypal.domain.RecipeItem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {FoodItem.class, RecipeItem.class}, version = 1, exportSchema = false)
public abstract class PantryPalRoomDatabase extends RoomDatabase {
    public abstract FoodItemDao foodItemDao();
    public abstract RecipeItemDao recipeItemDao();
    //Add new DAOs here
    private static volatile PantryPalRoomDatabase pantryPalRoomDatabase;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static PantryPalRoomDatabase getDatabase(final Context context) {
        if (pantryPalRoomDatabase == null) {
            synchronized (PantryPalRoomDatabase.class) {
                if (pantryPalRoomDatabase == null) {
                    pantryPalRoomDatabase = Room.databaseBuilder(context.getApplicationContext(),
                                    PantryPalRoomDatabase.class, "PantryPal.db").fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return pantryPalRoomDatabase;
    }
}
