package com.example.pantrypal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class PantryViewModel extends ViewModel {
    private SQLiteDatabase database;

    private MutableLiveData<List<FoodItem>> foodItemsLiveData = new MutableLiveData<>();

    public LiveData<List<FoodItem>> getFoodItemsLiveData() {
        return foodItemsLiveData;
    }
    public PantryViewModel() {
        // Initialize your ViewModel with default values if needed.
    }

    public void initialize(Context context) {
        PantryDatabaseHelper dbHelper = new PantryDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public List<FoodItem> getFoodItems() {
        List<FoodItem> foodList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM FoodItems", null);

        while (cursor.moveToNext()) {
            int nameColumnIndex = cursor.getColumnIndex("name");

            if (nameColumnIndex >= 0) {
                String name = cursor.getString(nameColumnIndex);
                foodList.add(new FoodItem(name));
            } else {
                Log.e("PantryPalError", "There was an error getting the food items");
            }
        }

        cursor.close();
        return foodList;
    }

    public void addFoodItem(String name) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        database.insert("FoodItems", null, values);
        foodItemsLiveData.setValue(getFoodItems());
    }

    public void deleteFoodItem(String name) {
        database.delete("FoodItems", "name = ?", new String[]{name});
        foodItemsLiveData.setValue(getFoodItems());
    }

    public void deleteAllFoodItems() {
        // Execute an SQL DELETE statement to remove all records from the "FoodItems" table
        foodItemsLiveData.setValue(new ArrayList<>());
        database.execSQL("DELETE FROM FoodItems");
    }
}

