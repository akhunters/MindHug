package com.akhunters.mindhug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;

import com.akhunters.mindhug.databinding.ActivitySigninBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;


public class SignIn extends AppCompatActivity {

    ActivitySigninBinding binding;

    public static SignIn activitySignIn;

    TextInputLayout emailLayout;
    TextInputLayout passwordLayout;

    CircularProgressButton signIn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        activitySignIn =this;

        mAuth = FirebaseAuth.getInstance();

        emailLayout = binding.emailLayout;
        passwordLayout = binding.passwordLayout;
        signIn = binding.signIn;
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn.startAnimation();
                signInUser();
            }
        });

        binding.forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn.startAnimation();
                forgotPassword();
            }
        });

        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUp.class));
            }
        });
    }

    public void forgotPassword() {

        String email = emailLayout.getEditText().getText().toString().trim();
        passwordLayout.setError(null);
        if (email.isEmpty()) {
            emailLayout.setError("Enter email to forgot password");
            emailLayout.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Invalid email address");
            emailLayout.requestFocus();
            return;
        } else {
            emailLayout.setError(null);

            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Snackbar.make(findViewById(R.id.mainContent), "Password reset link sent to your email", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp);
                        signIn.doneLoadingAnimation(ContextCompat.getColor(SignIn.this, android.R.color.black), icon);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                signIn.revertAnimation();
                                signIn.setBackgroundResource(R.drawable.welcome_signup_background);
                            }
                        }, 1000);

                        return;
                    } else {
                        Snackbar.make(findViewById(R.id.mainContent), task.getException().getMessage(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        signIn.revertAnimation();
                        signIn.setBackgroundResource(R.drawable.welcome_signup_background);
                        return;
                    }
                }
            });
        }

    }

    public Boolean validateEmail(String email) {
        if (email.isEmpty()) {
            emailLayout.setError("Field cannot be empty");
            emailLayout.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Invalid email address");
            emailLayout.requestFocus();
            return false;
        } else {
            emailLayout.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(String password) {
        if (password.isEmpty()) {
            passwordLayout.setError("Field cannot be empty");
            passwordLayout.requestFocus();
            return false;
        } else if (password.length() < 6) {
            passwordLayout.setError("Password is too short");
            passwordLayout.requestFocus();
            return false;
        } else {
            passwordLayout.setError(null);
            return true;
        }
    }


    public void signInUser() {

        String userEmail = emailLayout.getEditText().getText().toString().trim();
        String userPassword = passwordLayout.getEditText().getText().toString().trim();

        if (!validatePassword(userPassword) | !validateEmail(userEmail)) {
            signIn.revertAnimation();
            signIn.setBackgroundResource(R.drawable.welcome_signup_background);
            return;

        }


        mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(SignIn.this, "Logged in successfully", Toast.LENGTH_SHORT).show();

                    Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp);
                    signIn.doneLoadingAnimation(ContextCompat.getColor(SignIn.this, android.R.color.black), icon);
                    Snackbar.make(findViewById(R.id.mainContent), "Logged in successfully", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    }, 1000);


                } else {
                    signIn.revertAnimation();
                    signIn.setBackgroundResource(R.drawable.welcome_signup_background);
                    Snackbar.make(findViewById(R.id.mainContent), task.getException().getMessage(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }
}
