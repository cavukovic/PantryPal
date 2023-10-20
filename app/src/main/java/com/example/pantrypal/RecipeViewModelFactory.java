package com.example.pantrypal;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class RecipeViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;
    public RecipeViewModelFactory(Application myApplication) {
        application = myApplication;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RecipeViewModel(application);
    }
}
