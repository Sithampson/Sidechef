package com.example.sidechef.ui.HomePageRecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.example.sidechef.Model.Food;
import com.example.sidechef.R;
import com.example.sidechef.Recipe_Display;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter_HomePage extends RecyclerView.Adapter<RecyclerViewAdapter_HomePage.ViewHolder> {

    private Context context;
    private List<Food> foodList;


    public RecyclerViewAdapter_HomePage(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.food_items, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
        public String recipeid;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            itemView.setOnClickListener(this);
            txtname = itemView.findViewById(R.id.txtname);
            imageView = itemView.findViewById(R.id.imgFood);
        }

        @Override
        public void onClick(View v) {
            recipeid = foodList.get(getAdapterPosition()).getId();

            Intent intent = new Intent(context, Recipe_Display.class);
            intent.putExtra("RecipeId", recipeid);
            context.startActivity(intent);
        }
    }
}