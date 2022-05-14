package com.example.thedigitaldinerapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.thedigitaldinerapp.utils.ErrorUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private EditText eT_FullName_SignUp, eT_Phone_SignUp, eT_Address_SignUp, eT_Email_Signup, eT_Password_SignUp;
    private Button btnSignUp;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 6 characters
                    "$");

    private static final Pattern PHONE_PATTERN = Pattern.compile("^[+]?[0-9]{10,13}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        eT_FullName_SignUp = findViewById(R.id.eT_FullName_SignUp);
        eT_Phone_SignUp = findViewById(R.id.eT_Phone_SignUp);
        eT_Address_SignUp = findViewById(R.id.eT_Address_SignUp);
        eT_Email_Signup = findViewById(R.id.eT_Email_Signup);
        eT_Password_SignUp = findViewById(R.id.eT_Password_SignUp);

        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        eT_FullName_SignUp.addTextChangedListener(nameTextWatcher);
        eT_Email_Signup.addTextChangedListener(emailTextWatcher);
        eT_Phone_SignUp.addTextChangedListener(phoneTextWatcher);
        eT_Password_SignUp.addTextChangedListener(passwordTextWatcher);
    }

    private TextWatcher nameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String full_name = eT_FullName_SignUp.getText().toString().trim();
            if (!full_name.matches("[a-zA-Z\\s]+")) {
                eT_FullName_SignUp.setError("Please enter your valid name");
                btnSignUp.setEnabled(false);
            } else {
                eT_FullName_SignUp.setError(null);
                btnSignUp.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private TextWatcher emailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String email = eT_Email_Signup.getText().toString().trim();
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                eT_Email_Signup.setError("Please enter a valid email address");
                btnSignUp.setEnabled(false);
            }
            else {
                eT_Email_Signup.setError(null);
                btnSignUp.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String pass = eT_Password_SignUp.getText().toString().trim();
            if (!PASSWORD_PATTERN.matcher(pass).matches()){
                eT_Password_SignUp.setError("Password must be 6 character long and include one lowercase, digit, special character and uppercase");
                btnSignUp.setEnabled(false);

            }
            else {
                eT_Password_SignUp.setError(null);
                btnSignUp.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher phoneTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String phone = eT_Phone_SignUp.getText().toString().trim();
            if (!PHONE_PATTERN.matcher(phone).matches()){
                eT_Phone_SignUp.setError("Please enter valid phone number");
                btnSignUp.setEnabled(false);
            }
            else {
                eT_Phone_SignUp.setError(null);
                btnSignUp.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void register() {
        final String fullName, email, password, phone, address;
        boolean error = false;
        String url = "https://s-t-k.000webhostapp.com/digitaldiner/register.php";
        fullName = eT_FullName_SignUp.getText().toString().trim();
        email = eT_Email_Signup.getText().toString().trim();
        password = eT_Password_SignUp.getText().toString().trim();
        phone = eT_Phone_SignUp.getText().toString().trim();
        address = eT_Address_SignUp.getText().toString().trim();

        if (fullName.isEmpty()){
            eT_FullName_SignUp.setError("Please fill this field");
            error = true;
        }
        if (!fullName.matches("[a-zA-Z\\s]+")){
            eT_FullName_SignUp.setError("Please enter your valid name");
            error = true;
        }
        if (email.isEmpty()){
            eT_Email_Signup.setError("Please fill this field");
            error = true;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            eT_Email_Signup.setError("Please enter a valid email address");
            error = true;
        }
        if (password.isEmpty()){
            eT_Password_SignUp.setError("Please fill this field");
            error = true;
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            eT_Password_SignUp.setError("Password too weak. Must be 6 character long");
            error = true;
        }
        if (phone.isEmpty()){
            eT_Phone_SignUp.setError("Please fill this field");
            error = true;
        }
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            eT_Phone_SignUp.setError("Please enter valid phone number");
            error = true;
        }

        if (address.isEmpty()) {
            eT_Address_SignUp.setError("Please fill this field");
            error = true;
        }

        if (!error) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            final RequestQueue requestQueue = Volley.newRequestQueue(SignupActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    if (response.trim().equals("success")) {
                        Toast.makeText(SignupActivity.this, "Account successfully registered", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if (response.trim().equals("dbError")) {
                        Toast.makeText(getApplicationContext(), "Error while inserting", Toast.LENGTH_SHORT).show();
                    }
                    else if (response.trim().equals("email_taken")) {
                        Toast.makeText(getApplicationContext(), "Email already registered", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(SignupActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("fullName", fullName);
                    params.put("email", email);
                    params.put("password", password);
                    params.put("phone", phone);
                    params.put("address", address);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
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
}