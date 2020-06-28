package com.example.sidechef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sidechef.Data.DataBaseHelper;

public class MainActivity extends AppCompatActivity {

    private EditText Username;
    private EditText Password;
    private static final String origin_activity = "Main_Activity";

    DataBaseHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mydb = new DataBaseHelper(this);

        Username = findViewById(R.id.UsenameEditText);
        Password = findViewById(R.id.PasswordEditText);
        Button login = findViewById(R.id.LoginButton);
        TextView registerText = findViewById(R.id.RegisterText);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean res = mydb.checkUser(Username.getText().toString(), Password.getText().toString());
                if(res)
                {
                    Intent HomePage = new Intent(MainActivity.this,Home_Page.class);
                    HomePage.putExtra("profile_name", Username.getText().toString());
                    HomePage.putExtra("Origin_Activity", origin_activity);
                    startActivity(HomePage);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Login Error",Toast.LENGTH_SHORT).show();
                }
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
}
