package com.example.pantrypal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    private List<FoodItem> foodList = new ArrayList<>();

//    public FoodAdapter(List<FoodItem> foodList) {
//        this.foodList = foodList;
//    }
public interface OnItemClickListener {
    void onItemClick(String itemName);
}

    private OnItemClickListener clickListener;

    public FoodAdapter(List<FoodItem> foodList, OnItemClickListener clickListener) {
        this.foodList = foodList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodItem foodItem = foodList.get(position);
        holder.foodNameTextView.setText(foodItem.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(foodItem.getName());
            }
        });
        // Add any other bindings or event handlers here
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public void setData(List<FoodItem> foodList) {
        this.foodList = foodList;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView foodNameTextView;
        // Other views as needed

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodNameTextView = itemView.findViewById(R.id.foodNameTextView);
        }
    }
}

