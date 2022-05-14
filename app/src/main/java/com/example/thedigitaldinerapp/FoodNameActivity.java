package com.example.thedigitaldinerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.thedigitaldinerapp.adaptors.FoodAdapter;
import com.example.thedigitaldinerapp.adaptors.RecyclerAdapterCart;
import com.example.thedigitaldinerapp.adaptors.RecyclerAdapterFoodNameActivity;
import com.example.thedigitaldinerapp.models.Food;
import com.example.thedigitaldinerapp.models.FoodCategory;
import com.example.thedigitaldinerapp.utils.ErrorUtils;
import com.example.thedigitaldinerapp.utils.ItemClickSupport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FoodNameActivity extends AppCompatActivity {

    private Bundle bundle;
    private FoodCategory foodCategory;

    private TextView tvFoodCategoryFoodNameActivity;

    private RecyclerView recyclerViewFoodNameActivity;
    private ArrayList<Food> foodArrayList;
    private FoodAdapter foodAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_name);

        bundle = getIntent().getExtras();
        foodCategory = (FoodCategory) bundle.getSerializable("foodCategory");

        tvFoodCategoryFoodNameActivity = findViewById(R.id.tvFoodCategoryFoodNameActivity);
        recyclerViewFoodNameActivity = (RecyclerView) findViewById(R.id.recyclerViewFoodNameActivity);

        tvFoodCategoryFoodNameActivity.setText(foodCategory.getCategory_name());

        foodArrayList = new ArrayList<>();
        foodAdapter = new FoodAdapter(foodArrayList, FoodNameActivity.this);

        getFoods();

    }

    private void getFoods() {
        String url = "https://s-t-k.000webhostapp.com/digitaldiner/get_foods.php";

        final RequestQueue requestQueue = Volley.newRequestQueue(FoodNameActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("not_found")) {
                    Toast.makeText(FoodNameActivity.this, "No any foods found!", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonResponse;

                        for (int i = 0; i < jsonArray.length(); i++){
                            jsonResponse = jsonArray.getJSONObject(i);
                            int foodId = jsonResponse.getInt("id");
                            int price = jsonResponse.getInt("price");
                            int foodCategoryId = jsonResponse.getInt("food_category");
                            String foodName = jsonResponse.getString("food_name");
                            String image = jsonResponse.getString("image");
                            String ingredients = jsonResponse.getString("ingredients");


                            Food food = new Food(foodId, price, foodCategoryId, foodName, ingredients, image);
                            foodArrayList.add(food);
                        }

                        recyclerViewFoodNameActivity.setLayoutManager(new GridLayoutManager(FoodNameActivity.this, 3));
                        recyclerViewFoodNameActivity.setAdapter(foodAdapter);
                        foodAdapter.notifyDataSetChanged();

                        ItemClickSupport.addTo(recyclerViewFoodNameActivity).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                            @Override
                            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                Food food = foodArrayList.get(position);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("food", food);
                                bundle.putString("foodCategory", foodCategory.getCategory_name());
                                Intent intent = new Intent(FoodNameActivity.this, FoodOrderingActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });


                    }

                    catch (JSONException e) {
                        Toast.makeText(FoodNameActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FoodNameActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("foodCategoryId", String.valueOf(foodCategory.getId()));
                return params;
            }

        };
        requestQueue.add(stringRequest);
    }

}