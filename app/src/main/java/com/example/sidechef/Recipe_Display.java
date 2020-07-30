package com.example.sidechef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class Recipe_Display extends AppCompatActivity implements View.OnClickListener {

    TextView name;
    TextView meal;
    TextView cuisine;
    TextView description;
    TextView ingredient;
    TextView procedure;
    ImageView image;
    TextView DateTime;
    Button updatebtn;
    Button deletebtn;
    String recid;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Recipe");


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
        DateTime = findViewById(R.id.DateTimeText);
        updatebtn = findViewById(R.id.recupdatebutton);
        deletebtn = findViewById(R.id.recdeletebutton);

        final CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        Intent intent = getIntent();
        recid = intent.getStringExtra("RecipeId");

        assert recid != null;
        collectionReference.document(recid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    assert documentSnapshot != null;
                    if(documentSnapshot.exists()){
                        String imgUrl;

                        name.setText(documentSnapshot.getString("recipeName"));
                        meal.setText(documentSnapshot.getString("mealType"));
                        cuisine.setText(documentSnapshot.getString("cuisine"));
                        description.setText(documentSnapshot.getString("recipeDescription"));
                        ingredient.setText(documentSnapshot.getString("ingredients"));
                        procedure.setText(documentSnapshot.getString("recipeProcedure"));

                        imgUrl = documentSnapshot.getString("imageUrl");
                        Picasso.get().load(imgUrl).placeholder(circularProgressDrawable).fit().into(image);

                        Timestamp timestamp = documentSnapshot.getTimestamp("timestamp");
                        assert timestamp != null;
                        DateTime.setText(timestamp.toDate().toLocaleString());

                    }

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Display", e.toString());

            }
        });

        updatebtn.setOnClickListener(this);
        deletebtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.recupdatebutton:
                //TODO: Update recipe
                break;
            case R.id.recdeletebutton:
                LayoutInflater inflater;

                builder = new AlertDialog.Builder(this);
                inflater = LayoutInflater.from(this);
                View view = inflater.inflate(R.layout.delete_confirmation_popup, null);
                Button nobtn = view.findViewById(R.id.cnf_no_btn);
                Button yesbtn =  view.findViewById(R.id.cnf_yes_btn);

                builder.setView(view);
                dialog = builder.create();
                dialog.show();

                yesbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        finish();
                    }
                });
                nobtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                break;
        }
    }
}
