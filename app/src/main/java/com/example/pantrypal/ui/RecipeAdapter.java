package com.example.pantrypal.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pantrypal.R;
import com.example.pantrypal.domain.FoodItem;
import com.example.pantrypal.domain.RecipeItem;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    ArrayList<RecipeItem> recipeArrayList;

    public interface OnItemClickListener {
        void onItemClick(String itemName);
    }

    private OnItemClickListener clickListener;

    public RecipeAdapter(ArrayList<RecipeItem> recipes, RecipeAdapter.OnItemClickListener clickListener) {
        this.recipeArrayList = recipes;
        this.clickListener = clickListener;
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
        RecipeItem recipeItem = recipeArrayList.get(position);
        holder.tvRecipeName.setText(recipeItem.getName());
        holder.tvRecipeName.setText(recipeItem.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(recipeItem.getName());
            }
        });
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

