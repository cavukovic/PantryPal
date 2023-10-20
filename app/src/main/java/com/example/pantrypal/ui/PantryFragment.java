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

import com.example.pantrypal.domain.viewmodel.PantryViewModel;
import com.example.pantrypal.R;
import com.example.pantrypal.databinding.FragmentFirstBinding;
import com.example.pantrypal.domain.FoodItem;

import java.util.ArrayList;

public class PantryFragment extends Fragment {

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
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity.getApplication()).create(PantryViewModel.class);

        // Initialize RecyclerView and adapter
        RecyclerView foodRecyclerView = view.findViewById(R.id.foodRecyclerView);
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Observe LiveData for changes and update the adapter
        viewModel.getAllFoodItemsFromVm().observe(getViewLifecycleOwner(), foodItems ->
        {
            FoodAdapter adapter;
            if (foodItems != null && !foodItems.isEmpty()) {
                adapter = new FoodAdapter((ArrayList<FoodItem>) foodItems);
            } else {
                adapter = new FoodAdapter(new ArrayList<>());
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
                //viewModel.deleteFoodItem(itemName);
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