package com.example.sidechef;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sidechef.Data.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class Recipe_Insert extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    protected Spinner mealtype;
    protected Spinner cuisine;
    private EditText recname;
    private EditText recdescrip;
    private EditText ingredient;
    private EditText recproc;
    protected Button newrec;
    private String spinmeal;
    private String spincui;
    private ImageView recimg;
    protected Button uploadimg;

    private int REQUEST_CAMERA = 1, SELECT_FILE = 0;

    protected Uri imgpath;
    private Bitmap imgtostore;

    DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_insert);

        recname = findViewById(R.id.insertrecname);
        mealtype = findViewById(R.id.insertmealtype);
        cuisine = findViewById(R.id.insertcuisine);
        recdescrip = findViewById(R.id.insertrecdescrip);
        ingredient = findViewById(R.id.insertingredient);
        recproc = findViewById(R.id.insertrecproc);
        newrec = findViewById(R.id.insertrecbutton);
        uploadimg = findViewById(R.id.uploadimage);
        recimg = findViewById(R.id.insertrecimage);

        db = new DataBaseHelper(this);

//        Request for Camera Permission
        if(ContextCompat.checkSelfPermission(Recipe_Insert.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Recipe_Insert.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100 );
        }

//        Spinner for mealtype
        List<String> item_meal = new ArrayList<>();
        item_meal.add("Choose");
        item_meal.add("Breakfast");
        item_meal.add("Dinner");
        item_meal.add("Lunch");
        item_meal.add("Snacks");
        ArrayAdapter<String> adapter_meal = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, item_meal);
        adapter_meal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mealtype.setAdapter(adapter_meal);
        mealtype.setOnItemSelectedListener(this);

//        Spinner for cuisine
        List<String> item_cuisine = new ArrayList<>();
        item_cuisine.add("Choose");
        item_cuisine.add("Arab");
        item_cuisine.add("Chinese");
        item_cuisine.add("French");
        item_cuisine.add("Indian");
        item_cuisine.add("Indonesian");
        item_cuisine.add("Italian");
        item_cuisine.add("Japanese");
        item_cuisine.add("Moroccan");
        item_cuisine.add("Spanish");
        item_cuisine.add("Thai");
        item_cuisine.add("Turkish");

        ArrayAdapter<String> adapter_cuisine = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, item_cuisine);
        adapter_cuisine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cuisine.setAdapter(adapter_cuisine);
        cuisine.setOnItemSelectedListener(this);


//        Insert recipe button
        newrec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = recname.getText().toString().trim();
                String descrip = recdescrip.getText().toString().trim();
                String ingrdnt = ingredient.getText().toString().trim();
                String proc = recproc.getText().toString().trim();
                String meal = spinmeal;
                String cui = spincui;
                Bitmap img = imgtostore;

                try {
                    if(!name.isEmpty() && !meal.equals("Choose") && !cui.equals("Choose") && !descrip.isEmpty() && !ingrdnt.isEmpty() && !proc.isEmpty() && img != null) {
                        long val = db.addrecipe(name, meal, cui, descrip, ingrdnt, proc, img);
                        if (val > 0) {
                            finish();
                            Toast.makeText(Recipe_Insert.this, "Recipe Inserted", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(Recipe_Insert.this, "Fill all the Details", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Toast.makeText(Recipe_Insert.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

//        Upload Image
        uploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectimage();
            }
        });

    }

//    Adapter for Spinners
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.insertmealtype)
        {
            spinmeal = parent.getItemAtPosition(position).toString();
        }
        else if(parent.getId() == R.id.insertcuisine)
        {
            spincui = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try{
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == SELECT_FILE && resultCode == RESULT_OK && data != null && data.getData() != null ) {
                imgpath = data.getData();
                imgtostore = MediaStore.Images.Media.getBitmap(getContentResolver(), imgpath);
                recimg.setImageBitmap(imgtostore);
            }
            if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
                assert data != null;
                Bundle extras = data.getExtras();
                assert extras != null;
                imgtostore = (Bitmap) extras.get("data");
                recimg.setImageBitmap(imgtostore);
            }
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void selectimage(){
        try {
            final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Image");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    if (items[i].equals("Camera")) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, REQUEST_CAMERA);
                        }

                    }
                    else if (items[i].equals("Gallery")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);

                    }
                    else if (items[i].equals("Cancel")) {
                        dialog.dismiss();

                    }
                }
            });
            builder.show();
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
