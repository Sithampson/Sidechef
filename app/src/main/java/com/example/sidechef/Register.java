package com.example.sidechef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    private EditText Username;
    private EditText Password;
    private EditText CnfPassword;
    private TextView LoginText;
    private Button Register;
    private EditText FullName;

    DataBaseHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mydb = new DataBaseHelper(this);
        Username = (EditText) findViewById(R.id.RegUsernameText);
        Password = (EditText) findViewById(R.id.RegPasswordText);
        CnfPassword = (EditText) findViewById(R.id.RegConfirmpassText);
        LoginText = (TextView) findViewById(R.id.RegLoginText);
        Register = (Button) findViewById(R.id.RegisterButton);
        FullName = (EditText) findViewById(R.id.RegFullNameText);

        LoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = Username.getText().toString().trim();
                String full = FullName.getText().toString().trim();
                String pwd = Password.getText().toString().trim();
                String cnf_pwd = CnfPassword.getText().toString().trim();

                if(pwd.equals(cnf_pwd)){
                    long val = mydb.addUser(user,full,pwd);
                    if(val > 0){
                        Toast.makeText(Register.this,"You have registered",Toast.LENGTH_SHORT).show();
                        Intent moveToHome = new Intent(Register.this,Home_Page.class);
                        startActivity(moveToHome);
                    }
                    else{
                        Toast.makeText(Register.this,"Registeration Error",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(Register.this,"Password is not matching",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
