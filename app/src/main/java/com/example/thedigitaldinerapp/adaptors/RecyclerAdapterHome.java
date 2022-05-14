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

public class RecyclerAdapterHome extends RecyclerView.Adapter<RecyclerAdapterHome.ViewHolder> {

    String ivFoodCategoryHome[];
    int ivFoodIconHome[];
    Context context;

    private OnRoomListener mOnRoomListener;


    //creating constructor and context n data should be change in order as in parent class
//    public RecyclerAdapter(Context context, String[] data) {
//        this.data = data;
//        this.context = context;
//
//    }

    public RecyclerAdapterHome(Context context, String[] ivFoodCategoryHome, int[] ivFoodIconHome, OnRoomListener onRoomListener) {
        this.ivFoodCategoryHome = ivFoodCategoryHome;
        this.ivFoodIconHome = ivFoodIconHome;
        this.context = context;
        this.mOnRoomListener = onRoomListener;
    }




    @NonNull
    @Override
    public RecyclerAdapterHome.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.design_for_fragment_home, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mOnRoomListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterHome.ViewHolder holder, int position) {
        holder._ivFoodCategoryHome.setText(ivFoodCategoryHome[position]);
        holder._ivFoodIconHome.setImageResource(ivFoodIconHome[position]);


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
        return ivFoodCategoryHome.length;

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView _ivFoodCategoryHome;
        ImageView _ivFoodIconHome;
        OnRoomListener onRoomListener;

        public ViewHolder(@NonNull View itemView, OnRoomListener onRoomListener) {
            super(itemView);
            _ivFoodCategoryHome = itemView.findViewById(R.id.ivFoodCategoryHome);
            _ivFoodIconHome = itemView.findViewById(R.id.ivFoodIconHome);
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
