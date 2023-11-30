package com.example.pantrypal.ui;

import android.os.Bundle;

import com.example.pantrypal.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.FrameLayout;
import android.util.Log;
import android.widget.TextView;

import androidx.core.view.GravityCompat;

import android.view.MenuItem;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);



        TextView textViewMain = findViewById(R.id.textview_main);

        // Set up the navigation drawer toggle button in our toolbar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton cameraButton = (FloatingActionButton) findViewById(R.id.CameraFAB);

        // Set a listener for navigation item selection
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // Handle navigation item selection here
                int itemId = menuItem.getItemId();

                // Destroy the text you see when you first launch the app
                FrameLayout fragmentContainer = findViewById(R.id.fragment_container);
                fragmentContainer.removeView(textViewMain);

                if (itemId == R.id.nav_pantry) {
                    Log.d("PantryPalDebug", "Pantry was clicked");
                    cameraButton.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new PantryFragment())
                            .commit();

                } else if (itemId == R.id.nav_recipe) {
                    Log.d("PantryPalDebug", "Recipe was clicked");
                    cameraButton.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new RecipeFragment())
                            .commit();
                }

                // Close the drawer
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // OnClickListener for the FAB
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("PantryPalDebug", "FAB was clicked");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new CameraFragment())
                        .commit();
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle the action bar toggle button
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
