package com.example.sidechef.ui.UserRecipes;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.example.sidechef.Model.Food;
import com.example.sidechef.Model.UserApi;
import com.example.sidechef.R;
import com.example.sidechef.Recipe_Display;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter_UserRecipes extends RecyclerView.Adapter<RecyclerViewAdapter_UserRecipes.ViewHolder> {

    private Context context;
    private List<Food> foodList;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Recipe");
    private StorageReference storageReference;


    public RecyclerViewAdapter_UserRecipes(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter_UserRecipes.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_recipe, parent, false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter_UserRecipes.ViewHolder holder, int position) {
        Food food = foodList.get(position);
        String imgUrl;
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        holder.txtname.setText(food.getName());

        imgUrl = food.getImageUrl();
        Picasso.get().load(imgUrl).placeholder(circularProgressDrawable).fit().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView txtname;
        public ImageView imageView;
        public String recipeid, recipeuserid;
        public ImageButton updatebutton;
        public ImageButton deletebutton;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            itemView.setOnClickListener(this);
            txtname = itemView.findViewById(R.id.UserRecipe_Name);
            imageView = itemView.findViewById(R.id.UserRecipe_Image);
            updatebutton = itemView.findViewById(R.id.recupdatebutton);
            deletebutton = itemView.findViewById(R.id.recdeletebutton);

            updatebutton.setOnClickListener(this);
            deletebutton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.recupdatebutton:
                    Toast.makeText(context, "update", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.recdeletebutton:
                    delete_recipe();
                    break;

                default:
                    recipeid = foodList.get(getAdapterPosition()).getId();

                    Intent intent = new Intent(context, Recipe_Display.class);
                    intent.putExtra("RecipeId", recipeid);
                    context.startActivity(intent);
            }
        }

        private void delete_recipe(){
            recipeid = foodList.get(getAdapterPosition()).getId();
            recipeuserid = foodList.get(getAdapterPosition()).getUserid();
            builder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.delete_confirmation_popup, null);
            Button nobtn = view.findViewById(R.id.cnf_no_btn);
            Button yesbtn =  view.findViewById(R.id.cnf_yes_btn);

            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            yesbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recipeuserid.equals(UserApi.getInstance().getUserId())){
                        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(foodList.get(getAdapterPosition()).getImageUrl());
                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("Adapter_User", e.toString());

                                    }
                                });

                        collectionReference.document(recipeid).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        foodList.remove(getAdapterPosition());
                                        notifyItemRemoved(getAdapterPosition());
                                        Toast.makeText(context, "Recipe successfully deleted", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Error deleting recipe", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                    dialog.dismiss();
                }
            });
            nobtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    }
}