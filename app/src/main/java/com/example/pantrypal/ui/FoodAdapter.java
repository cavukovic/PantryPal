package com.example.pantrypal.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pantrypal.R;
import com.example.pantrypal.domain.FoodItem;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    ArrayList<FoodItem> foodArrayList;

    public interface OnItemClickListener {
        void onItemClick(String itemName);
    }

    private OnItemClickListener clickListener;

    public FoodAdapter(ArrayList<FoodItem> students, OnItemClickListener clickListener) {

        this.foodArrayList = students;
        this.clickListener = clickListener;
    }



    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.food_item, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        holder.tvFoodName.setText(foodArrayList.get(position).getName());
        FoodItem foodItem = foodArrayList.get(position);
        holder.tvFoodName.setText(foodItem.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(foodItem.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodArrayList.size();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView tvFoodName;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            findViews();
        }

        private void findViews() {
            tvFoodName = itemView.findViewById(R.id.foodNameTextView);
        }
    }
}

