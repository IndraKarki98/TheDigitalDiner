package com.example.thedigitaldinerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
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
import com.example.thedigitaldinerapp.adaptors.RecyclerAdapterOrderHistory;
import com.example.thedigitaldinerapp.models.Food;
import com.example.thedigitaldinerapp.models.OrderHistory;
import com.example.thedigitaldinerapp.models.User;
import com.example.thedigitaldinerapp.utils.ErrorUtils;
import com.example.thedigitaldinerapp.utils.ItemClickSupport;
import com.example.thedigitaldinerapp.utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class OrderHistoryActivity extends AppCompatActivity{

    Toolbar _tbOrderHistory;
    ImageView _ivFilterDate;
    RecyclerView recyclerViewOrderHistory;
    RecyclerAdapterOrderHistory adapterOrderHistory;


    User user;
    ArrayList<OrderHistory> orderHistoryArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);


        _tbOrderHistory = (Toolbar)findViewById(R.id.tbOrderHistory);
        setSupportActionBar(_tbOrderHistory);

        recyclerViewOrderHistory = (RecyclerView) findViewById(R.id.recyclerViewOrderHistory);

        user = SharedPrefManager.getInstance(OrderHistoryActivity.this).getUser();

        orderHistoryArrayList = new ArrayList<>();
        adapterOrderHistory = new RecyclerAdapterOrderHistory(OrderHistoryActivity.this, orderHistoryArrayList);

        getOrderHistory();

        _ivFilterDate = (ImageView) findViewById(R.id.ivFilterDate);
        _ivFilterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String currentDate = simpleDateFormat.format(calendar.getTime());
                        //Toast.makeText(OrderHistoryActivity.this, currentDate, Toast.LENGTH_SHORT).show();
                        filterOrderHistory(currentDate);

                    }
                };
                DatePickerDialog dialog = new DatePickerDialog(OrderHistoryActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
               // dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });

    }

    private void getOrderHistory() {
        String url = "https://s-t-k.000webhostapp.com/digitaldiner/get_order_history.php";

        final RequestQueue requestQueue = Volley.newRequestQueue(OrderHistoryActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("not_found")) {
                    Toast.makeText(OrderHistoryActivity.this, "You haven't ordered any foods yet!", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonResponse;

                        for (int i = 0; i < jsonArray.length(); i++){
                            jsonResponse = jsonArray.getJSONObject(i);
                            String foodName = jsonResponse.getString("food_name");
                            String categoryName = jsonResponse.getString("category_name");
                            String dateTime = jsonResponse.getString("ordered_date");
                            String paymentMethod = jsonResponse.getString("payment_method");
                            String totalQuantity = String.valueOf(jsonResponse.getInt("total_quantity"));
                            String totalAmount = String.valueOf(jsonResponse.getInt("total_amount"));

                            OrderHistory orderHistory = new OrderHistory(dateTime, categoryName, foodName, totalQuantity, totalAmount, paymentMethod, null, null, null, null);
                            orderHistoryArrayList.add(orderHistory);
                        }

                        recyclerViewOrderHistory.setLayoutManager(new LinearLayoutManager(OrderHistoryActivity.this));
                        recyclerViewOrderHistory.setAdapter(adapterOrderHistory);
                        adapterOrderHistory.notifyDataSetChanged();
                    }

                    catch (JSONException e) {
                        Toast.makeText(OrderHistoryActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrderHistoryActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userId", String.valueOf(user.getUserId()));
                return params;
            }

        };
        requestQueue.add(stringRequest);
    }


    private void filterOrderHistory(String filterDate) {
        String url = "https://s-t-k.000webhostapp.com/digitaldiner/filter_order_history.php";

        final RequestQueue requestQueue = Volley.newRequestQueue(OrderHistoryActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("not_found")) {
                    Toast.makeText(OrderHistoryActivity.this, "No Orders Found!", Toast.LENGTH_SHORT).show();
                    orderHistoryArrayList.clear();
                    adapterOrderHistory.notifyDataSetChanged();
                }
                else{
                    orderHistoryArrayList.clear();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonResponse;

                        for (int i = 0; i < jsonArray.length(); i++){
                            jsonResponse = jsonArray.getJSONObject(i);
                            String foodName = jsonResponse.getString("food_name");
                            String categoryName = jsonResponse.getString("category_name");
                            String dateTime = jsonResponse.getString("ordered_date");
                            String paymentMethod = jsonResponse.getString("payment_method");
                            String totalQuantity = String.valueOf(jsonResponse.getInt("total_quantity"));
                            String totalAmount = String.valueOf(jsonResponse.getInt("total_amount"));

                            OrderHistory orderHistory = new OrderHistory(dateTime, categoryName, foodName, totalQuantity, totalAmount, paymentMethod, null, null, null, null);
                            orderHistoryArrayList.add(orderHistory);
                        }

                        recyclerViewOrderHistory.setLayoutManager(new LinearLayoutManager(OrderHistoryActivity.this));
                        recyclerViewOrderHistory.setAdapter(adapterOrderHistory);
                        adapterOrderHistory.notifyDataSetChanged();
                    }

                    catch (JSONException e) {
                        Toast.makeText(OrderHistoryActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrderHistoryActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userId", String.valueOf(user.getUserId()));
                params.put("filterDate", filterDate);
                return params;
            }

        };
        requestQueue.add(stringRequest);
    }
}