package com.example.epams.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.epams.AdminActivity;
import com.example.epams.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.ExecutionException;


public class AdminLogin extends Fragment {
    View frag;
    EditText email, password;
    Button btn;
    FirebaseFirestore db;

    public AdminLogin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        frag = inflater.inflate(R.layout.fragment_admin_login, container, false);
        init();
        return frag;
    }

    private void init() {
        try {
            email = frag.findViewById(R.id.admin_emailET);
            password = frag.findViewById(R.id.admin_passET);
            btn = frag.findViewById(R.id.loginBTN);
            db = FirebaseFirestore.getInstance();
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adminLogin();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void adminLogin() {
        try {
            if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
               db.collection("Admin").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful()) {
                           for (QueryDocumentSnapshot document : task.getResult()) {
                               if(document.get("email").equals(email.getText().toString()) && document.get("password").equals(password.getText().toString()))
                               {
                                   Toast.makeText(getContext(), "Admin Logged In", Toast.LENGTH_SHORT).show();
                                   getActivity().finish();
                                   startActivity(new Intent(getContext(), AdminActivity.class));
                               }
                               else
                               {
                                   Toast.makeText(getContext(), "Please Enter Correct Credentials", Toast.LENGTH_SHORT).show();
                               }
                           }
                       } else {
                           Toast.makeText(getContext(), "login nai hoa"+task.getException(), Toast.LENGTH_SHORT).show();
                       }
                   }
               });
            }else if(email.getText().toString().isEmpty())
            {
                Toast.makeText(getContext(), "Please Enter email", Toast.LENGTH_SHORT).show();
                email.requestFocus();
            }else  if (password.getText().toString().isEmpty())
            {
                Toast.makeText(getContext(), "Please Enter password", Toast.LENGTH_SHORT).show();
                password.requestFocus();

            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error Login : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
