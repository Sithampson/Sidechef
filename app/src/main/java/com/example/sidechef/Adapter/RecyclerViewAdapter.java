package com.example.sidechef.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sidechef.Data.DataBaseHelper;
import com.example.sidechef.Model.Food;
import com.example.sidechef.R;
import com.example.sidechef.Recipe_Display;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Food> foodList;


    public RecyclerViewAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food food = foodList.get(position);

        holder.txtname.setText(food.getName());

        byte[] foodimage = food.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(foodimage, 0, foodimage.length);
        holder.imageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView txtname;
        public ImageView imageView;

        private String name;
        private String meal;
        private String cuisine;
        private String descrip;
        private String ingrd;
        private String proc;
        private byte[] image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            txtname = itemView.findViewById(R.id.txtname);
            imageView = itemView.findViewById(R.id.imgFood);


        }

        @Override
        public void onClick(View v) {
            int recid = foodList.get(getAdapterPosition()).getId();

            DataBaseHelper db = new DataBaseHelper(context);

            Cursor cu = db.getdata("SELECT * from Recipe where RecID = "+recid);

            while(cu.moveToNext()) {
                name = cu.getString(1);
                meal = cu.getString(2);
                cuisine = cu.getString(3);
                descrip = cu.getString(4);
                ingrd = cu.getString(5);
                proc = cu.getString(6);
                image = cu.getBlob(7);
            }

            db.update_viewcount(recid);

            Intent intent = new Intent(context, Recipe_Display.class);
            intent.putExtra("Name", name);
            intent.putExtra("Meal", meal);
            intent.putExtra("Cuisine", cuisine);
            intent.putExtra("Description", descrip);
            intent.putExtra("Ingredient", ingrd);
            intent.putExtra("Procedure", proc);
            intent.putExtra("Image", image);

            context.startActivity(intent);

        }
    }
}
