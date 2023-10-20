package com.example.pantrypal.domain.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.pantrypal.domain.FoodItemRepository;
import com.example.pantrypal.domain.FoodItem;

import java.util.List;

public class PantryViewModel extends AndroidViewModel {
    private final FoodItemRepository foodItemRepository;
    private final LiveData<List<FoodItem>> listLiveData;

    public PantryViewModel(Application application) {
        super(application);
        foodItemRepository = new FoodItemRepository(application);
        listLiveData = foodItemRepository.getAllFoodItems();
    }

    public LiveData<List<FoodItem>> getAllFoodItemsFromVm() {
        return listLiveData;
    }

    public void insertFoodItem(FoodItem item) {
        foodItemRepository.insertItem(item);
    }

    public void deleteAllFoodItems(){
        foodItemRepository.deleteAll();
    }
}

