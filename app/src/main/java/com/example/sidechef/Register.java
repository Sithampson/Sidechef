package com.example.sidechef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sidechef.Model.UserApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Register extends AppCompatActivity {
    private AutoCompleteTextView Email;
    private EditText Username;
    private EditText Password;
    private EditText CnfPassword;
    protected TextView LoginText;
    protected Button Register;
    private EditText FullName;
    private TextView MissDetail;

    //    Connection to firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("LoginUsers");
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        Username = findViewById(R.id.RegUsernameText);
        Password = findViewById(R.id.RegPasswordText);
        CnfPassword = findViewById(R.id.RegConfirmpassText);
        LoginText = findViewById(R.id.RegLoginText);
        Register = findViewById(R.id.RegisterButton);
        FullName = findViewById(R.id.RegFullNameText);
        MissDetail = findViewById(R.id.missing_detail);
        Email = findViewById(R.id.RegEmailText);
        final ProgressBar progressBar = findViewById(R.id.RegProgressBar);
        progressBar.setVisibility(View.INVISIBLE);

        LoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null){
                    //user is already logged in
                }
                else{
                    // no user not
                }
            }
        };

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getText().toString();
                final String username = Username.getText().toString();
                final String full = FullName.getText().toString();
                String pwd = Password.getText().toString();
                String cnf_pwd = CnfPassword.getText().toString();

                progressBar.setVisibility(View.VISIBLE);

                try{
                    if(!email.isEmpty() && !username.isEmpty() && !full.isEmpty() && !pwd.isEmpty() && !cnf_pwd.isEmpty()){
                        if(pwd.equals(cnf_pwd)){
                            firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                currentUser = firebaseAuth.getCurrentUser();
                                                assert currentUser != null;
                                                final String currentUserID = currentUser.getUid();

                                                Map<String, String> userObj = new HashMap<>();
                                                userObj.put("UserID", currentUserID);
                                                userObj.put("Username", username);
                                                userObj.put("FullName", full);

                                                collectionReference.add(userObj)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                        if(Objects.requireNonNull(task.getResult()).exists()){
                                                                            progressBar.setVisibility(View.INVISIBLE);
                                                                            String name = task.getResult().getString("Username");
                                                                            Toast.makeText(Register.this,"You have registered",Toast.LENGTH_SHORT).show();

                                                                            UserApi userApi = UserApi.getInstance(); // Global API
                                                                            userApi.setUserId(currentUserID);
                                                                            userApi.setUsername(name);

                                                                            Intent moveToHome = new Intent(Register.this,Home_Page.class);
                                                                            startActivity(moveToHome);
                                                                        }
                                                                        else{
                                                                            progressBar.setVisibility(View.INVISIBLE);
                                                                            Log.d("Register/collction/else", "Error");

                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.d("Register/collction/fail", e.toString());
                                                            }
                                                        });
                                            }
                                            else{
                                                Toast.makeText(Register.this,"Registration Error",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("Register/onFailure", e.toString());
                                            Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        else{
                            MissDetail.setText(R.string.password_not_match);
                        }
                    }
                    else{
                        if (full.isEmpty()){
                            MissDetail.setText(R.string.missing_name);
                        }

                        else if (email.isEmpty()){
                            MissDetail.setText(R.string.missing_email);
                        }

                        else if (username.isEmpty()){
                            MissDetail.setText(R.string.missing_username);
                        }

                        else {
                            MissDetail.setText(R.string.missing_password);
                        }
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }
                catch (Exception e){
                    Log.d("Register.java", e.toString());
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(firebaseAuth != null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

}
