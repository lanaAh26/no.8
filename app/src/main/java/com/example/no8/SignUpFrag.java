package com.example.no8;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SignUpFrag extends Fragment {

    private TextInputEditText sEmail,sUser,sName,sphone,sPass,sPass2;
    private String email,pass,name,phone,user,pass2;
    private Button bSignup;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    public SignUpFrag() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        View view= inflater.inflate(R.layout.fragment_sign_up, container, false);
        bSignup=view.findViewById(R.id.SignUp);
        sEmail=view.findViewById(R.id.sEmail);
        sUser=view.findViewById(R.id.sUsername);
        sName=view.findViewById(R.id.sName);
        sphone=view.findViewById(R.id.sPhone);
        sPass=view.findViewById(R.id.sPass);
        sPass2=view.findViewById(R.id.sPass2);

        bSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intiStrings();
                if( name.isEmpty() || user.isEmpty() || email.isEmpty() || phone.isEmpty() || pass.isEmpty() || pass2.isEmpty() ) {
                    Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
                else
                    SignUp();
            }
        });


        return view;
    }

    public void SignUp(){
        intiStrings();

        if (!checkName(name)){
            Toast.makeText(getActivity(), "Name Can't Contain Numbers", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!checkUser(user)){
            Toast.makeText(getActivity(), "User Must Be Longer Than 6 Chars", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!(isValidEmail(email))){
            Toast.makeText(getActivity(), "Invalid Email", Toast.LENGTH_SHORT).show();
            sEmail.setError("Invalid Email");
            sEmail.requestFocus();
            return;
        }
        if (!checkphone(phone)){
            return;
        }
        if (!checkPass(pass,pass2)){
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Sign Up Success", Toast.LENGTH_SHORT).show();
                    LoginFrag.UpdateUI();
                    addData();
                }
                else
                    Toast.makeText(getActivity(), "Sign Up Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private static boolean isValidEmail(String email){
        return !(TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private boolean checkPass(String pass,String pass2){
        if (!pass.equals(pass2)){
            Toast.makeText(getActivity(), "Confirm Password Dosen't Match", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pass.length()<6){
            Toast.makeText(getActivity(), "Password Must Be At Least 6 Chars", Toast.LENGTH_SHORT).show();
            return false;
        }
       return true;

    }

    private boolean checkUser(String user){
        return user.length()>=7;
    }

    private boolean checkName(String name){
        char[] n=name.toCharArray();
        for (int i=0;i<n.length;i++){
            if (!Character.isUpperCase(n[i]) && !Character.isLowerCase(n[i])){
                return false;
            }
        }
        return true;
    }

    private boolean checkphone(String phone){
        char[] p=phone.toCharArray();
        for (int n=0;n<phone.length();n++){
            if (!Character.isDigit(p[n])){
                Toast.makeText(getActivity(), "Phone Must Be All Numbers", Toast.LENGTH_SHORT).show();
                return false;}
        }
        if (phone.length()!=10) {
            Toast.makeText(getActivity(), "Phone Must Be 10 Digits", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void intiStrings(){
        email=sEmail.getText().toString();
        user=sUser.getText().toString();
        name=sName.getText().toString();
        phone=sphone.getText().toString();
        pass=sPass.getText().toString();
        pass2=sPass2.getText().toString();
    }

    private void addData(){
        intiStrings();
        Map<String,Object>map;
        map=new HashMap<>();
        map.put("Name",name);
        map.put("Username",user);
        map.put("Email",email);
        map.put("Phone",phone);
        map.put("Password",pass);
        db.collection("users").document(user).set(map);
        Log.d("Tag","add user" +map);
    }
}