package com.example.thedigitaldinerapp.adaptors;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.thedigitaldinerapp.R;
import com.example.thedigitaldinerapp.models.Cart;
import com.example.thedigitaldinerapp.models.User;
import com.example.thedigitaldinerapp.utils.ErrorUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerAdapterCart extends RecyclerView.Adapter<RecyclerAdapterCart.ViewHolder> {
    ArrayList<Cart> cartArrayList;
    Context context;
    Activity activity;
    User user;
    String previousTotal;
    private OnRoomListener mOnRoomListener;

    private OnItemsClickListener listener = null;

    int count;

    public RecyclerAdapterCart(ArrayList<Cart> cartArrayList, Context context, Activity activity, User user) {
        this.cartArrayList = cartArrayList;
        this.context = context;
        this.activity = activity;
        this.user = user;
    }

    @NonNull
    @Override
    public RecyclerAdapterCart.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.design_for_fragment_cart, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mOnRoomListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterCart.ViewHolder holder, int position) {

        Cart cart = cartArrayList.get(position);

        Glide.with(context).load(cart.getFoodImage()).into(holder._ivFoodImageMyCart);
        holder._tvFoodCategoryMyCart.setText(cart.getFoodCategoryName());
        holder._tvFoodNameMyCart.setText(cart.getFoodName());
        holder._tvFoodPriceMyCart.setText(String.valueOf(cart.getPrice()));
        holder._tvFoodQuantityMyCart.setText(String.valueOf(cart.getQuantity()));

        count = cart.getQuantity();
        holder.ivFoodQuantiyPlusMyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                holder._tvFoodQuantityMyCart.setText("" + count);
                String updatedQuantity = holder._tvFoodQuantityMyCart.getText().toString();
                int totalAmount = Integer.parseInt(updatedQuantity) * cart.getPerItemPrice();
                if (listener != null) {
                    updateCart(cart, updatedQuantity, totalAmount, activity);
                    previousTotal = holder._tvFoodPriceMyCart.getText().toString();
                    holder._tvFoodPriceMyCart.setText(String.valueOf(totalAmount));
                    listener.onItemClick(totalAmount, Integer.parseInt(previousTotal));
                }

            }
        });

        holder.ivFoodQuantiyMinusMyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count <= 1) count = 1;
                else count--;
                holder._tvFoodQuantityMyCart.setText("" + count);
                String updatedQuantity = holder._tvFoodQuantityMyCart.getText().toString();
                int totalAmount = Integer.parseInt(updatedQuantity) * cart.getPerItemPrice();
                if (listener != null) {
                    updateCart(cart, updatedQuantity, totalAmount, activity);
                    previousTotal = holder._tvFoodPriceMyCart.getText().toString();
                    holder._tvFoodPriceMyCart.setText(String.valueOf(totalAmount));
                    listener.onItemClick(totalAmount, Integer.parseInt(previousTotal));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView _tvFoodCategoryMyCart, _tvFoodNameMyCart, _tvFoodPriceMyCart, _tvFoodQuantityMyCart;
        ImageView _ivFoodImageMyCart, ivFoodQuantiyPlusMyCart, ivFoodQuantiyMinusMyCart;

        OnRoomListener onRoomListener;

        public ViewHolder(@NonNull View itemView, OnRoomListener onRoomListener) {
            super(itemView);
            _tvFoodCategoryMyCart = itemView.findViewById(R.id.tvFoodCategoryMyCart);
            _tvFoodNameMyCart = itemView.findViewById(R.id.tvFoodNameMyCart);
            _tvFoodPriceMyCart = itemView.findViewById(R.id.tvFoodPriceMyCart);
            _tvFoodQuantityMyCart = itemView.findViewById(R.id.tvFoodQuantityMyCart);
            _ivFoodImageMyCart = itemView.findViewById(R.id.ivFoodImageMyCart);
            ivFoodQuantiyPlusMyCart = itemView.findViewById(R.id.ivFoodQuantiyPlusMyCart);
            ivFoodQuantiyMinusMyCart = itemView.findViewById(R.id.ivFoodQuantiyMinusMyCart);
            this.onRoomListener = onRoomListener;

        }

        @Override
        public void onClick(View view)  {
            onRoomListener.onRoomClick(getAdapterPosition());
        }
    }

    public interface OnRoomListener {
        void onRoomClick(int position);
    }

    public interface OnItemsClickListener {
        void onItemClick(int totalAmount, int previousTotal);
    }

    public void setOnItemClickListener(OnItemsClickListener listener) {
        this.listener = listener;
    }

    private void updateCart(Cart cart, String updatedQuantity, int totalAmount, Activity activity) {
        String url = "https://s-t-k.000webhostapp.com/digitaldiner/update_cart.php";
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.trim().equals("success")) {
                    Toast.makeText(activity.getApplicationContext(), "Successfully updated.", Toast.LENGTH_SHORT).show();
                } else if (response.trim().equals("error")) {
                    Toast.makeText(activity.getApplicationContext(), "Error while inserting", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity.getApplicationContext(), "This is not your cart item.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(activity.getApplicationContext(), ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("foodId", String.valueOf(cart.getFoodId()));
                params.put("userId", String.valueOf(user.getUserId()));
                params.put("totalQuantity", updatedQuantity);
                params.put("totalAmount", String.valueOf(totalAmount));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
