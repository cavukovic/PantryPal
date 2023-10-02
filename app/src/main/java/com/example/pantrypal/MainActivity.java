package com.example.pantrypal;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Spinner;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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


//public class MainActivity extends AppCompatActivity {
//
//    private AppBarConfiguration appBarConfiguration;
//    private ActivityMainBinding binding;
//    private Spinner spinner;
//    private ArrayAdapter<CharSequence> adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        // Initialize the Spinner
//        spinner = findViewById(R.id.my_spinner);
//        adapter = ArrayAdapter.createFromResource(this, R.array.spinner_options, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//
//        // Set the initial selection (might want to change this later)
//        spinner.setSelection(0);
//
//        // Handle spinner item selection
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                String selectedOption = adapter.getItem(position).toString();
//                handleToolbarOptionSelected(selectedOption);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // Handle the case where nothing is selected
//            }
//        });
//
//        setSupportActionBar(binding.toolbar);
//
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//
//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // open the barcode scanner here
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAnchorView(R.id.fab)
//                        .setAction("Action", null).show();
//            }
//        });
//    }
//
//    private void handleToolbarOptionSelected(String selectedOption) {
//        // Replace this with your logic for handling "Pantry" and "Recipe" options
//        if ("Pantry".equals(selectedOption)) {
//            // Handle "Pantry" option
//            Log.d("PantryPalDebug", "Pantry was clicked");
//
//        } else if ("Recipe".equals(selectedOption)) {
//            // Handle "Recipe" option
//            Log.d("PantryPalDebug", "Recipe was clicked");
//
//        }
//    }
//
////    @Override
////    public boolean onCreateOptionsMenu(Menu menu) {
////        // Inflate the menu; this adds items to the action bar if it is present.
////        getMenuInflater().inflate(R.menu.menu_main, menu);
////        return true;
////    }
////
////    @Override
////    public boolean onOptionsItemSelected(MenuItem item) {
////        // Handle action bar item clicks here. The action bar will
////        // automatically handle clicks on the Home/Up button, so long
////        // as you specify a parent activity in AndroidManifest.xml.
////        int id = item.getItemId();
////
////        //noinspection SimplifiableIfStatement
////        if (id == R.id.action_settings) {
////            return true;
////        }
////
////        return super.onOptionsItemSelected(item);
////    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
//}

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        //navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // Set up the navigation drawer toggle button in your toolbar
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

                if (itemId == R.id.nav_pantry) {
                    // Handle the "Pantry" item
                    Log.d("PantryPalDebug", "Pantry was clicked");
                    //navController.navigate(R.id.action_FirstFragment_to_SecondFragment);

                } else if (itemId == R.id.nav_recipe) {
                    // Handle the "Recipe" item
                    Log.d("PantryPalDebug", "Recipe was clicked");
                    //navController.navigate(R.id.SecondFragment);
                    //navController.navigate(R.id.action_SecondFragment_to_FirstFragment);
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
