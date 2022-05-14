package com.example.thedigitaldinerapp.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.thedigitaldinerapp.R;
import com.example.thedigitaldinerapp.models.Food;
import com.example.thedigitaldinerapp.models.FoodCategory;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    ArrayList<Food> foodArrayList;
    Context context;

    public FoodAdapter(ArrayList<Food> foodArrayList, Context context) {
        this.foodArrayList = foodArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.design_for_foodname_activity, parent, false);
        FoodAdapter.ViewHolder viewHolder = new FoodAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food food = foodArrayList.get(position);

        Glide.with(context).load(food.getImage()).into(holder.ivFoodImageFoodNameActivity);
        holder.tvFoodNameFoodNameActivity.setText(food.getFoodName());
        holder.tvFoodPriceFoodNameActivity.setText(String.valueOf(food.getPrice()));
    }

    @Override
    public int getItemCount() {
        return foodArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivFoodImageFoodNameActivity;
        public TextView tvFoodNameFoodNameActivity, tvFoodPriceFoodNameActivity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFoodImageFoodNameActivity = itemView.findViewById(R.id.ivFoodImageFoodNameActivity);
            tvFoodNameFoodNameActivity = itemView.findViewById(R.id.tvFoodNameFoodNameActivity);
            tvFoodPriceFoodNameActivity = itemView.findViewById(R.id.tvFoodPriceFoodNameActivity);
        }
    }
}
