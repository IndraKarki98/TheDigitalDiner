package com.example.thedigitaldinerapp.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thedigitaldinerapp.R;

public class RecyclerAdapterFoodNameActivity extends RecyclerView.Adapter<RecyclerAdapterFoodNameActivity.ViewHolder> {

    String tvFoodNameFoodNameActivity[];
    String tvFoodPriceFoodNameActivity[];
    int ivFoodImageFoodNameActivity[];
    Context context;

    private OnRoomListener mOnRoomListener;


    //creating constructor and context n data should be change in order as in parent class
//    public RecyclerAdapter(Context context, String[] data) {
//        this.data = data;
//        this.context = context;
//
//    }

    public RecyclerAdapterFoodNameActivity(Context context, String[] tvFoodNameFoodNameActivity, String[] tvFoodPriceFoodNameActivity, int[] ivFoodImageFoodNameActivity, OnRoomListener onRoomListener) {
        this.tvFoodNameFoodNameActivity = tvFoodNameFoodNameActivity;
        this.tvFoodPriceFoodNameActivity = tvFoodPriceFoodNameActivity;
        this.ivFoodImageFoodNameActivity = ivFoodImageFoodNameActivity;
        this.context = context;
        this.mOnRoomListener = onRoomListener;
    }




    @NonNull
    @Override
    public RecyclerAdapterFoodNameActivity.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.design_for_foodname_activity, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mOnRoomListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterFoodNameActivity.ViewHolder holder, int position) {
        holder._tvFoodNameFoodNameActivity.setText(tvFoodNameFoodNameActivity[position]);
        holder._tvFoodPriceFoodNameActivity.setText(tvFoodPriceFoodNameActivity[position]);
        holder._ivFoodImageFoodNameActivity.setImageResource(ivFoodImageFoodNameActivity[position]);


//        holder._tvroomsname.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, "clicked on: " + roomsName[position], Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        //pass the array length, which is number of elments present in the array
        return tvFoodNameFoodNameActivity.length;

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView _tvFoodNameFoodNameActivity, _tvFoodPriceFoodNameActivity;
        ImageView _ivFoodImageFoodNameActivity;
        OnRoomListener onRoomListener;

        public ViewHolder(@NonNull View itemView, OnRoomListener onRoomListener) {
            super(itemView);
            _tvFoodNameFoodNameActivity = itemView.findViewById(R.id.tvFoodNameFoodNameActivity);
            _tvFoodPriceFoodNameActivity = itemView.findViewById(R.id.tvFoodPriceFoodNameActivity);
            _ivFoodImageFoodNameActivity = itemView.findViewById(R.id.ivFoodImageFoodNameActivity);
            this.onRoomListener = onRoomListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onRoomListener.onRoomClick(getAdapterPosition());
        }
    }

    public interface OnRoomListener {
        void onRoomClick(int position);
    }
}
