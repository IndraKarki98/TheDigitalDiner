package com.example.thedigitaldinerapp.admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.thedigitaldinerapp.R;
import com.example.thedigitaldinerapp.models.FoodCategory;
import com.example.thedigitaldinerapp.utils.ErrorUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddNewFoodItem extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    ImageView _ivChooseImage, _ivChoosedImage;
    Button _btnAddNewFoodItem;
    LinearLayout _lLUploadImageIcon;
    private final int IMG_REQUEST = 1;
    private Bitmap bitmap;
    private Spinner spinner;

    private int categoryId;

    private ArrayList<FoodCategory> foodCategoryArrayList;
    private ArrayAdapter<FoodCategory> foodCategoryArrayAdapter;

    EditText etNewFoodName, etFoodPriceAddFoodItem, etIngredientsAddFoodItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_food_item);

        etNewFoodName = findViewById(R.id.etNewFoodName);
        etFoodPriceAddFoodItem = findViewById(R.id.etFoodPriceAddFoodItem);
        etIngredientsAddFoodItem = findViewById(R.id.etIngredientsAddFoodItem);

        foodCategoryArrayList = new ArrayList<>();

        //to control spinner or dropdown menu for room number
        spinner = findViewById(R.id.roomsNumberDropdownSpinner_ordering);
        spinner.setOnItemSelectedListener(this);

        etNewFoodName.addTextChangedListener(nameTextWatcher);


        _ivChooseImage = (ImageView) findViewById(R.id.ivChooseImage);
        _ivChoosedImage = (ImageView) findViewById(R.id.ivChoosedImage);
        _btnAddNewFoodItem = (Button) findViewById(R.id.btnAddNewFoodItem);
        _lLUploadImageIcon = (LinearLayout) findViewById(R.id.lLUploadImageIcon);

        _ivChooseImage.setOnClickListener(this);
        _btnAddNewFoodItem.setOnClickListener(this);

        getCategories();
    }

    //to control action of dropdown(spinner) selected data
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        FoodCategory categories = (FoodCategory) adapterView.getSelectedItem();
        categoryId = categories.getId();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ivChooseImage:
                selectImage();
                _ivChooseImage.setVisibility(View.GONE);
                _lLUploadImageIcon.setVisibility(View.GONE);
                _ivChoosedImage.setVisibility(View.VISIBLE);
                break;

            case R.id.btnAddNewFoodItem:
                addFoodItem();
                break;
        }

    }

    private TextWatcher nameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String foodName = etNewFoodName.getText().toString().trim();
            if (!foodName.matches("[a-zA-Z\\s]+")) {
                etNewFoodName.setError("Please enter your valid name");
                _btnAddNewFoodItem.setEnabled(false);
            } else {
                etNewFoodName.setError(null);
                _btnAddNewFoodItem.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

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
                _ivChoosedImage.setImageBitmap(bitmap);
                _ivChoosedImage.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {
            _ivChooseImage.setVisibility(View.VISIBLE);
            _lLUploadImageIcon.setVisibility(View.VISIBLE);
            _ivChoosedImage.setVisibility(View.GONE);
        }
    }

    private void addFoodItem() {
        final String image, foodName, price, ingredients;
        boolean error = false;
        String url = "https://s-t-k.000webhostapp.com/digitaldiner/add_food.php";
        foodName = etNewFoodName.getText().toString().trim();
        price = etFoodPriceAddFoodItem.getText().toString().trim();
        ingredients = etIngredientsAddFoodItem.getText().toString().trim();
        image = getStringImage(bitmap);

        if (image.isEmpty()){
            Toast.makeText(AddNewFoodItem.this, "Please Choose Image", Toast.LENGTH_SHORT).show();
            error = true;
        }

        if (foodName.isEmpty()) {
            etNewFoodName.setError("Please fill this field");
            error = true;
        }

        if (!foodName.matches("[a-zA-Z\\s]+")){
            etNewFoodName.setError("Please enter valid name");
            error = true;
        }

        if (price.isEmpty()) {
            etFoodPriceAddFoodItem.setError("Please fill this field");
            error = true;
        }

        if (ingredients.isEmpty()) {
            etIngredientsAddFoodItem.setError("Please fill this field");
            error = true;
        }

        if (!error) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            final RequestQueue requestQueue = Volley.newRequestQueue(AddNewFoodItem.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    if (response.trim().equals("success")){
                        Toast.makeText(AddNewFoodItem.this, "Food Added Successfully", Toast.LENGTH_SHORT).show();
                        etNewFoodName.setText(null);
                        etFoodPriceAddFoodItem.setText(null);
                        etIngredientsAddFoodItem.setText(null);
                        _ivChoosedImage.setVisibility(View.GONE);
                        _ivChooseImage.setVisibility(View.VISIBLE);
                        _lLUploadImageIcon.setVisibility(View.VISIBLE);
                        etNewFoodName.setError(null);
                    }
                    else if (response.trim().equals("DBError")) {
                        Toast.makeText(AddNewFoodItem.this, "Error While Inserting", Toast.LENGTH_SHORT).show();
                    }
                    else if (response.trim().equals("File Upload Error")){
                        Toast.makeText(AddNewFoodItem.this, "Could not upload file", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(AddNewFoodItem.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("image", image);
                    params.put("categoryId", String.valueOf(categoryId));
                    params.put("foodName", String.valueOf(foodName));
                    params.put("price", String.valueOf(price));
                    params.put("ingredients", String.valueOf(ingredients));
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }

    private void getCategories() {
        String url = "https://s-t-k.000webhostapp.com/digitaldiner/get_categories.php";

        final RequestQueue requestQueue = Volley.newRequestQueue(AddNewFoodItem.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("not_found")) {
                    Toast.makeText(AddNewFoodItem.this, "No Categories Found", Toast.LENGTH_SHORT).show();

                }
                else{
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonResponse;

                        for (int i = 0; i < jsonArray.length(); i++){
                            jsonResponse = jsonArray.getJSONObject(i);
                            int foodCategoryId = jsonResponse.getInt("id");
                            String categoryName = jsonResponse.getString("category_name");
                            String image = jsonResponse.getString("image");

                            FoodCategory foodCategory = new FoodCategory(foodCategoryId, categoryName, image);
                            foodCategoryArrayList.add(foodCategory);
                            foodCategoryArrayAdapter = new ArrayAdapter<>(AddNewFoodItem.this, android.R.layout.simple_spinner_item, foodCategoryArrayList);
                            foodCategoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(foodCategoryArrayAdapter);
                        }


                    }

                    catch (JSONException e) {
                        Toast.makeText(AddNewFoodItem.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddNewFoodItem.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
            }
        }) {

        };
        requestQueue.add(stringRequest);
    }

    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try{
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        }catch (Exception ex){
            Toast.makeText(AddNewFoodItem.this, "Please Select Image", Toast.LENGTH_SHORT).show();
        }
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
}