package com.example.pantrypal;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pantrypal.databinding.FragmentSecondBinding;

import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

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

        List<RecipeItem> recipeList = new ArrayList<>();
        recipeList.add(new RecipeItem("Chicken and Waffles", "This is a lovely brunch option"));
        recipeList.add(new RecipeItem("Beef Stew", "Perfect for a cold winter day"));
        recipeList.add(new RecipeItem("Chocolate Cake", "1 hour chocolate cake recipe, serves 10"));

        RecyclerView recipeRecyclerView = view.findViewById(R.id.recipeRecyclerView);
        RecipeAdapter adapter = new RecipeAdapter(recipeList);
        recipeRecyclerView.setAdapter(adapter);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Button commented out for now but we might want it
//        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(SecondFragment.this)
//                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}