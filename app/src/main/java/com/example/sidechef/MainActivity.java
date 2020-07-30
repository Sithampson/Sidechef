package com.example.sidechef;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView Email;
    private EditText Password;
    private ProgressBar progressBar;

//    Connection to firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("LoginUsers");
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser CurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                CurrentUser = firebaseAuth.getCurrentUser();
                if (CurrentUser != null){
                    String currentUserId = CurrentUser.getUid();

                    collectionReference.whereEqualTo("UserID", currentUserId)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    if(error != null){
                                        Log.d("MainActivity", error.toString());
                                        return;
                                    }

                                    assert value != null;
                                    if(!value.isEmpty()){
                                        for(QueryDocumentSnapshot snapshot : value){
                                            UserApi userApi = UserApi.getInstance();
                                            userApi.setUsername(snapshot.getString("Username"));
                                            userApi.setUserId(snapshot.getString("UserID"));
                                            userApi.setFullName(snapshot.getString("FullName"));

                                            progressBar.setVisibility(View.INVISIBLE);

                                            startActivity(new Intent(MainActivity.this, Home_Page.class));
                                        }
                                    }
                                }
                            });
                }
                else{

                }
            }
        };

        progressBar = findViewById(R.id.login_progressbar);
        Email = findViewById(R.id.EmailEditText);
        Password = findViewById(R.id.PasswordEditText);
        Button login = findViewById(R.id.LoginButton);
        TextView registerText = findViewById(R.id.RegisterText);

        progressBar.setVisibility(View.INVISIBLE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                loginEmailPasswordUser(Email.getText().toString().trim(), Password.getText().toString().trim());
            }
        });

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this,Register.class);
                startActivity(registerIntent);
            }
        });

    }

    private void loginEmailPasswordUser(String email, String pwd){
        if(!email.isEmpty() && !pwd.isEmpty()){
            firebaseAuth.signInWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                if(user != null){
                                    final String currentUserId = user.getUid();

                                    collectionReference.whereEqualTo("UserID", currentUserId)
                                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                    if(error != null){
                                                        Log.d("MainActivity", error.toString());
                                                    }
                                                    assert value != null;
                                                    if(!value.isEmpty()){
                                                        for(QueryDocumentSnapshot snapshot : value){
                                                            UserApi userApi = UserApi.getInstance();
                                                            userApi.setUsername(snapshot.getString("Username"));
                                                            userApi.setUserId(snapshot.getString("UserID"));
                                                            userApi.setFullName(snapshot.getString("FullName"));

                                                            progressBar.setVisibility(View.INVISIBLE);

                                                            startActivity(new Intent(MainActivity.this, Home_Page.class));
                                                        }
                                                    }
                                                }
                                            });
                                }
                            }
                            else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Log.d("MainActivity", "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.d("MainActivity", e.toString());

                        }
                    });
        }
        else{
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Please enter Email and Password", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        CurrentUser = firebaseAuth.getCurrentUser();
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
