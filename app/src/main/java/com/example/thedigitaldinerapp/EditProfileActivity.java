package com.example.thedigitaldinerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.thedigitaldinerapp.models.User;
import com.example.thedigitaldinerapp.utils.ErrorUtils;
import com.example.thedigitaldinerapp.utils.SharedPrefManager;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class EditProfileActivity extends AppCompatActivity {
    Toolbar _tbEditUserProfile;
    private User user;
    private EditText etFullnameEditProfile, etEmailEditProfile, AddressEditProfile, etPhoneEditProfile, etPasswordEditProfile;
    private Button btnSaveUserDetails;
    private int oldUserId;
    private String oldEmail;
    private String userType;

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
        setContentView(R.layout.activity_edit_profile);

        _tbEditUserProfile = (Toolbar) findViewById(R.id.tbEditUserProfile);
        setSupportActionBar(_tbEditUserProfile);

        user = SharedPrefManager.getInstance(this).getUser();
        oldUserId = user.getUserId();
        oldEmail = user.getEmail();
        userType = user.getUserType();

        etFullnameEditProfile = findViewById(R.id.etFullnameEditProfile);
        etEmailEditProfile = findViewById(R.id.etEmailEditProfile);
        AddressEditProfile = findViewById(R.id.AddressEditProfile);
        etPhoneEditProfile = findViewById(R.id.etPhoneEditProfile);
        etPasswordEditProfile = findViewById(R.id.etPasswordEditProfile);

        btnSaveUserDetails = findViewById(R.id.btnSaveUserDetails);

        etFullnameEditProfile.addTextChangedListener(nameTextWatcher);
        etPhoneEditProfile.addTextChangedListener(phoneTextWatcher);
        etEmailEditProfile.addTextChangedListener(emailTextWatcher);
        etPasswordEditProfile.addTextChangedListener(passwordTextWatcher);

        btnSaveUserDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDetails();
            }
        });

        setData();
    }

    private TextWatcher nameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String full_name = etFullnameEditProfile.getText().toString().trim();
            if (!full_name.matches("[a-zA-Z\\s]+")) {
                etFullnameEditProfile.setError("Please enter your valid name");
                btnSaveUserDetails.setEnabled(false);
            } else {
                etFullnameEditProfile.setError(null);
                btnSaveUserDetails.setEnabled(true);
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
            String email = etEmailEditProfile.getText().toString().trim();
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                etEmailEditProfile.setError("Please enter a valid email address");
                btnSaveUserDetails.setEnabled(false);
            }
            else {
                etEmailEditProfile.setError(null);
                btnSaveUserDetails.setEnabled(true);
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
            String pass = etPasswordEditProfile.getText().toString().trim();
            if (!PASSWORD_PATTERN.matcher(pass).matches()){
                etPasswordEditProfile.setError("Password must be 6 character long and include one lowercase, digit, special character and uppercase");
                btnSaveUserDetails.setEnabled(false);

            }
            else {
                etPasswordEditProfile.setError(null);
                btnSaveUserDetails.setEnabled(true);
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
            String phone = etPhoneEditProfile.getText().toString().trim();
            if (!PHONE_PATTERN.matcher(phone).matches()){
                etPhoneEditProfile.setError("Please enter valid phone number");
                btnSaveUserDetails.setEnabled(false);
            }
            else {
                etPhoneEditProfile.setError(null);
                btnSaveUserDetails.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void setData() {
        etFullnameEditProfile.setText(user.getFullName());
        AddressEditProfile.setText(user.getAddress());
        etPhoneEditProfile.setText(user.getPhone());
        etEmailEditProfile.setText(user.getEmail());
        etPasswordEditProfile.setText(user.getPassword());
    }

    private void updateDetails() {
        final String fullName, newEmail, phone, address, newPassword;
        boolean error = false;
        String url = "https://s-t-k.000webhostapp.com/digitaldiner/edit_profile.php";
        fullName = etFullnameEditProfile.getText().toString().trim();
        newEmail = etEmailEditProfile.getText().toString().trim();
        phone = etPhoneEditProfile.getText().toString().trim();
        address = AddressEditProfile.getText().toString().trim();
        newPassword = etPasswordEditProfile.getText().toString().trim();

        if (fullName.isEmpty()){
            etFullnameEditProfile.setError("Please fill this field");
            error = true;
        }
        if (!fullName.matches("[a-zA-Z\\s]+")){
            etFullnameEditProfile.setError("Please enter your name properly");
            error = true;
        }
        if (newEmail.isEmpty()){
            etEmailEditProfile.setError("Please fill this field");
            error = true;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            etEmailEditProfile.setError("Please enter a valid email address");
            error = true;
        }
        if (phone.isEmpty()){
            etPhoneEditProfile.setError("Please fill this field");
            error = true;
        }
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            etPhoneEditProfile.setError("Please enter valid phone number");
            error = true;
        }

        if (newPassword.isEmpty()){
            etPasswordEditProfile.setError("Please fill this field");
            error = true;
        }
        if (!PASSWORD_PATTERN.matcher(newPassword).matches()) {
            etPasswordEditProfile.setError("Password too weak. Must be 6 character long");
            error = true;
        }
        if (address.isEmpty()) {
            AddressEditProfile.setError("Please fill this field");
            error = true;
        }

        if (!error) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            final RequestQueue requestQueue = Volley.newRequestQueue(EditProfileActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    if (response.trim().equals("success")) {
                        Toast.makeText(EditProfileActivity.this, "Success! Profile Updated", Toast.LENGTH_SHORT).show();
                        if (!newPassword.equals(user.getPassword()) || !newEmail.equals(user.getEmail())) {
                            Intent intent = new Intent(EditProfileActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                        else {
                            User user = new User(oldUserId, fullName, address, phone, newEmail, newPassword, userType);
                            SharedPrefManager.getInstance(EditProfileActivity.this).userLogin(user);
                            Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        finish();

                    } else if (response.trim().equals("dbError")) {
                        Toast.makeText(EditProfileActivity.this, "Error while inserting", Toast.LENGTH_SHORT).show();
                    } else if (response.trim().equals("dbError2")) {
                        Toast.makeText(EditProfileActivity.this, "Error while fetching", Toast.LENGTH_SHORT).show();
                    } else if (response.trim().equals("email_taken")) {
                        Toast.makeText(EditProfileActivity.this, "Sorry! This email is already registered", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(EditProfileActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("fullName", fullName);
                    params.put("newEmail", newEmail);
                    params.put("oldEmail", oldEmail);
                    params.put("userId", String.valueOf(user.getUserId()));
                    params.put("phone", phone);
                    params.put("address", address);
                    params.put("newPassword", newPassword);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }
}