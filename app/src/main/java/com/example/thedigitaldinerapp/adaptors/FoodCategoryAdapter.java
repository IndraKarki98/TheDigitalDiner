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

public class FoodCategoryAdapter extends RecyclerView.Adapter<FoodCategoryAdapter.ViewHolder> {

    ArrayList<FoodCategory> foodCategoryArrayList;
    Context context;

    public FoodCategoryAdapter(ArrayList<FoodCategory> foodCategoryArrayList, Context context) {
        this.foodCategoryArrayList = foodCategoryArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.design_for_fragment_home, parent, false);
        FoodCategoryAdapter.ViewHolder viewHolder = new FoodCategoryAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodCategory foodCategory = foodCategoryArrayList.get(position);

        Glide.with(context).load(foodCategory.getImage()).into(holder.ivFoodIconHome);
        holder.ivFoodCategoryHome.setText(foodCategory.getCategory_name());
    }

    @Override
    public int getItemCount() {
        return foodCategoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivFoodIconHome;
        public TextView ivFoodCategoryHome;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFoodIconHome = itemView.findViewById(R.id.ivFoodIconHome);
            ivFoodCategoryHome = itemView.findViewById(R.id.ivFoodCategoryHome);
        }
    }
}
