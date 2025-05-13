package com.example.mobilecomputingproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    ImageView profileImage;
    Button logoutBtn, loginBtn, signupBtn;
    EditText emailInput, passwordInput;
    TextView profileNameTxt, LoginTxt;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        profileImage = findViewById(R.id.profileImage);
        profileNameTxt = findViewById(R.id.profileNameTxt);
        LoginTxt = findViewById(R.id.LoginTxt);

        logoutBtn = findViewById(R.id.LogoutBtn);
        loginBtn = findViewById(R.id.loginBtn);
        signupBtn = findViewById(R.id.signupBtn);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        /// check if user is logged in
        if (currentUser != null) {
            emailInput.setVisibility(View.INVISIBLE);
            passwordInput.setVisibility(View.INVISIBLE);
            loginBtn.setVisibility(View.INVISIBLE);
            signupBtn.setVisibility(View.INVISIBLE);
            logoutBtn.setVisibility(View.VISIBLE);
            profileNameTxt.setVisibility(View.VISIBLE);
            profileNameTxt.setText(currentUser.getEmail());
            LoginTxt.setVisibility(View.INVISIBLE);
        }
        else {
            emailInput.setVisibility(View.VISIBLE);
            passwordInput.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.VISIBLE);
            signupBtn.setVisibility(View.VISIBLE);
            logoutBtn.setVisibility(View.INVISIBLE);
            profileNameTxt.setVisibility(View.INVISIBLE);
            LoginTxt.setVisibility(View.VISIBLE);
        }

        // login function
        loginBtn.setOnClickListener(v -> {
                    String email = emailInput.getText().toString().trim();
                    String password = passwordInput.getText().toString().trim();

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, task -> {
                                if (task.isSuccessful()) {
                                    // Log in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(ProfileActivity.this, "Log in successful.",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    // If log in fails, display a message to the user.
                                    Toast.makeText(ProfileActivity.this, "Log in failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
            });


            //logout function

        logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(ProfileActivity.this, "Logged out successfully.",
                    Toast.LENGTH_SHORT).show();
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             Intent intent = new Intent(ProfileActivity.this, SignupActivity.class);
                                             startActivity(intent);
                                         }
                                     });


                // navigation bar
                ImageButton home = findViewById(R.id.homeBtn);
        ImageButton profile = findViewById(R.id.profileBtn);
        ImageButton settings = findViewById(R.id.settingsBtn);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // end of navigation bar


    }

}
