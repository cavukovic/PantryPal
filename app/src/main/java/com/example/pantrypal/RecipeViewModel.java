package com.example.pantrypal;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.pantrypal.domain.RecipeItem;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {
    private final RecipeItemRepository recipeItemRepository;
    private final LiveData<List<RecipeItem>> listLiveData;

    public RecipeViewModel(Application application) {
        super(application);
        recipeItemRepository = new RecipeItemRepository(application);
        listLiveData = recipeItemRepository.getAllRecipeItems();
    }

    public LiveData<List<RecipeItem>> getAllRecipeItemsFromVm() {
        return listLiveData;
    }

    public void insertRecipeItem(RecipeItem item) {
        recipeItemRepository.insertItem(item);
    }

    public void deleteAllRecipeItems(){
        recipeItemRepository.deleteAll();
    }
}

