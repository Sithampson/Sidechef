package com.example.sidechef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
public class Home_Page extends AppCompatActivity{
    protected GridView gridView;
    private String name;
    private String meal;
    private String cuisine;
    private String descrip;
    private String ingrd;
    private String proc;
    private byte[] image;

    ArrayList<Food> list;
    FoodListAdapter adapter = null;

    DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__page);

//        Set up a appbar
        Toolbar toolbar = findViewById(R.id.toobar);
        setSupportActionBar(toolbar);

//        Database
        db = new DataBaseHelper(this);

//        Calling gridview
        viewImage();

    }

//    Create appbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }

//    When item selected in appbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.refbutton:
                viewImage();
                Toast.makeText(this, "Refreshed", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.logoutbutton:
                Intent logout = new Intent(this, MainActivity.class);
                startActivity(logout);
                Toast.makeText(this, "You have been logged out", Toast.LENGTH_SHORT).show();
                finish();
                return true;

            case R.id.new_recipe_button:
                Intent insert_recipe = new Intent(Home_Page.this, Recipe_Insert.class);
                startActivity(insert_recipe);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //    Display items in GridView
    public void viewImage(){
        try {
            gridView = findViewById(R.id.imggridview);
            list = new ArrayList<>();
            adapter = new FoodListAdapter(Home_Page.this, R.layout.food_items, list);
            gridView.setAdapter(adapter);

            Cursor cursor = db.getdata("SELECT * from Recipe");
            list.clear();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                byte[] image = cursor.getBlob(7);

                list.add(new Food(id, name, image));

            }
            adapter.notifyDataSetChanged();

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Cursor c = db.getdata("SELECT RecID from Recipe");
                    ArrayList<Integer> arrid = new ArrayList<>();
                    while (c.moveToNext()){
                        arrid.add(c.getInt(0));
                    }
                    int recid = arrid.get(position);
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
                    try {
                        db.update_viewcount(recid);

                        Intent intent = new Intent(Home_Page.this, Recipe_Display.class);
                        intent.putExtra("Name", name);
                        intent.putExtra("Meal", meal);
                        intent.putExtra("Cuisine", cuisine);
                        intent.putExtra("Description", descrip);
                        intent.putExtra("Ingredient", ingrd);
                        intent.putExtra("Procedure", proc);
                        intent.putExtra("Image", image);

                        startActivity(intent);
                    }
                    catch (Exception e){
                        Toast.makeText(Home_Page.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        catch (Exception e){
            Toast.makeText(Home_Page.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
