package com.example.pantrypal.domain.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class PantryViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;
    public PantryViewModelFactory(Application myApplication) {
            application = myApplication;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PantryViewModel(application);
    }
}
