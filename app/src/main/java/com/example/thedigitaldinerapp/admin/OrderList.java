package com.example.thedigitaldinerapp.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.thedigitaldinerapp.OrderHistoryActivity;
import com.example.thedigitaldinerapp.R;
import com.example.thedigitaldinerapp.adaptors.RecyclerAdapterOrderHistory;
import com.example.thedigitaldinerapp.adaptors.RecyclerAdapterOrderList;
import com.example.thedigitaldinerapp.models.OrderHistory;
import com.example.thedigitaldinerapp.models.User;
import com.example.thedigitaldinerapp.utils.ErrorUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class OrderList extends AppCompatActivity{

    Toolbar _tbOrderList;
    RecyclerView recyclerViewOrderList;
    ImageView _ivFilterDate;
    RecyclerAdapterOrderList adapterOrderList;

    ArrayList<OrderHistory> orderHistoryArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        _tbOrderList = (Toolbar)findViewById(R.id.tbOrderList);
        setSupportActionBar(_tbOrderList);

        recyclerViewOrderList = (RecyclerView) findViewById(R.id.recyclerViewOrderList);

        orderHistoryArrayList = new ArrayList<>();

        adapterOrderList = new RecyclerAdapterOrderList(OrderList.this, orderHistoryArrayList);

        getAllOrders();

        _ivFilterDate = (ImageView) findViewById(R.id.ivFilterDateAdmin);
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
                DatePickerDialog dialog = new DatePickerDialog(OrderList.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                // dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });

    }

    private void getAllOrders() {
        String url = "https://s-t-k.000webhostapp.com/digitaldiner/get_all_orders.php";

        final RequestQueue requestQueue = Volley.newRequestQueue(OrderList.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("not_found")) {
                    Toast.makeText(OrderList.this, "No any orders found!", Toast.LENGTH_SHORT).show();
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
                            String fullName = jsonResponse.getString("full_name");
                            String email = jsonResponse.getString("email");
                            String address = jsonResponse.getString("address");
                            String phone = jsonResponse.getString("phone");

                            OrderHistory orderHistory = new OrderHistory(dateTime, categoryName, foodName, totalQuantity, totalAmount, paymentMethod, fullName, email, address, phone);
                            orderHistoryArrayList.add(orderHistory);
                        }

                        recyclerViewOrderList.setLayoutManager(new LinearLayoutManager(OrderList.this));
                        recyclerViewOrderList.setAdapter(adapterOrderList);
                        adapterOrderList.notifyDataSetChanged();
                    }

                    catch (JSONException e) {
                        Toast.makeText(OrderList.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrderList.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
            }
        }) {

        };
        requestQueue.add(stringRequest);
    }

    private void filterOrderHistory(String filterDate) {
        String url = "https://s-t-k.000webhostapp.com/digitaldiner/filter_order_history_admin.php";

        final RequestQueue requestQueue = Volley.newRequestQueue(OrderList.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("not_found")) {
                    Toast.makeText(OrderList.this, "No Orders Found!", Toast.LENGTH_SHORT).show();
                    orderHistoryArrayList.clear();
                    adapterOrderList.notifyDataSetChanged();
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

                        recyclerViewOrderList.setLayoutManager(new LinearLayoutManager(OrderList.this));
                        recyclerViewOrderList.setAdapter(adapterOrderList);
                        adapterOrderList.notifyDataSetChanged();
                    }

                    catch (JSONException e) {
                        Toast.makeText(OrderList.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrderList.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("filterDate", filterDate);
                return params;
            }

        };
        requestQueue.add(stringRequest);
    }

}