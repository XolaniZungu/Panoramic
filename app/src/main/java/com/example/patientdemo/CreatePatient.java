package com.example.patientdemo;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class  CreatePatient extends AppCompatActivity {

    TextInputLayout name,user_name,mobile_Number, email, password,confirmPassword;
    Button Register;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID, Client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_patient);

        name = findViewById(R.id.lname);
        user_name = findViewById(R.id.lusername);
        email = findViewById(R.id.lemail);
        mobile_Number = findViewById(R.id.lmobileNo);
        password = findViewById(R.id.lpassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        Register = findViewById(R.id.lgo);
        Client = getIntent().getStringExtra("Client");
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

//        if(fAuth.getCurrentUser() != null){
//            startActivity(new Intent(getApplicationContext(),MainActivity.class));
//            finish();
//        }

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }


    private Boolean validateName(){
        String val = name.getEditText().getText().toString();

        if(val.isEmpty()){
            name.setError("Field cannot be empty");
            return false;
        }
        else{
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername(){
        String val = user_name.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if(val.isEmpty()){
            user_name.setError("Field cannot be empty");
            return false;
        } else if(val.length()>=15) {
            user_name.setError("Username is too long");
            return false;
        }
        else if(!val.matches(noWhiteSpace)){
            user_name.setError("White Spaces are not allowed");
            return false;
        }
        else{
            user_name.setError(null);
            return true;
        }
    }

    private Boolean validateMobileNo(){
        String val = mobile_Number.getEditText().getText().toString();

        if(val.isEmpty()){
            mobile_Number.setError("Field cannot be empty");
            return false;
        }
        else{
            mobile_Number.setError(null);
            return true;
        }
    }
    private static final String coDomain = "dut4life.ac.za";
    private Boolean validateEmail(){
        String val = email.getEditText().getText().toString();
//        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+" + Pattern.quote(coDomain) + "$";
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + Pattern.quote(coDomain) + "$";

        if(val.isEmpty()){
            email.setError("Field cannot be empty");
            return false;
        } else if(!val.matches(emailPattern)){
            email.setError("Invalid email address");
            return false;
        }
        else{
            email.setError(null);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = password.getEditText().getText().toString();

        if(val.isEmpty()){
            password.setError("Field cannot be empty");
            return false;
        }
        else{
            password.setError(null);
            return true;
        }
    }

    private Boolean confirmPassword(){
        String val1 = confirmPassword.getEditText().getText().toString();
        String val2 = password.getEditText().getText().toString();
        if(val1.equals(val2)){
            confirmPassword.setError(null);
            return true;
        }
        else {
            confirmPassword.setError("password doesn't match");
            return false;
        }
    }


    public void registerUser(){

        if(!validateName() || !validateEmail() || !validateMobileNo() || !validateUsername()|| !validatePassword() || !confirmPassword()){
            return;
        }

        String Name = name.getEditText().getText().toString();
        String Username = user_name.getEditText().getText().toString();
        String Email = email.getEditText().getText().toString();
        String Mobile = mobile_Number.getEditText().getText().toString();
        String Password = password.getEditText().getText().toString();

        fAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    userID = fAuth.getCurrentUser().getUid();
                    Toast.makeText(CreatePatient.this, "Created " +userID, Toast.LENGTH_SHORT).show();

                    DocumentReference documentReference = fStore.collection("userss").document(userID);
                    Map<String,Object> user = new HashMap<>();
                    user.put("FullName",Name);
                    user.put("Username",Username);
                    user.put("Mobile",Mobile);
                    user.put("Email",Email);

                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(CreatePatient.this, "onSuccess: User profile is created for", Toast.LENGTH_SHORT).show();

                            Log.d(TAG, "onSuccess: User profile is created for " + userID);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG,"onFailure: " + e.toString());
                        }
                    });

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    //sending verification email to user below

                    FirebaseUser fuser = fAuth.getCurrentUser();
                    fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(CreatePatient.this, "Verification Email has been sent! ", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Log.d(TAG, "onFailure: Email not sent! " + e.getMessage());

                        }
                    });
                    Toast.makeText(CreatePatient.this, "User created ", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));

                }else {
                    Toast.makeText(CreatePatient.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    //progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}