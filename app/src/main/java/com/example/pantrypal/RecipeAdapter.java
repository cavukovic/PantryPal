package com.example.pantrypal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pantrypal.domain.RecipeItem;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    ArrayList<RecipeItem> recipeArrayList;

    public RecipeAdapter(ArrayList<RecipeItem> recipes) {
        this.recipeArrayList = recipes;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.tvRecipeName.setText(recipeArrayList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return recipeArrayList.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView tvRecipeName;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            findViews();
        }

        private void findViews() {
            tvRecipeName = itemView.findViewById(R.id.recipeNameTextView);
        }
    }
}

