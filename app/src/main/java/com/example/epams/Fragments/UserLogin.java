package com.example.epams.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.epams.R;
import com.example.epams.UserActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class UserLogin extends Fragment {
    View frag;
    EditText  email , pass;
    Button btn_login;
    private TextView tosignup;
    Fragment signup_user;
    private FirebaseAuth auth;
    public UserLogin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        frag = inflater.inflate(R.layout.fragment_user_login, container, false);
        init();
        return frag;
    }

    private void init()
    {
        try {
            auth = FirebaseAuth.getInstance();
            signup_user = new user_signup();
            email = frag.findViewById(R.id.user_emailET);
            pass = frag.findViewById(R.id.user_passET);
            btn_login = frag.findViewById(R.id.loginBTN);
            tosignup = frag.findViewById(R.id.tosignupTV);
            String text = "<font color='black'>Not a User Please </font><font color='red'> Sign Up!</font>";
            tosignup.setText(Html.fromHtml(text) , TextView.BufferType.SPANNABLE);
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login();
                }
            });
            tosignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().beginTransaction().replace(R.id.fragment_holder_login , signup_user).commit();
                }
            });
        }catch (Exception e)

        {
            Toast.makeText(getContext(), "Error init "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void login()
    {
        try {
            if(!email.getText().toString().isEmpty() && !pass.getText().toString().isEmpty())
            {
                auth.signOut();
                auth.signInWithEmailAndPassword(email.getText().toString() , pass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getContext(), "Sign In Successfull", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                        startActivity(new Intent(getActivity(), UserActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }else if(email.getText().toString().isEmpty())
            {
                Toast.makeText(getContext(), "Email can not be emoty ", Toast.LENGTH_SHORT).show();
                email.requestFocus();
            }else if(pass.getText().toString().isEmpty())
            {
                Toast.makeText(getContext(), "Password can not be empty ", Toast.LENGTH_SHORT).show();
                pass.requestFocus();
            }
        }catch (Exception e)
        {
            Toast.makeText(getContext(), "Error Logging in user "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
