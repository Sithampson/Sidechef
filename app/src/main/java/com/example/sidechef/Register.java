package com.example.sidechef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sidechef.Activity.ActivityHomePage;
import com.example.sidechef.Activity.ActivityLogin;
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
    private TextView strengthView;
    private ProgressBar RegProgressBar;
    private ProgressBar strength_progressbar;

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

        strengthView = findViewById(R.id.password_strength);
        strength_progressbar = findViewById(R.id.strength_progressBar);

        Username = findViewById(R.id.RegUsernameText);
        Password = findViewById(R.id.RegPasswordText);
        CnfPassword = findViewById(R.id.RegConfirmpassText);
        LoginText = findViewById(R.id.RegLoginText);
        Register = findViewById(R.id.RegisterButton);
        FullName = findViewById(R.id.RegFullNameText);
        MissDetail = findViewById(R.id.missing_detail);
        Email = findViewById(R.id.RegEmailText);
        RegProgressBar = findViewById(R.id.RegProgressBar);
        RegProgressBar.setVisibility(View.INVISIBLE);

        LoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, ActivityLogin.class);
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

        strength_progressbar.setVisibility(View.GONE);
        strengthView.setVisibility(View.GONE);
        Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    strength_progressbar.setVisibility(View.GONE);
                    strengthView.setVisibility(View.GONE);
                } else {
                    strength_progressbar.setVisibility(View.VISIBLE);
                    strengthView.setVisibility(View.VISIBLE);
                    updatePasswordStrengthView(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getText().toString();
                final String username = Username.getText().toString();
                final String full = FullName.getText().toString();
                String pwd = Password.getText().toString();
                String cnf_pwd = CnfPassword.getText().toString();

                RegProgressBar.setVisibility(View.VISIBLE);

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
                                                                        RegProgressBar.setVisibility(View.INVISIBLE);
                                                                        String name = task.getResult().getString("Username");
                                                                        Toast.makeText(Register.this,"You have registered",Toast.LENGTH_SHORT).show();

                                                                        UserApi userApi = UserApi.getInstance(); // Global API
                                                                        userApi.setUserId(currentUserID);
                                                                        userApi.setUsername(name);

                                                                        Intent moveToHome = new Intent(Register.this, ActivityHomePage.class);
                                                                        startActivity(moveToHome);
                                                                    }
                                                                    else{
                                                                        RegProgressBar.setVisibility(View.INVISIBLE);
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
                RegProgressBar.setVisibility(View.INVISIBLE);
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

    private void updatePasswordStrengthView(String password) {
        if (TextView.VISIBLE != strengthView.getVisibility())
            return;

        if (password.isEmpty()) {
            strengthView.setText("");
            strength_progressbar.setProgress(0);
            return;
        }

        PasswordStrength str = PasswordStrength.calculateStrength(password);
        strengthView.setText(str.getText(this));
        strengthView.setTextColor(str.getColor());

        PorterDuff.Mode mode = android.graphics.PorterDuff.Mode.SRC_IN;
        strength_progressbar.getProgressDrawable().setColorFilter(str.getColor(), mode);
        if (str.getText(this).equals(getString(R.string.password_strength_weak))) {
            strength_progressbar.setProgress(25);
        } else if (str.getText(this).equals(getString(R.string.password_strength_medium))) {
            strength_progressbar.setProgress(50);
        } else if (str.getText(this).equals(getString(R.string.password_strength_strong))) {
            strength_progressbar.setProgress(75);
        } else {
            strength_progressbar.setProgress(100);
        }
    }

}
