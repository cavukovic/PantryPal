package com.example.pantrypal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pantrypal.databinding.FragmentFirstBinding;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Create sample FoodItem instances
        List<FoodItem> foodList = new ArrayList<>();
        foodList.add(new FoodItem("Apples"));
        foodList.add(new FoodItem("Bananas"));
        foodList.add(new FoodItem("Oranges"));
        foodList.add(new FoodItem("Flour"));
        foodList.add(new FoodItem("Milk"));
        foodList.add(new FoodItem("Eggs"));
        foodList.add(new FoodItem("Cookies"));
        foodList.add(new FoodItem("Cereal"));
        foodList.add(new FoodItem("Ground Beef"));
        foodList.add(new FoodItem("Baking Powder"));
        foodList.add(new FoodItem("Coffee"));
        foodList.add(new FoodItem("Butter"));
        foodList.add(new FoodItem("Noodles"));
        foodList.add(new FoodItem("Salt"));
        foodList.add(new FoodItem("Pepper"));
        foodList.add(new FoodItem("Garlic Powder"));
        foodList.add(new FoodItem("Onion"));
        foodList.add(new FoodItem("Watermelon"));
        foodList.add(new FoodItem("Lettuce"));
        foodList.add(new FoodItem("Carrot"));
        foodList.add(new FoodItem("Cream Cheese"));
        foodList.add(new FoodItem("Peanut Butter"));
        foodList.add(new FoodItem("Jelly"));
        foodList.add(new FoodItem("Bread"));




        // Initialize RecyclerView and adapter
        RecyclerView foodRecyclerView = view.findViewById(R.id.foodRecyclerView);
        FoodAdapter adapter = new FoodAdapter(foodList);
        foodRecyclerView.setAdapter(adapter);
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}