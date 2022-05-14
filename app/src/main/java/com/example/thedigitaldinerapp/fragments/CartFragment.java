package com.example.thedigitaldinerapp.fragments;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.thedigitaldinerapp.FoodNameActivity;
import com.example.thedigitaldinerapp.FoodOrderingActivity;
import com.example.thedigitaldinerapp.R;
import com.example.thedigitaldinerapp.SelectPaymentMethodActivity;
import com.example.thedigitaldinerapp.adaptors.RecyclerAdapterCart;
import com.example.thedigitaldinerapp.models.Cart;
import com.example.thedigitaldinerapp.models.FoodCategory;
import com.example.thedigitaldinerapp.models.User;
import com.example.thedigitaldinerapp.utils.ErrorUtils;
import com.example.thedigitaldinerapp.utils.ItemClickSupport;
import com.example.thedigitaldinerapp.utils.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class CartFragment extends Fragment implements RecyclerAdapterCart.OnRoomListener {
    RecyclerView recyclerViewMyCart;
    ArrayList<Cart> cartArrayList;
    RecyclerAdapterCart adapterCart;

    User user;

    int totalAmount = 0;
    TextView totalCartAmount;
    Button buttonPlaceOrder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cart, container, false);

        totalCartAmount = v.findViewById(R.id.totalCartAmount);
        buttonPlaceOrder = v.findViewById(R.id.buttonPlaceOrder);
        recyclerViewMyCart = (RecyclerView) v.findViewById(R.id.recyclerViewMyCart);

        user = SharedPrefManager.getInstance(getContext()).getUser();

        cartArrayList = new ArrayList<>();

        adapterCart = new RecyclerAdapterCart(cartArrayList, getContext(), getActivity(), user);
        getCartItems();
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerViewMyCart);

        adapterCart.setOnItemClickListener(new RecyclerAdapterCart.OnItemsClickListener() {
            @Override
            public void onItemClick(int totalAmount, int previousPrice) {
                int totalPriceAfterDeduction = Integer.parseInt(totalCartAmount.getText().toString()) - previousPrice;
                int finalTotalAmount = totalPriceAfterDeduction + totalAmount;
                totalCartAmount.setText(String.valueOf(finalTotalAmount));
            }
        });

        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(totalCartAmount.getText().toString()) == 0) {
                    Toast.makeText(getContext(), "Your cart is empty. Couldn't go further.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Bundle bundle = new Bundle();
                    bundle.putString("totalAmount", totalCartAmount.getText().toString());
                    Intent intent = new Intent(getContext(), SelectPaymentMethodActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        return v;
    }

    private void getCartItems() {
        String url = "https://s-t-k.000webhostapp.com/digitaldiner/get_cart_items.php";

        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("not_found")) {
                    Toast.makeText(getContext(), "No any cart item found!", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonResponse;

                        cartArrayList.clear();
                        for (int i = 0; i < jsonArray.length(); i++){
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

                        totalAmount = cartArrayList.stream().mapToInt(Cart::getPrice).sum();
                        totalCartAmount.setText(String.valueOf(totalAmount));
                        recyclerViewMyCart.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerViewMyCart.setAdapter(adapterCart);
                        adapterCart.notifyDataSetChanged();

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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userId", String.valueOf(user.getUserId()));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onRoomClick(int position) {

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAbsoluteAdapterPosition();

            Cart cart = cartArrayList.get(position);
            deleteItemFromCart(cart);
            adapterCart.notifyItemRemoved(position);
            //adapterCart.notifyDataSetChanged();
        }

        //using RecyclerViewSwipeDecorator library to decorate
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.DeleteColor))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    .addSwipeLeftLabel("Delete")
                    .setSwipeLeftLabelColor(ContextCompat.getColor(getContext(), R.color.white))
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void deleteItemFromCart(Cart cart) {
        String url = "https://s-t-k.000webhostapp.com/digitaldiner/delete_cart_item.php";

        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                    Toast.makeText(getContext(), cart.getFoodName() + " removed from cart.", Toast.LENGTH_SHORT).show();
                    getCartItems();
                }
                else{
                    Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userId", String.valueOf(user.getUserId()));
                params.put("foodId", String.valueOf(cart.getFoodId()));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
