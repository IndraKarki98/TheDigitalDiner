package com.example.thedigitaldinerapp.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.thedigitaldinerapp.LoginActivity;
import com.example.thedigitaldinerapp.R;

public class AddingFoods extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_foods);
    }

    public void goToAddNewCategory(View view) {
        Intent intent = new Intent(AddingFoods.this, AddNewFoodCategory.class);
        startActivity(intent);
    }

    public void goToAddingNewFoodItem(View view) {
        Intent intent = new Intent(AddingFoods.this, AddNewFoodItem.class);
        startActivity(intent);
    }
}