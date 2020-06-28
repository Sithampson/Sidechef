package com.example.sidechef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Recipe_Display extends AppCompatActivity {

    TextView name;
    TextView meal;
    TextView cuisine;
    TextView description;
    TextView ingredient;
    TextView procedure;
    ImageView image;
    byte[] imagetoshow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe__display);

        name = findViewById(R.id.nametext);
        meal = findViewById(R.id.typetext);
        cuisine = findViewById(R.id.cuisinetext);
        description = findViewById(R.id.descriptiontext);
        ingredient = findViewById(R.id.ingredientstext);
        procedure = findViewById(R.id.proceduretext);
        image = findViewById(R.id.imagetext);

        Intent intent = getIntent();
        name.setText(intent.getStringExtra("Name"));
        meal.setText(intent.getStringExtra("Meal"));
        cuisine.setText(intent.getStringExtra("Cuisine"));
        description.setText(intent.getStringExtra("Description"));
        ingredient.setText(intent.getStringExtra("Ingredient"));
        procedure.setText(intent.getStringExtra("Procedure"));
        imagetoshow = intent.getByteArrayExtra("Image");


        try {
            assert imagetoshow != null;
            Bitmap bitmap = BitmapFactory.decodeByteArray(imagetoshow, 0, imagetoshow.length);
            image.setImageBitmap(bitmap);
        }
        catch (Exception e){
            Toast.makeText(Recipe_Display.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
}
