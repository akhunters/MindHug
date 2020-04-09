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
import android.widget.Toast;

import com.akhunters.mindhug.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class SignUp extends AppCompatActivity {

    ActivitySignUpBinding binding;

    private FirebaseAuth mAuth;
    DatabaseReference reference;

    TextInputLayout nameLayout;
    TextInputLayout emailLayout;
    TextInputLayout passwordLayout;

    CircularProgressButton signUp;

    MaterialTextView signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        nameLayout = binding.nameLayout;
        emailLayout = binding.emailLayout;
        passwordLayout = binding.passwordLayout;
        signUp = binding.signUp;
        signIn = binding.signIn;

        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp.startAnimation();
                registerUser();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public Boolean validateName(String name){
        if(name.isEmpty()){
            nameLayout.setError("Field cannot be empty");
            nameLayout.requestFocus();
            return false;
        }
        else{
            nameLayout.setError(null);
            return true;
        }
    }

    public Boolean validateEmail(String email){
        if(email.isEmpty()){
            emailLayout.setError("Field cannot be empty");
            emailLayout.requestFocus();
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailLayout.setError("Invalid email address");
            emailLayout.requestFocus();
            return false;
        }
        else{
            emailLayout.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(String password){
        if(password.isEmpty()){
            passwordLayout.setError("Field cannot be empty");
            passwordLayout.requestFocus();
            return false;
        }
        else if(password.length()<6){
            passwordLayout.setError("Password is too short");
            passwordLayout.requestFocus();
            return false;
        }
        else{
            passwordLayout.setError(null);
            return true;
        }
    }

    public void registerUser(){
        final String name = nameLayout.getEditText().getText().toString().trim();
        final String email = emailLayout.getEditText().getText().toString().trim();
        final String password = passwordLayout.getEditText().getText().toString().trim();
        final String[] userUID = new String[1];

        if(!validatePassword(password) | !validateEmail(email) | !validateName(name)){
            signUp.revertAnimation();
            signUp.setBackgroundResource(R.drawable.welcome_signup_background);
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Snackbar.make(findViewById(R.id.mainContent), "User successfully created", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    FirebaseUser newUser = mAuth.getCurrentUser();



                    User user = new User();
                    user.userEmail = email;
                    user.userPassword = password;
                    user.userName = name;
                    user.userUID = newUser.getUid();
                    user.userProfileUrl = "empty";

                    reference.child(newUser.getUid()).setValue(user);
                    Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp);
                    signUp.doneLoadingAnimation(ContextCompat.getColor(SignUp.this, android.R.color.black),icon);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            SignIn.activitySignIn.finish();
                            finish();
                        }
                    }, 1000);

                }
                else{
                    Snackbar.make(findViewById(R.id.mainContent), task.getException().getMessage(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    signUp.revertAnimation();
                    signUp.setBackgroundResource(R.drawable.welcome_signup_background);
                    return;
                }
            }
        });




    }

}
