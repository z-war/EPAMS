package com.example.epams;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.epams.Fragments.AdminAddEventHall;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminActivity extends AppCompatActivity {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private View header_view;
    private FirebaseFirestore db;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    Fragment admin_add_marquee;
    private ImageView profile_pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        init();
    }
    private void init()
    {
        try {
            admin_add_marquee = new AdminAddEventHall();
            navigationView = findViewById(R.id.design_navigation_view);
            drawerLayout = findViewById(R.id.drawer_layout);
            header_view = navigationView.getHeaderView(0);
            db = FirebaseFirestore.getInstance();
            toolbar = findViewById(R.id.toolbar);
            setuphamburgericon();
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    if(item.getItemId()==R.id.add_marquee){
                        changefragment(admin_add_marquee);
                        closedrawer();
                        return true;
                    }
                    return false;
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void closedrawer() {
        try {
            drawerLayout.closeDrawer(GravityCompat.START);
        } catch (Exception e) {
            Toast.makeText(this, "Error Closing Drawer" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void setuphamburgericon() {
        try {
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, (R.string.open), (R.string.close));
            actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPrimaryDark));
            actionBarDrawerToggle.syncState();
        } catch (Exception e) {
            Toast.makeText(this, "Error Setting Up Hamburger ICon" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void changefragment(Fragment obj){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder , obj).commit();
    }
}
