package com.example.thedigitaldinerapp.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.thedigitaldinerapp.LoginActivity;
import com.example.thedigitaldinerapp.R;
import com.example.thedigitaldinerapp.models.User;
import com.example.thedigitaldinerapp.utils.SharedPrefManager;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class ProfileFragment extends Fragment {

    private TextView tvUserFullname, tvUserEmail, tvUserPhone, tvUserAddress, tvLogout, tvGreeting;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        tvUserFullname = v.findViewById(R.id.tvUserFullname);
        tvGreeting = v.findViewById(R.id.tvGreeting);
        tvUserEmail = v.findViewById(R.id.tvUserEmail);
        tvUserPhone = v.findViewById(R.id.tvUserPhone);
        tvUserAddress = v.findViewById(R.id.tvUserAddress);
        tvLogout = v.findViewById(R.id.tvLogout);

        user = SharedPrefManager.getInstance(getContext()).getUser();

        refreshGreetingText();
        setData();

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        return v;
    }

    private void refreshGreetingText() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Calendar c = Calendar.getInstance();
                int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

                if(timeOfDay >= 0 && timeOfDay < 12){
                    tvGreeting.setText("Good Morning");
                    refreshGreetingText();
                }else if(timeOfDay >= 12 && timeOfDay < 16){
                    tvGreeting.setText("Good Afternoon");
                    refreshGreetingText();
                }else if(timeOfDay >= 16 && timeOfDay < 21){
                    tvGreeting.setText("Good Evening");
                    refreshGreetingText();
                }else if(timeOfDay >= 21 && timeOfDay < 24){
                    tvGreeting.setText("Good Night");
                    refreshGreetingText();
                }
            }
        }, 1000);
    }

    private void setData() {
        tvUserFullname.setText(user.getFullName());
        tvUserEmail.setText(user.getEmail());
        tvUserPhone.setText(user.getPhone());
        tvUserAddress.setText(user.getAddress());
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Logout from the app?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPrefManager.getInstance(getContext()).logout();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();

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
}
