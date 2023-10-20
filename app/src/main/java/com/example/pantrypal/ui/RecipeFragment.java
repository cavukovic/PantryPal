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
import com.example.pantrypal.domain.viewmodel.RecipeViewModel;
import com.example.pantrypal.databinding.FragmentSecondBinding;
import com.example.pantrypal.domain.RecipeItem;

import java.util.ArrayList;

public class RecipeFragment extends Fragment {

    private FragmentSecondBinding binding;
    private RecipeViewModel viewModel;


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
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity.getApplication()).create(RecipeViewModel.class);

        // Initialize RecyclerView and adapter
        RecyclerView recipeRecyclerView = view.findViewById(R.id.recipeRecyclerView);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Observe LiveData for changes and update the adapter
        viewModel.getAllRecipeItemsFromVm().observe(getViewLifecycleOwner(), recipeItems ->
        {
            RecipeAdapter adapter;
            if (recipeItems != null && !recipeItems.isEmpty()) {
                adapter = new RecipeAdapter((ArrayList<RecipeItem>) recipeItems);
            } else {
                adapter = new RecipeAdapter(new ArrayList<>());
            }
            recipeRecyclerView.setAdapter(adapter);
        });

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
                viewModel.deleteAllRecipeItems();
            }
        });

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
                    viewModel.insertRecipeItem(new RecipeItem(name, imgUrl, usedIngCount, missedIngCount));
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
                //viewModel.deleteRecipeItem(itemName);
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