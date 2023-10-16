package com.example.pantrypal;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.pantrypal.databinding.ActivityMainBinding;

import android.view.Menu;
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

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new FirstFragment())
                            .commit();

                } else if (itemId == R.id.nav_recipe) {
                    Log.d("PantryPalDebug", "Recipe was clicked");
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new SecondFragment())
                            .commit();
                }

                // Close the drawer
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);

        // OnClickListener for the FAB
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("PantryPalDebug", "FAB was clicked");
            }
            // we will want to have this pull up the camera, then the user can scan the barcode
            // then we will need to get the ingredient name and any other information
            // from the barcode and the add that to the list the same way we did in the
            // pantry fragment
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
