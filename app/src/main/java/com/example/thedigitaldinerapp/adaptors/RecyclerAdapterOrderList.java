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

public class RecyclerAdapterOrderList extends RecyclerView.Adapter<RecyclerAdapterOrderList.ViewHolder> {

    Context context;
    ArrayList<OrderHistory> orderHistoryArrayList;

    public RecyclerAdapterOrderList(Context context, ArrayList<OrderHistory> orderHistoryArrayList) {
        this.context = context;
        this.orderHistoryArrayList = orderHistoryArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.design_for_admin_order_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        OrderHistory orderHistory = orderHistoryArrayList.get(position);

        holder._tvDateTimeOrderList.setText(orderHistory.getDateTime());
        holder._tvFullNameOrderList.setText(orderHistory.getFullName());
        holder._tvEmailOrderList.setText(orderHistory.getEmail());
        holder._tvPhoneOrderList.setText(orderHistory.getPhone());
        holder._tvAddressOrderList.setText(orderHistory.getAddress());
        holder._tvFoodCategoryOrderList.setText(orderHistory.getFoodCategory());
        holder._tvFoodNameOrderList.setText(orderHistory.getFoodName());
        holder._tvQuantityOrderList.setText(orderHistory.getQuantity());
        holder._tvTotalAmountOrderList.setText(orderHistory.getTotalAmount());
        holder._tvPaymentMethodOrderList.setText(orderHistory.getPaymentMethod());

    }

    @Override
    public int getItemCount() {
        return orderHistoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView _tvDateTimeOrderList, _tvFullNameOrderList, _tvEmailOrderList, _tvPhoneOrderList, _tvAddressOrderList, _tvFoodCategoryOrderList,
                _tvFoodNameOrderList, _tvQuantityOrderList, _tvTotalAmountOrderList, _tvPaymentMethodOrderList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            _tvDateTimeOrderList = itemView.findViewById(R.id.tvDateTimeOrderList);
            _tvFullNameOrderList = itemView.findViewById(R.id.tvFullNameOrderList);
            _tvEmailOrderList = itemView.findViewById(R.id.tvEmailOrderList);
            _tvPhoneOrderList = itemView.findViewById(R.id.tvPhoneOrderList);
            _tvAddressOrderList = itemView.findViewById(R.id.tvAddressOrderList);
            _tvFoodCategoryOrderList = itemView.findViewById(R.id.tvFoodCategoryOrderList);
            _tvFoodNameOrderList = itemView.findViewById(R.id.tvFoodNameOrderList);
            _tvQuantityOrderList = itemView.findViewById(R.id.tvQuantityOrderList);
            _tvTotalAmountOrderList = itemView.findViewById(R.id.tvTotalAmountOrderList);
            _tvPaymentMethodOrderList = itemView.findViewById(R.id.tvPaymentMethodOrderList);

        }

    }

}
