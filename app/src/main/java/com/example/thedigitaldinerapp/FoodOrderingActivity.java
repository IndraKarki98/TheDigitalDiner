package com.example.thedigitaldinerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.thedigitaldinerapp.models.Food;
import com.example.thedigitaldinerapp.models.User;
import com.example.thedigitaldinerapp.utils.ErrorUtils;
import com.example.thedigitaldinerapp.utils.SharedPrefManager;

import java.util.HashMap;
import java.util.Map;

public class FoodOrderingActivity extends AppCompatActivity {
    TextView _tvFoodQuantiyFoodOrderingActivity, tvFoodCategoryFoodOrderingActivity,
            tvFoodNameFoodOrderingActivity, tvTotalAmountFoodOrderingActivity,
            tvIngredientsFoodOrderingActivity, tvOriginalPriceFoodOrderingActivity;
    ImageView ivfoodsType_booking;
    int count = 0;
    Bundle bundle;
    User user;
    Food food;
    Button buttonAddToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_ordering);

        _tvFoodQuantiyFoodOrderingActivity = (TextView) findViewById(R.id.tvFoodQuantiyFoodOrderingActivity);
        tvFoodCategoryFoodOrderingActivity = (TextView) findViewById(R.id.tvFoodCategoryFoodOrderingActivity);
        tvFoodNameFoodOrderingActivity = (TextView) findViewById(R.id.tvFoodNameFoodOrderingActivity);
        tvTotalAmountFoodOrderingActivity = (TextView) findViewById(R.id.tvTotalAmountFoodOrderingActivity);
        tvIngredientsFoodOrderingActivity = (TextView) findViewById(R.id.tvIngredientsFoodOrderingActivity);
        tvOriginalPriceFoodOrderingActivity = (TextView) findViewById(R.id.tvOriginalPriceFoodOrderingActivity);
        ivfoodsType_booking = findViewById(R.id.ivfoodsType_booking);
        buttonAddToCart = findViewById(R.id.buttonAddToCart);

        bundle = getIntent().getExtras();
        food = (Food) bundle.getSerializable("food");

        user = SharedPrefManager.getInstance(FoodOrderingActivity.this).getUser();

        setData();

        buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart();
            }
        });
    }

    private void setData() {
        Glide.with(FoodOrderingActivity.this).load(food.getImage()).into(ivfoodsType_booking);
        tvFoodCategoryFoodOrderingActivity.setText(bundle.getString("foodCategory"));
        tvFoodNameFoodOrderingActivity.setText(food.getFoodName());
        tvIngredientsFoodOrderingActivity.setText(food.getIngredients());
        tvTotalAmountFoodOrderingActivity.setText("0");
        tvOriginalPriceFoodOrderingActivity.setText(String.valueOf(food.getPrice()));
    }

    public void goToSelectPayement(View view) {
        String totalAmount = tvTotalAmountFoodOrderingActivity.getText().toString();
        if (Integer.parseInt(totalAmount) == 0) {
            Toast.makeText(this, "Please order at least one.", Toast.LENGTH_SHORT).show();
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable("food", food);
            bundle.putString("totalQuantity", _tvFoodQuantiyFoodOrderingActivity.getText().toString());
            bundle.putString("totalAmount", tvTotalAmountFoodOrderingActivity.getText().toString());
            Intent intent = new Intent(FoodOrderingActivity.this, SelectPaymentMethodActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private void addToCart() {
        String totalAmount = tvTotalAmountFoodOrderingActivity.getText().toString();
        if (Integer.parseInt(totalAmount) == 0) {
            Toast.makeText(this, "Please order at least one.", Toast.LENGTH_SHORT).show();
        } else {
            uploadToCart();
        }
    }

    private void uploadToCart() {
        String url = "https://s-t-k.000webhostapp.com/digitaldiner/add_to_cart.php";
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        final RequestQueue requestQueue = Volley.newRequestQueue(FoodOrderingActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.trim().equals("success")) {
                    Toast.makeText(FoodOrderingActivity.this, "Successfully added to cart.", Toast.LENGTH_SHORT).show();
                } else if (response.trim().equals("error")) {
                    Toast.makeText(FoodOrderingActivity.this, "Error while inserting", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(FoodOrderingActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("foodId", String.valueOf(food.getId()));
                params.put("userId", String.valueOf(user.getUserId()));
                params.put("totalQuantity", _tvFoodQuantiyFoodOrderingActivity.getText().toString());
                params.put("totalAmount", tvTotalAmountFoodOrderingActivity.getText().toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    //handling onclick increment and decrement icon of food quantity
    public void increment(View view) {
        if (count > 10) {
            Toast.makeText(this, "Maximum order limit reached.", Toast.LENGTH_SHORT).show();
        }
        else {
            count++;
            _tvFoodQuantiyFoodOrderingActivity.setText("" + count);
            int totalPrice = food.getPrice() * count;
            tvTotalAmountFoodOrderingActivity.setText(String.valueOf(totalPrice));
        }
    }

    public void decrement(View view) {
        if (count <= 0) count = 0;
        else count--;
        _tvFoodQuantiyFoodOrderingActivity.setText("" + count);
        int totalPrice = food.getPrice() * count;
        tvTotalAmountFoodOrderingActivity.setText(String.valueOf(totalPrice));
    }

}