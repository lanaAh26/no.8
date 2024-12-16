package com.example.no8;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    public static FrameLayout fhome,fDash,fLogin,fSignup;
    private HomeFrag homeFrag;
    private DashFrag dashFrag;
    private LoginFrag loginFrag;
    private SignUpFrag signUpFrag;
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;


    public static FloatingActionButton btnLogout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fhome=findViewById(R.id.fHome);
        fDash=findViewById(R.id.fDash);
        fLogin=findViewById(R.id.fLogin);
        fSignup=findViewById(R.id.fSignup);
        bottomNavigationView=findViewById(R.id.bottom);

        btnLogout=findViewById(R.id.btn_logOut);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });

        startFragment();


    }

    private void startFragment(){

        homeFrag=new HomeFrag();
        dashFrag=new DashFrag();
        loginFrag=new LoginFrag();
        signUpFrag=new SignUpFrag();

        getSupportFragmentManager().beginTransaction().replace(R.id.fHome,homeFrag).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fDash,dashFrag).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fLogin,loginFrag).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fSignup,signUpFrag).commit();

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser =mAuth.getCurrentUser();
        if (currentUser!=null) {
            LoginFrag.UpdateUI();}
        else {
            fhome.setVisibility(View.INVISIBLE);
            fDash.setVisibility(View.INVISIBLE);
            fLogin.setVisibility(View.VISIBLE);
            fSignup.setVisibility(View.INVISIBLE);
            btnLogout.hide();}


        //set up navigation
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId()==R.id.mHome && loginFrag.loged){
                    fhome.setVisibility(View.VISIBLE);
                    fDash.setVisibility(View.INVISIBLE);
                    fLogin.setVisibility(View.INVISIBLE);
                    fSignup.setVisibility(View.INVISIBLE);
                }

                if (item.getItemId()==R.id.mLogin && !loginFrag.loged){
                    fhome.setVisibility(View.INVISIBLE);
                    fDash.setVisibility(View.INVISIBLE);
                    fLogin.setVisibility(View.VISIBLE);
                    fSignup.setVisibility(View.INVISIBLE);
                }

                if (item.getItemId()==R.id.mDash && loginFrag.loged){
                    fhome.setVisibility(View.INVISIBLE);
                    fDash.setVisibility(View.VISIBLE);
                    fLogin.setVisibility(View.INVISIBLE);
                    fSignup.setVisibility(View.INVISIBLE);
                }

                return true;
            }
        });

    }

    private void logOut(){
        FirebaseAuth.getInstance().signOut();
        LoginFrag.loged=false;
        fhome.setVisibility(View.INVISIBLE);
        fLogin.setVisibility(View.VISIBLE);
        fDash.setVisibility(View.INVISIBLE);
        fSignup.setVisibility(View.INVISIBLE);
        btnLogout.hide();
    }
}