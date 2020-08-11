package com.example.sidechef.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sidechef.Model.UserApi;
import com.example.sidechef.R;
import com.example.sidechef.Recipe_Insert;
import com.example.sidechef.ui.HomePageRecyclerView.HomePage_RecyclerView_Fragment;
import com.example.sidechef.ui.gallery.GalleryFragment;
import com.example.sidechef.ui.UserRecipes.UserRecipesFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class ActivityHomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    private static String profile_username;

    private SwipeRefreshLayout swipelayout;
    private DrawerLayout drawer;
    private TextView profile_name;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//        Get detail for profile details
        View header = navigationView.getHeaderView(0);
        profile_name = header.findViewById(R.id.profile_username);
        profile_username = UserApi.getInstance().getUsername();

//        Select navigation drawer
        navigationView.setNavigationItemSelectedListener(this);

//        SwipeRefreshLayout
//        swipelayout = findViewById(R.id.swipecontainer);
//        swipelayout.setColorSchemeColors(Color.BLUE, Color.YELLOW, Color.GREEN);
//
//        swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipelayout.setRefreshing(true);
//                (new Handler()).postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(Home_Page.this, "Refreshed", Toast.LENGTH_SHORT).show();
//                        swipelayout.setRefreshing(false);
//                    }
//                },3000);
//            }
//        });

//        Version info
//        try {
//            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
//            String version = pInfo.versionName;
//            Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu, menu);

        MenuItem searchitem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchitem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchtxt) {
                Bundle bundle = new Bundle();

                String query = "SELECT * from Recipe where RecName LIKE '%" + searchtxt + "%'";

                bundle.putString("search_query", query);
                HomePage_RecyclerView_Fragment fragobj = new HomePage_RecyclerView_Fragment();
                fragobj.setArguments(bundle);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home, fragobj).commit();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        profile_name.setText(profile_username);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home, new HomePage_RecyclerView_Fragment()).commit();
        navigationView.setCheckedItem(R.id.nav_home);
    }

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

        switch (item.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home, new HomePage_RecyclerView_Fragment()).commit();
                break;

            case R.id.new_recipe_button:
                if(user != null && firebaseAuth != null) {
                    Intent insert_recipe = new Intent(ActivityHomePage.this, Recipe_Insert.class);
                    startActivity(insert_recipe);
                }
                    break;

            case R.id.nav_gallery:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home, new GalleryFragment()).commit();
                break;

            case R.id.nav_myrecipe:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home, new UserRecipesFragment()).commit();
                break;

            case R.id.logoutbutton:
                if(user != null && firebaseAuth != null) {
                    firebaseAuth.signOut();
                    Intent logout = new Intent(this, ActivityLogin.class);
                    startActivity(logout);
                    Toast.makeText(this, "You have been logged out", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}