package com.example.epams;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.epams.Fragments.edit_profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserActivity extends AppCompatActivity {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private View header_view;
    private TextView user_email, user_name;
    private FirebaseAuth muath;
    private FirebaseUser curUser;
    private FirebaseFirestore db;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private ImageView profile_pic;
    private Fragment editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        init();
    }

    private void init() {
        try {
            editProfile = new edit_profile();
            navigationView = findViewById(R.id.design_navigation_view);
            drawerLayout = findViewById(R.id.drawer_layout);
            header_view = navigationView.getHeaderView(0);
            user_email = header_view.findViewById(R.id.user_emailTV_header);
            user_name = header_view.findViewById(R.id.user_nameTV_header);
            profile_pic = header_view.findViewById(R.id.user_profile_picIV_header);
            muath = FirebaseAuth.getInstance();
            curUser = muath.getCurrentUser();
            db = FirebaseFirestore.getInstance();
            toolbar = findViewById(R.id.toolbar);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.user_edit_profile) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder, editProfile).commit();
                        closedrawer();
                        return true;

                    } else if (item.getItemId() == R.id.user_home) {
                        closedrawer();
                        finish();
                        startActivity(new Intent(getBaseContext(), UserActivity.class));

                    }else if (item.getItemId()==R.id.logout)
                    {
                        muath.signOut();
                        finish();
                        startActivity(new Intent(getBaseContext(),MainActivity.class).putExtra("status","user_login"));
                    }
                    return false;
                }
            });
            setuphamburgericon();
            setcurrentuserdetails();
        } catch (Exception e) {
            Toast.makeText(this, "Error Init" + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void setcurrentuserdetails() {
        try {
            curUser = muath.getCurrentUser();
            user_email.setText(curUser.getEmail());
            DocumentReference ref = db.collection("Users").document(curUser.getEmail());
            ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {

                        DocumentSnapshot doc = task.getResult();
                        String name = doc.getString("firstName");
                        name += " ";
                        name += doc.getString("lastName");
                        user_name.setText(name);
                        if (!doc.getString("imageuri").isEmpty()) {
                            Glide.with(getApplicationContext()).load(doc.getString("imageuri")).into(profile_pic);
                        }
                    }
                }
            }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Toast.makeText(UserActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Setting User Details " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void closedrawer() {
        try {
            drawerLayout.closeDrawer(GravityCompat.START);
        } catch (Exception e) {
            Toast.makeText(this, "Error Closing Drawer" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
