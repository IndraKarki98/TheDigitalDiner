package com.example.thedigitaldinerapp.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.thedigitaldinerapp.LoginActivity;
import com.example.thedigitaldinerapp.R;
import com.example.thedigitaldinerapp.SignupActivity;
import com.example.thedigitaldinerapp.utils.SharedPrefManager;

public class AdminMainPanel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_panel);
    }

    public void goToOrder(View view) {
        Intent intent = new Intent(AdminMainPanel.this, OrderList.class);
        startActivity(intent);
    }


    public void goToAddingFood(View view) {
        Intent intent = new Intent(AdminMainPanel.this, AddingFoods.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminMainPanel.this);
        builder.setMessage("Are you sure want to exit the app?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
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

    public void goToAdminLogout(View view) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AdminMainPanel.this);
        builder.setMessage("Logout from the app?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPrefManager.getInstance(AdminMainPanel.this).logout();
                Intent intent = new Intent(AdminMainPanel.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}