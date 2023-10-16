package com.example.pantrypal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pantrypal.databinding.FragmentFirstBinding;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment implements FoodAdapter.OnItemClickListener{

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
        //viewModel = new ViewModelProvider(this).get(PantryViewModel.class);
        //viewModel = new ViewModelProvider((ViewModelStoreOwner) this, new ViewModelProvider.NewInstanceFactory()).get(PantryViewModel.class);
        Activity activity = requireActivity();
        viewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(PantryViewModel.class);
        viewModel.initialize(requireContext());

        // create db in assets dir src main assets
        // something run in bg to query
        // happens in vm
        // Create sample FoodItem instances
//        List<FoodItem> foodList = new ArrayList<>();
//        foodList.add(new FoodItem("Apples"));
//        foodList.add(new FoodItem("Bananas"));
//        foodList.add(new FoodItem("Oranges"));
//        foodList.add(new FoodItem("Flour"));
//        foodList.add(new FoodItem("Milk"));
//        foodList.add(new FoodItem("Eggs"));
//        foodList.add(new FoodItem("Cookies"));
//        foodList.add(new FoodItem("Cereal"));
//        foodList.add(new FoodItem("Ground Beef"));
//        foodList.add(new FoodItem("Baking Powder"));
//        foodList.add(new FoodItem("Coffee"));
//        foodList.add(new FoodItem("Butter"));
//        foodList.add(new FoodItem("Noodles"));
//        foodList.add(new FoodItem("Salt"));
//        foodList.add(new FoodItem("Pepper"));
//        foodList.add(new FoodItem("Garlic Powder"));
//        foodList.add(new FoodItem("Onion"));
//        foodList.add(new FoodItem("Watermelon"));
//        foodList.add(new FoodItem("Lettuce"));
//        foodList.add(new FoodItem("Carrot"));
//        foodList.add(new FoodItem("Cream Cheese"));
//        foodList.add(new FoodItem("Peanut Butter"));
//        foodList.add(new FoodItem("Jelly"));
//        foodList.add(new FoodItem("Bread"));


        // Initialize RecyclerView and adapter
        RecyclerView foodRecyclerView = view.findViewById(R.id.foodRecyclerView);
//        FoodAdapter adapter = new FoodAdapter(viewModel.getFoodItems());
        FoodAdapter adapter = new FoodAdapter(viewModel.getFoodItems(), this); // 'this' refers to the fragment
        foodRecyclerView.setAdapter(adapter);
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Observe LiveData for changes and update the adapter
        viewModel.getFoodItemsLiveData().observe(getViewLifecycleOwner(), new Observer<List<FoodItem>>() {
            @Override
            public void onChanged(List<FoodItem> foodItems) {
                adapter.setData(foodItems); // Create a method in your adapter to set the new data
            }
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
                // Handle adding ingredients
                viewModel.deleteAllFoodItems();
                adapter.notifyDataSetChanged(); // Refresh the RecyclerView
            }
        });

    }

    private void showAddFoodItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Food Item");

        final EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String itemName = input.getText().toString().trim();
                if (!itemName.isEmpty()) {
                    viewModel.addFoodItem(itemName);
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