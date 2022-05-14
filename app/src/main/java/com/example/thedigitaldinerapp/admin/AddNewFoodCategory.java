package com.example.thedigitaldinerapp.admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.thedigitaldinerapp.EditProfileActivity;
import com.example.thedigitaldinerapp.LoginActivity;
import com.example.thedigitaldinerapp.MainActivity;
import com.example.thedigitaldinerapp.R;
import com.example.thedigitaldinerapp.models.User;
import com.example.thedigitaldinerapp.utils.ErrorUtils;
import com.example.thedigitaldinerapp.utils.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddNewFoodCategory extends AppCompatActivity implements View.OnClickListener {

    ImageView _ivChooseIcon, _ivChoosedIcon;
    Button _btnAddNewFoodCategory;
    LinearLayout _lLUploadIcon;
    private final int IMG_REQUEST = 1;
    private Bitmap bitmap;

    EditText etNewCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_food_category);

        _ivChooseIcon = (ImageView) findViewById(R.id.ivChooseIcon);
        _ivChoosedIcon = (ImageView) findViewById(R.id.ivChoosedIcon);
        _btnAddNewFoodCategory = (Button) findViewById(R.id.btnAddNewFoodCategory);
        _lLUploadIcon = (LinearLayout) findViewById(R.id.lLUploadIcon);

        etNewCategoryName = findViewById(R.id.etNewCategoryName);

        _ivChooseIcon.setOnClickListener(this);
        _btnAddNewFoodCategory.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivChooseIcon:
                selectImage();
                _ivChooseIcon.setVisibility(View.GONE);
                _lLUploadIcon.setVisibility(View.GONE);
                _ivChoosedIcon.setVisibility(View.VISIBLE);
                break;

            case R.id.btnAddNewFoodCategory:
                addCategory();
                break;
        }

    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                _ivChoosedIcon.setImageBitmap(bitmap);
                _ivChoosedIcon.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {
            _ivChooseIcon.setVisibility(View.VISIBLE);
            _lLUploadIcon.setVisibility(View.VISIBLE);
            _ivChoosedIcon.setVisibility(View.GONE);
        }
    }

    private void addCategory() {
        final String image, categoryName;
        boolean error = false;
        String url = "https://s-t-k.000webhostapp.com/digitaldiner/add_category.php";
        categoryName = etNewCategoryName.getText().toString().trim();
        image = getStringImage(bitmap);

        if (image.isEmpty()){
            Toast.makeText(AddNewFoodCategory.this, "Please Choose Image", Toast.LENGTH_SHORT).show();
            error = true;
        }

        if (categoryName.isEmpty()) {
            etNewCategoryName.setError("Please fill this field");
            error = true;
        }

        if (!error) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            final RequestQueue requestQueue = Volley.newRequestQueue(AddNewFoodCategory.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    if (response.trim().equals("success")){
                        Toast.makeText(AddNewFoodCategory.this, "Category Added Successfully", Toast.LENGTH_SHORT).show();
                    }
                    else if (response.trim().equals("DBError")) {
                        Toast.makeText(AddNewFoodCategory.this, "Error While Inserting", Toast.LENGTH_SHORT).show();
                    }
                    else if (response.trim().equals("File Upload Error")){
                        Toast.makeText(AddNewFoodCategory.this, "Could not upload file", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(AddNewFoodCategory.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("image", image);
                    params.put("categoryName", categoryName);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }

    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try{
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        }catch (Exception ex){
            Toast.makeText(AddNewFoodCategory.this, "Please Select Image", Toast.LENGTH_SHORT).show();
        }
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }


}