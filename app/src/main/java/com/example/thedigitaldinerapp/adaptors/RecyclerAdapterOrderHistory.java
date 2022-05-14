package com.example.thedigitaldinerapp.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thedigitaldinerapp.R;
import com.example.thedigitaldinerapp.models.OrderHistory;

import java.util.ArrayList;

public class RecyclerAdapterOrderHistory extends RecyclerView.Adapter<RecyclerAdapterOrderHistory.ViewHolder> {

    Context context;
    ArrayList<OrderHistory> orderHistoryArrayList;

    public RecyclerAdapterOrderHistory(Context context, ArrayList<OrderHistory> orderHistoryArrayList) {
        this.context = context;
        this.orderHistoryArrayList = orderHistoryArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.design_for_order_history, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        OrderHistory orderHistory = orderHistoryArrayList.get(position);

        holder._tvFoodCategoryOrderHistory.setText(orderHistory.getFoodCategory());
        holder._tvFoodNameOrderHistory.setText(orderHistory.getFoodName());
        holder._tvQuantityOrderHistory.setText(orderHistory.getQuantity());
        holder._tvTotalAmountOrderHistory.setText(orderHistory.getTotalAmount());
        holder._tvPaymentMethodOrderHistory.setText(orderHistory.getPaymentMethod());
        holder._tvDateTimeOrderHistory.setText(orderHistory.getDateTime());

    }

    @Override
    public int getItemCount() {
        return orderHistoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView _tvFoodCategoryOrderHistory, _tvFoodNameOrderHistory, _tvQuantityOrderHistory, _tvTotalAmountOrderHistory, _tvPaymentMethodOrderHistory, _tvDateTimeOrderHistory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            _tvFoodCategoryOrderHistory = itemView.findViewById(R.id.tvFoodCategoryOrderHistory);
            _tvFoodNameOrderHistory = itemView.findViewById(R.id.tvFoodNameOrderHistory);
            _tvQuantityOrderHistory = itemView.findViewById(R.id.tvQuantityOrderHistory);
            _tvTotalAmountOrderHistory = itemView.findViewById(R.id.tvTotalAmountOrderHistory);
            _tvPaymentMethodOrderHistory = itemView.findViewById(R.id.tvPaymentMethodOrderHistory);
            _tvDateTimeOrderHistory = itemView.findViewById(R.id.tvDateTimeOrderHistory);

        }

    }

}
