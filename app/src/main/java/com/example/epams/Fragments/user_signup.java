package com.example.epams.Fragments;

import android.app.Dialog;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class user_signup extends Fragment {

    private View frag;
    private EditText firstName, lastName, email, password;
    private Button buttonSignup;
    private TextView tollogin;
    private Fragment login;
    FirebaseFirestore db;
    private FirebaseAuth auth;
    private Dialog dialog;

    public user_signup() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        frag = inflater.inflate(R.layout.fragment_user_signup, container, false);

        init();

        return frag;
    }

    public void init() {
        try {
            db = FirebaseFirestore.getInstance();

            auth = FirebaseAuth.getInstance();
            dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.please_wait);
            dialog.setCancelable(false);
            firstName = frag.findViewById(R.id.firstNameET);
            lastName = frag.findViewById(R.id.lastNameET);
            email = frag.findViewById(R.id.emailET);
            password = frag.findViewById(R.id.passwordET);
            buttonSignup = frag.findViewById(R.id.signupBTN);
            tollogin = frag.findViewById(R.id.tologinactivity);
            login = new UserLogin();
            buttonSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                    Signup();
                }
            });
            String text = "<font color='black'>Already a User ? </font><font color='red' > Sign In !</font>";
            tollogin.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
            tollogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getFragmentManager().beginTransaction().replace(R.id.fragment_holder_login, login).commit();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error init signup " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void Signup() {
        try {
            auth.signOut();
            if (!firstName.getText().toString().isEmpty() && !lastName.getText().toString().isEmpty() &&
                    !email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            final Map<String , Object> datamap = new HashMap<>();
                            datamap.put("firstName" , firstName.getText().toString());
                            datamap.put("lastName" , lastName.getText().toString());
                            datamap.put("password" , password.getText().toString());
                            datamap.put("imageuri" ,"" );
                            datamap.put("phone", "");
                            db.collection("Users").document(email.getText().toString()).set(datamap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    dialog.dismiss();
                                    Toast.makeText(getContext(), "Sign Up Successfull", Toast.LENGTH_SHORT).show();
                                   getActivity().finish();
                                   startActivity(new Intent(getActivity(),UserActivity.class));

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss();
                                    Toast.makeText(getContext(), "Error Signing Up User "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            dialog.dismiss();
                            Toast.makeText(getContext(), "" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else if (firstName.getText().toString().isEmpty()) {
                dialog.dismiss();
                firstName.requestFocus();
                Toast.makeText(getContext(), "Please Enter First Name ", Toast.LENGTH_SHORT).show();
            } else if (lastName.getText().toString().isEmpty()) {
                dialog.dismiss();
                lastName.requestFocus();
                Toast.makeText(getContext(), "Please Enter Last Name ", Toast.LENGTH_SHORT).show();
            } else if (password.getText().toString().isEmpty()) {
                dialog.dismiss();
                password.requestFocus();
                Toast.makeText(getContext(), "Please Enter Password ", Toast.LENGTH_SHORT).show();
            } else if (email.getText().toString().isEmpty()) {
                dialog.dismiss();
                email.requestFocus();
                Toast.makeText(getContext(), "Please Enter Email ", Toast.LENGTH_SHORT).show();
            } else {
                dialog.dismiss();
                Toast.makeText(getContext(), "Error in Signup", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            dialog.dismiss();
            Toast.makeText(getContext(), "Error in Signup : " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }



}
