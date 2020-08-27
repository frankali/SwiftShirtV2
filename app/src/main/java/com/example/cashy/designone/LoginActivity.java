package com.example.cashy.designone;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;

public class LoginActivity extends AppCompatActivity {

    FrameLayout containerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        containerFragment = findViewById(R.id.HomePageContainer);

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.HomePageContainer, new loginFragment());
                fragmentTransaction.commit();
                // Stuff that updates the UI



    }




    public void Signup(View view) {

        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }


}
