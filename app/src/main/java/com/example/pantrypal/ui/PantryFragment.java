package com.example.pantrypal.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pantrypal.domain.viewmodel.PantryViewModel;
import com.example.pantrypal.R;
import com.example.pantrypal.databinding.FragmentFirstBinding;
import com.example.pantrypal.domain.FoodItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class

PantryFragment extends Fragment implements FoodAdapter.OnItemClickListener{

    private FragmentFirstBinding binding;
    private PantryViewModel viewModel;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Log.d("PantryPalDebug", "Pantry Fragment was created");
        super.onViewCreated(view, savedInstanceState);
        Activity activity = requireActivity();
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity.getApplication()).create(PantryViewModel.class);

        // Initialize RecyclerView and adapter
        RecyclerView foodRecyclerView = view.findViewById(R.id.foodRecyclerView);
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Observe LiveData for changes and update the adapter
        viewModel.getAllFoodItemsFromVm().observe(getViewLifecycleOwner(), foodItems ->
        {
            FoodAdapter adapter;
            if (foodItems != null && !foodItems.isEmpty()) {
                adapter = new FoodAdapter((ArrayList<FoodItem>) foodItems, this);
            } else {
                adapter = new FoodAdapter(new ArrayList<>(), this);
            }
            foodRecyclerView.setAdapter(adapter);
        });

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // For now this allows the user to type in an ingredient
                // we will probably want to keep this button
                showAddFoodItemDialog();
            }
        });
        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.deleteAllFoodItems();
            }
        });

    }

    private void showAddFoodItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View textEntryView = inflater.inflate(R.layout.dialog_addfooditem, null);
        builder.setView(textEntryView);
        builder.setTitle("Add Food Item");

        final EditText nameInput = textEntryView.findViewById(R.id.AFDNameEditText);
        final EditText quantityInput = textEntryView.findViewById(R.id.AFDQuantityEditText);
        final EditText unitInput = textEntryView.findViewById(R.id.AFDUnitEditText);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameInput.getText().toString().trim();
                int quantity = Integer.parseInt(quantityInput.getText().toString().trim());
                String unit = unitInput.getText().toString().trim();
                if (!name.isEmpty()) {
                    viewModel.insertFoodItem(new FoodItem(name, quantity, unit));
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void showDeleteFoodItemDialog(final String itemName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Delete Food Item");
        builder.setMessage("Are you sure you want to delete this item: " + itemName + "?");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.deleteFoodItem(itemName);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void fetchFood(String Upc) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted; you can make the network request.
            //List<FoodItem> pantryData = pantryViewModel.getAllFoodItemsFromVm().getValue();

            /*
            if (pantryData != null && !pantryData.isEmpty()) {
                // Build a list of ingredients from pantryData
                List<String> ingredients = new ArrayList<>();
                for (FoodItem foodItem : pantryData) {
                    ingredients.add(foodItem.getName());
                }
             */

            // URL
            String Url = "https://api.upcdatabase.org/product/" + Upc;

            // Make a network request to the UPC Database API to get recipes
            OkHttpClient client = new OkHttpClient();

            Log.d("URL", url);

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    // Handle the network request failure
                    Log.e("RecipeFragment", "Network request failed: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {

                        String jsonResponse = response.body().string();
                        Log.d("RecipeFragment", "API response JSON: " + jsonResponse);

                        // Display the recipe information in a pop-up dialog
                        getActivity().runOnUiThread(() -> showRecipePopup(jsonResponse)); // Replace recipeInfo with the parsed recipe information
                    } else {
                        Log.e("RecipeFragment", "API request failed with code: " + response.code());
                    }
                }
            });
            /*
            } else {
                Log.d("PantryPalDebug", "Pantry data is empty.");
            }
*/
        } else {
            Log.d("request", "perm not granted");
            // Permission is not granted; request it from the user.
            requestInternetPermission();
        }
    }

    @Override
    public void onItemClick(String itemName) {
        showDeleteFoodItemDialog(itemName);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}