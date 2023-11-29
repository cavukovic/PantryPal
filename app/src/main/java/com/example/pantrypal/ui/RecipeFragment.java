package com.example.pantrypal.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecipeFragment extends Fragment implements RecipeAdapter.OnItemClickListener{

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
                adapter = new RecipeAdapter((ArrayList<RecipeItem>) recipeItems, this);
            } else {
                adapter = new RecipeAdapter(new ArrayList<>(), this);
            }
            recipeRecyclerView.setAdapter(adapter);
        });

        // Observe LiveData for changes and update the adapter
        pantryViewModel.getAllFoodItemsFromVm().observe(getViewLifecycleOwner(), foodItems -> {});

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            List<FoodItem> pantryData = pantryViewModel.getAllFoodItemsFromVm().getValue();

            if (pantryData != null && !pantryData.isEmpty()) {
                List<String> ingredients = new ArrayList<>();
                for (FoodItem foodItem : pantryData) {
                    ingredients.add(foodItem.getName());
                }

                String baseUrl = "https://api.spoonacular.com/recipes/findByIngredients";
                String ingredientsParam = String.join(",", ingredients);
                int numberOfRecipes = 1;
                boolean limitLicense = true;
                int ranking = 1;
                boolean ignorePantry = true;

                String url = baseUrl + "?ingredients=" + ingredientsParam +
                        "&number=" + numberOfRecipes +
                        "&limitLicense=" + limitLicense +
                        "&ranking=" + ranking +
                        "&ignorePantry=" + ignorePantry +
                        "&apiKey=21d53be8528f40f09edfdbcc6747360a";

                new NetworkRequestAsyncTask().execute(url);
            } else {
                Log.d("PantryPalDebug", "Pantry data is empty.");
            }

        } else {
            Log.d("request", "perm not granted");
            requestInternetPermission();
        }
    }

    private class NetworkRequestAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            OkHttpClient client = new OkHttpClient();
            String url = urls[0];
            Log.d("URL", url);

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                } else {
                    Log.e("RecipeFragment", "API request failed with code: " + response.code());
                }
            } catch (IOException e) {
                Log.e("RecipeFragment", "Network request failed: " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            super.onPostExecute(jsonResponse);
            if (jsonResponse != null) {
                Log.d("RecipeFragment", "API response JSON: " + jsonResponse);
                showRecipePopup(jsonResponse);
            }
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

        // Parse Json info
        try {
            JSONArray recipes = new JSONArray(recipeInfo);

            if (recipes.length() > 0) {
                // Parse general recipe info
                JSONObject recipe = recipes.getJSONObject(0);
                // make title hyperlegible font
                String title = recipe.getString("title");
                builder.setTitle(title);
                String image = recipe.getString("image");
                int usedIngredientCount = recipe.getInt("usedIngredientCount");
                int missedIngredientCount = recipe.getInt("missedIngredientCount");

                // Parse and format the ingredient lists
                StringBuilder usedIngredientsList = new StringBuilder("Used Ingredients:\n");
                JSONArray usedIngredients = recipe.getJSONArray("usedIngredients");
                for (int i = 0; i < usedIngredients.length(); i++) {
                    JSONObject usedIngredient = usedIngredients.getJSONObject(i);
                    String original = usedIngredient.getString("original");
                    usedIngredientsList.append("• ").append(original).append("\n");
                }

                StringBuilder missedIngredientsList = new StringBuilder("Missed Ingredients:\n");
                JSONArray missedIngredients = recipe.getJSONArray("missedIngredients");
                for (int i = 0; i < missedIngredients.length(); i++) {
                    JSONObject missedIngredient = missedIngredients.getJSONObject(i);
                    String original = missedIngredient.getString("original");
                    missedIngredientsList.append("• ").append(original).append("\n");
                }

                View recipeView = getRecipeView(image, usedIngredientCount, missedIngredientCount, usedIngredientsList.toString(), missedIngredientsList.toString());
                builder.setView(recipeView);
                recipeViewModel.insertRecipeItem(new RecipeItem(title, image, usedIngredientCount, missedIngredientCount));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            builder.setMessage("Failed to parse recipe data");
        }

        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }


    private View getRecipeView(String imageUrl, int usedCount, int missedCount, String usedIngredients, String missedIngredients) {
        View recipeView = LayoutInflater.from(requireContext()).inflate(R.layout.recipe_popup_layout, null);

        // Find and set the UI elements
        ImageView imageView = recipeView.findViewById(R.id.imageView);
        TextView usedCountTextView = recipeView.findViewById(R.id.usedCountTextView);
        TextView missedCountTextView = recipeView.findViewById(R.id.missedCountTextView);
        TextView usedIngredientsTextView = recipeView.findViewById(R.id.usedIngredientsTextView);
        TextView missedIngredientsTextView = recipeView.findViewById(R.id.missedIngredientsTextView);

        // Load and display the image using Picasso
        Picasso.get().load(imageUrl).into(imageView);
        usedCountTextView.setText("Used Ingredients: " + usedCount);
        missedCountTextView.setText("Missed Ingredients: " + missedCount);

        // Set the ingredient lists
        usedIngredientsTextView.setText(usedIngredients);
        missedIngredientsTextView.setText(missedIngredients);

        return recipeView;
    }

    private void showAddRecipeItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View textEntryView = inflater.inflate(R.layout.dialog_addrecipeitem, null);
        builder.setView(textEntryView);

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
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View textView = inflater.inflate(R.layout.dialog_deleterecipeitem, null);
        builder.setView(textView);

        TextView message = textView.findViewById(R.id.DRImessage);
        message.append("Are you sure you want to delete this item: " + itemName + "?");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recipeViewModel.deleteRecipeItem(itemName);
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
    public void onItemClick(String itemName) {
        showDeleteRecipeItemDialog(itemName);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}