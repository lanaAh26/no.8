package com.example.no8;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginFrag extends Fragment {

    private TextInputEditText etEmail,etPassword;
    private Button btnSubmit,signUp;
    private FirebaseAuth mAuth;
    public static boolean loged=false;


    public LoginFrag() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth= FirebaseAuth.getInstance();
        View view= inflater.inflate(R.layout.fragment_login, container, false);
        etEmail=view.findViewById(R.id.etEmail);
        etPassword=view.findViewById(R.id.etPass);
        btnSubmit=view.findViewById(R.id.submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmailPass();
            }
        });

        signUp=view.findViewById(R.id.signBtn);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!loged){
                    MainActivity.fhome.setVisibility(View.INVISIBLE);
                    MainActivity.fLogin.setVisibility(View.INVISIBLE);
                    MainActivity.fDash.setVisibility(View.INVISIBLE);
                    MainActivity.fSignup.setVisibility(View.VISIBLE);
                    MainActivity.btnLogout.hide();}
            }
        });
        return view;
    }

    public void checkEmailPass(){
        String email,pass;
        email=etEmail.getText().toString();
        pass=etPassword.getText().toString();

        if ( email.isEmpty() || pass.isEmpty() ){
            Toast.makeText(getActivity(), "Please Fill All Fields", Toast.LENGTH_SHORT).show();

        }
        else {mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                    UpdateUI();
                }
                else {Toast.makeText(getActivity(), "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        }

    }


    public static void UpdateUI(){
        loged=true;
        MainActivity.fhome.setVisibility(View.VISIBLE);
        MainActivity.fLogin.setVisibility(View.INVISIBLE);
        MainActivity.fDash.setVisibility(View.INVISIBLE);
        MainActivity.fSignup.setVisibility(View.INVISIBLE);
        MainActivity.btnLogout.show();

    }
}