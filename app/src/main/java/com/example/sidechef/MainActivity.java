package com.example.sidechef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText Username;
    private EditText Password;
    private Button Login;
    private TextView RegisterText;

    DataBaseHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mydb = new DataBaseHelper(this);

        Username = (EditText)findViewById(R.id.UsenameEditText);
        Password = (EditText)findViewById(R.id.PasswordEditText);
        Login = (Button)findViewById(R.id.LoginButton);
        RegisterText = (TextView)findViewById(R.id.RegisterText);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean res = mydb.checkUser(Username.getText().toString(), Password.getText().toString());
                if(res == true)
                {
                    Intent HomePage = new Intent(MainActivity.this,Home_Page.class);
                    startActivity(HomePage);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Login Error",Toast.LENGTH_SHORT).show();
                }
            }
        });

        RegisterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this,Register.class);
                startActivity(registerIntent);
            }
        });

    }
}
