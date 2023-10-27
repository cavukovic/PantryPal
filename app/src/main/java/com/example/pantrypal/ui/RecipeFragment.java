package com.example.pantrypal.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pantrypal.R;
import com.example.pantrypal.domain.FoodItem;
import com.example.pantrypal.domain.viewmodel.RecipeViewModel;
import com.example.pantrypal.domain.viewmodel.PantryViewModel;
import com.example.pantrypal.databinding.FragmentSecondBinding;
import com.example.pantrypal.domain.RecipeItem;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecipeFragment extends Fragment {

    private FragmentSecondBinding binding;
    private RecipeViewModel recipeViewModel;
    private PantryViewModel pantryViewModel;

    private static final int INTERNET_PERMISSION_REQUEST_CODE = 1;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Log.d("PantryPalDebug", "Recipe Fragment was created");
        super.onViewCreated(view, savedInstanceState);
        //viewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        //viewModel = new ViewModelProvider((ViewModelStoreOwner) this, new ViewModelProvider.NewInstanceFactory()).get(RecipeViewModel.class);
        Activity activity = requireActivity();
        recipeViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity.getApplication()).create(RecipeViewModel.class);
        pantryViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity.getApplication()).create(PantryViewModel.class);


        // Initialize RecyclerView and adapter
        RecyclerView recipeRecyclerView = view.findViewById(R.id.recipeRecyclerView);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Observe LiveData for changes and update the adapter
        recipeViewModel.getAllRecipeItemsFromVm().observe(getViewLifecycleOwner(), recipeItems ->
        {
            RecipeAdapter adapter;
            if (recipeItems != null && !recipeItems.isEmpty()) {
                adapter = new RecipeAdapter((ArrayList<RecipeItem>) recipeItems);
            } else {
                adapter = new RecipeAdapter(new ArrayList<>());
            }
            recipeRecyclerView.setAdapter(adapter);
        });

        // Observe LiveData for changes and update the adapter
        pantryViewModel.getAllFoodItemsFromVm().observe(getViewLifecycleOwner(), foodItems -> {});

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // For now this allows the user to type in an ingredient
                // we will probably want to keep this button
                showAddRecipeItemDialog();
            }
        });
        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipeViewModel.deleteAllRecipeItems();
            }
        });

        binding.generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // generate recipes
                List<FoodItem> pantryData = pantryViewModel.getAllFoodItemsFromVm().getValue();

                if (pantryData != null) {
                    for (FoodItem foodItem : pantryData) {
                        Log.d("PantryPalDebug", "Food Item: " + foodItem.getName() + ", Quantity: " + foodItem.getQuantity() + ", Unit: " + foodItem.getUnit());
                    }
                } else {
                    Log.d("PantryPalDebug", "Pantry data is empty.");
                }

                generateRecipes();
            }
        });

    }

    private void generateRecipes() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted; you can make the network request.
        List<FoodItem> pantryData = pantryViewModel.getAllFoodItemsFromVm().getValue();

        if (pantryData != null && !pantryData.isEmpty()) {
            // Build a list of ingredients from pantryData
            List<String> ingredients = new ArrayList<>();
            for (FoodItem foodItem : pantryData) {
                ingredients.add(foodItem.getName());
            }

            // Base URL
            String baseUrl = "https://api.spoonacular.com/recipes/findByIngredients";
            // Ingredients parameter (comma-separated list)
            String ingredientsParam = String.join(",", ingredients);
            // Number of recipes to return
            int numberOfRecipes = 1;
            // Limit license, Whether the recipes should have an open license that allows display with proper attribution.
            boolean limitLicense = true;
            // Ranking (1 for maximize used ingredients, 2 for minimize missing ingredients)
            int ranking = 1;
            // Ignore common pantry items (water, flour, etc)
            boolean ignorePantry = true;

            String url = baseUrl + "?ingredients=" + ingredientsParam +
                    "&number=" + numberOfRecipes +
                    "&limitLicense=" + limitLicense +
                    "&ranking=" + ranking +
                    "&ignorePantry=" + ignorePantry+
                    "&apiKey=21d53be8528f40f09edfdbcc6747360a";

            // Make a network request to the Spoonacular API to get recipes
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
        } else {
            Log.d("PantryPalDebug", "Pantry data is empty.");
        }

        } else {
            Log.d("request", "perm not granted");
            // Permission is not granted; request it from the user.
            requestInternetPermission();
        }
    }

    private void requestInternetPermission() {
        Log.d("request", "in request");
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.INTERNET)) {
            // Can show an explanation to the user
        }
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.INTERNET}, INTERNET_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == INTERNET_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // The user granted the INTERNET permission.
                generateRecipes();
            } else {
                // User denied the INTERNET permission.
            }
        }
    }


    private void showRecipePopup(String recipeInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Generated Recipe");

        // Parse Json info

        //builder.setMessage(recipeInfo);
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void showAddRecipeItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View textEntryView = inflater.inflate(R.layout.dialog_addrecipeitem, null);
        builder.setView(textEntryView);
        builder.setTitle("Add Recipe Item");

        final EditText nameInput = textEntryView.findViewById(R.id.AFDNameEditText);
        final EditText imgUrlInput = textEntryView.findViewById(R.id.AFDImgUrlEditText);
        final EditText usedIngCountInput = textEntryView.findViewById(R.id.AFDUsedIngCountEditText);
        final EditText missedIngCountInput = textEntryView.findViewById(R.id.AFDMissedIngCountEditText);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameInput.getText().toString().trim();
                String imgUrl = imgUrlInput.getText().toString().trim();
                int usedIngCount = Integer.parseInt(usedIngCountInput.getText().toString().trim());
                int missedIngCount = Integer.parseInt(missedIngCountInput.getText().toString().trim());
                if (!name.isEmpty()) {
                    recipeViewModel.insertRecipeItem(new RecipeItem(name, imgUrl, usedIngCount, missedIngCount));
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

    private void showDeleteRecipeItemDialog(final String itemName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Delete Recipe Item");
        builder.setMessage("Are you sure you want to delete this item: " + itemName + "?");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //recipeViewModel.deleteRecipeItem(itemName);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}