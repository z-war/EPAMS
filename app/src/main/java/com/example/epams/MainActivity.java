package com.example.epams;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.epams.Fragments.AdminLogin;
import com.example.epams.Fragments.Guest;
import com.example.epams.Fragments.UserLogin;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bnv;
    Fragment admin_login , user_login , guest_user;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    void init()
    {
        try {
            bnv = findViewById(R.id.bottom_menu);
            admin_login = new AdminLogin();
            user_login  = new UserLogin();
            guest_user = new Guest();
            change_fragment(admin_login);
//            if(getIntent().getExtras()!=null)
//            {
//
//                intent = getIntent();
//                String status = intent.getStringExtra("status");
//                if(status.equals("user_login"))
//                {
//                    bnv.setSelectedItemId(R.id.user_login);
//                    change_fragment(user_login);
//                }
//
//            }
            bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if(item.getItemId()==R.id.admin_login)
                    {
                        Toast.makeText(MainActivity.this, "AdminLogin", Toast.LENGTH_SHORT).show();
                        change_fragment(admin_login);
                        return true;
                    }
                    else if(item.getItemId()==R.id.user_login)
                    {
                        change_fragment(user_login);
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

    void change_fragment(Fragment obj)
    {
        try {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder_login,obj).commit();
        }catch (Exception e)
        {
            Toast.makeText(this, "Error Changing Fragment", Toast.LENGTH_SHORT).show();
        }
    }
}
