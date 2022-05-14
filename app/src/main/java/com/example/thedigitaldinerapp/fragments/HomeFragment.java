package com.example.thedigitaldinerapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.thedigitaldinerapp.FoodNameActivity;
import com.example.thedigitaldinerapp.R;
import com.example.thedigitaldinerapp.adaptors.FoodCategoryAdapter;
import com.example.thedigitaldinerapp.adaptors.RecyclerAdapterHome;
import com.example.thedigitaldinerapp.models.FoodCategory;
import com.example.thedigitaldinerapp.utils.ErrorUtils;
import com.example.thedigitaldinerapp.utils.ItemClickSupport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewHome;
    private ArrayList<FoodCategory> foodCategoryArrayList;
    private FoodCategoryAdapter foodCategoryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewHome = v.findViewById(R.id.recyclerViewHome);

        foodCategoryArrayList = new ArrayList<>();

        foodCategoryAdapter = new FoodCategoryAdapter(foodCategoryArrayList, getContext());

        getFoodCategories();
        
        return v;
    }

    private void getFoodCategories() {
        String url = "https://s-t-k.000webhostapp.com/digitaldiner/get_categories.php";

        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("not_found")) {
                    Toast.makeText(getContext(), "No any categories found!", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonResponse;

                        for (int i = 0; i < jsonArray.length(); i++){
                            jsonResponse = jsonArray.getJSONObject(i);
                            int foodCategoryId = jsonResponse.getInt("id");
                            String categoryName = jsonResponse.getString("category_name");
                            String image = jsonResponse.getString("image");

                            FoodCategory foodCategory = new FoodCategory(foodCategoryId, categoryName, image);
                            foodCategoryArrayList.add(foodCategory);
                        }

                        recyclerViewHome.setLayoutManager(new GridLayoutManager(getContext(), 3));
                        recyclerViewHome.setAdapter(foodCategoryAdapter);
                        foodCategoryAdapter.notifyDataSetChanged();

                        ItemClickSupport.addTo(recyclerViewHome).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                            @Override
                            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                FoodCategory foodCategory = foodCategoryArrayList.get(position);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("foodCategory", foodCategory);
                                Intent intent = new Intent(getContext(), FoodNameActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });


                    }

                    catch (JSONException e) {
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
            }
        }) {

        };
        requestQueue.add(stringRequest);
    }

}
