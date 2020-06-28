package com.example.sidechef;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sidechef.Data.DataBaseHelper;
import com.example.sidechef.ui.HomePage_RecyclerView_Fragment;
import com.example.sidechef.ui.gallery.GalleryFragment;
import com.example.sidechef.ui.slideshow.SlideshowFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.Objects;

public class Home_Page extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    private static String profile_username;

    private SwipeRefreshLayout swipelayout;
    private DrawerLayout drawer;
    private TextView profile_name;
//    private String query = "SELECT * from Recipe ORDER by " + DataBaseHelper.reccol_9 + " DESC";

    DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//        Get Intent for profile details
        View header = navigationView.getHeaderView(0);
        profile_name = header.findViewById(R.id.profile_username);

        Intent intent = getIntent();
        if ( Objects.equals(intent.getStringExtra("Origin_Activity"), "Main_Activity") || Objects.equals(intent.getStringExtra("Origin_Activity"), "Register_Activity")){
            profile_username = intent.getStringExtra("profile_name");
        }

//        Select navigation drawer
        navigationView.setNavigationItemSelectedListener(this);

//        SwipeRefreshLayout
        swipelayout = findViewById(R.id.swipecontainer);
        swipelayout.setColorSchemeColors(Color.BLUE, Color.YELLOW, Color.GREEN);

        swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipelayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Home_Page.this, "Refreshed", Toast.LENGTH_SHORT).show();
                        swipelayout.setRefreshing(false);
                    }
                },3000);
            }
        });

//        Database
        db = new DataBaseHelper(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);

        MenuItem searchitem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchitem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(Home_Page.this, query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        profile_name.setText(profile_username);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home, new HomePage_RecyclerView_Fragment()).commit();
        navigationView.setCheckedItem(R.id.nav_home);
    }

    //    When item selected in appbar
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.logoutbutton:
//                Intent logout = new Intent(this, MainActivity.class);
//                startActivity(logout);
//                Toast.makeText(this, "You have been logged out", Toast.LENGTH_SHORT).show();
//                finish();
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);

        int item_id = item.getItemId();
        switch (item_id){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home, new HomePage_RecyclerView_Fragment()).commit();
                break;

            case R.id.new_recipe_button:
                Intent insert_recipe = new Intent(Home_Page.this, Recipe_Insert.class);
                startActivity(insert_recipe);
                break;

            case R.id.nav_gallery:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home, new GalleryFragment()).commit();
                break;

            case R.id.nav_slideshow:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home, new SlideshowFragment()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}