package com.example.thedigitaldinerapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.thedigitaldinerapp.models.Cart;
import com.example.thedigitaldinerapp.models.Food;
import com.example.thedigitaldinerapp.models.User;
import com.example.thedigitaldinerapp.utils.ErrorUtils;
import com.example.thedigitaldinerapp.utils.SharedPrefManager;
import com.khalti.checkout.helper.Config;
import com.khalti.checkout.helper.KhaltiCheckOut;
import com.khalti.checkout.helper.OnCheckOutListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SelectPaymentMethodActivity extends AppCompatActivity {

    private Bundle bundle;
    private User user;
    private Food food;

    private ArrayList<Cart> cartArrayList;

    private LinearLayout idKhaltiDigitalWallet, idCashOnDelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payment_method);

        idKhaltiDigitalWallet = findViewById(R.id.idKhaltiDigitalWallet);
        idCashOnDelivery = findViewById(R.id.idCashOnDelivery);

        bundle = getIntent().getExtras();
        food = bundle.containsKey("food") ? (Food) bundle.getSerializable("food") : null;

        if (food == null) {
            getCartItems();
        }

        cartArrayList = new ArrayList<>();

        user = SharedPrefManager.getInstance(SelectPaymentMethodActivity.this).getUser();

        idCashOnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proceed("Cash On Delivery");
            }
        });

        idKhaltiDigitalWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proceed("Khalti");
            }
        });
    }

    private void proceed(String paymentMethod) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SelectPaymentMethodActivity.this);
        builder.setTitle("Note");
        builder.setMessage("Once the payment is done, it will not be refunded. Are you sure you want to proceed?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (paymentMethod.equals("Khalti")) {
                    openKhalti();
                }
                else {
                    if (cartArrayList.isEmpty()) {
                        addSingleBooking("Cash On Delivery");
                    } else {
                        addMultipleOrders("Cash On Delivery");
                    }

                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void openKhalti() {
        long amount = Long.parseLong(bundle.getString("totalAmount"));
        openKhaltiApp(amount);
    }

    private void openKhaltiApp(long amount) {
        UUID uuid = UUID.randomUUID();
        amount *= 100;
        Config.Builder builder = new Config.Builder("test_public_key_d116bb904072477d9bcad17d18afbf6e", uuid.toString(), "Digital Diner",
                amount, new OnCheckOutListener() {

            @Override
            public void onSuccess(@NonNull Map<String, Object> data) {
                Log.i("success", data.toString());
                if (cartArrayList.isEmpty()) {
                    addSingleBooking("Khalti");
                }
                else {
                    addMultipleOrders("Khalti");
                }

            }

            @Override
            public void onError(@NonNull String action, @NonNull Map<String, String> errorMap) {
                Log.e("hello", errorMap.toString());
                Toast.makeText(SelectPaymentMethodActivity.this, "Khalti Error", Toast.LENGTH_SHORT).show();

            }

        });

        Config config = builder.build();

        KhaltiCheckOut khaltiCheckOut = new KhaltiCheckOut(SelectPaymentMethodActivity.this, config);
        khaltiCheckOut.show();
    }

    private void getCartItems() {
        String url = "https://s-t-k.000webhostapp.com/digitaldiner/get_cart_items.php";

        final RequestQueue requestQueue = Volley.newRequestQueue(SelectPaymentMethodActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("not_found")) {
                    Toast.makeText(SelectPaymentMethodActivity.this, "No any cart item found!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonResponse;

                        cartArrayList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonResponse = jsonArray.getJSONObject(i);
                            int cartId = jsonResponse.getInt("id");
                            int foodId = jsonResponse.getInt("food_id");
                            int quantity = jsonResponse.getInt("quantity");
                            int amount = jsonResponse.getInt("amount");
                            int perItemPrice = jsonResponse.getInt("price");
                            String foodCategoryName = jsonResponse.getString("category_name");
                            String foodName = jsonResponse.getString("food_name");
                            String image = jsonResponse.getString("image");

                            Cart cart = new Cart(cartId, foodId, amount, quantity, perItemPrice, foodName, foodCategoryName, image);
                            cartArrayList.add(cart);
                        }

                    } catch (JSONException e) {
                        Toast.makeText(SelectPaymentMethodActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SelectPaymentMethodActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
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

    private void addSingleBooking(String paymentMethod) {
        String url = "https://s-t-k.000webhostapp.com/digitaldiner/order.php";
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        final RequestQueue requestQueue = Volley.newRequestQueue(SelectPaymentMethodActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.trim().equals("success")) {
                    Toast.makeText(SelectPaymentMethodActivity.this, "Order Successful! Thank you for ordering.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SelectPaymentMethodActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (response.trim().equals("error")) {
                    Toast.makeText(SelectPaymentMethodActivity.this, "Error while inserting", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(SelectPaymentMethodActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("foodId", String.valueOf(food.getId()));
                params.put("userId", String.valueOf(user.getUserId()));
                params.put("totalQuantity", bundle.getString("totalQuantity"));
                params.put("totalAmount", bundle.getString("totalAmount"));
                params.put("paymentMethod", paymentMethod);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void addMultipleOrders(String paymentMethod) {
        for (Cart cart: cartArrayList) {
            String url = "https://s-t-k.000webhostapp.com/digitaldiner/order.php";
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            final RequestQueue requestQueue = Volley.newRequestQueue(SelectPaymentMethodActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    if (response.trim().equals("success")) {
                    } else if (response.trim().equals("error")) {
                        Toast.makeText(SelectPaymentMethodActivity.this, "Error while inserting", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SelectPaymentMethodActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(SelectPaymentMethodActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("foodId", String.valueOf(cart.getFoodId()));
                    params.put("userId", String.valueOf(user.getUserId()));
                    params.put("totalQuantity", String.valueOf(cart.getQuantity()));
                    params.put("totalAmount", String.valueOf(cart.getPrice()));
                    params.put("paymentMethod", paymentMethod);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
        Toast.makeText(SelectPaymentMethodActivity.this, "Order Successful! Thank you for ordering.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SelectPaymentMethodActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}