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
import com.example.sidechef.Model.User;

public class Register extends AppCompatActivity {
    private EditText Username;
    private EditText Password;
    private EditText CnfPassword;
    protected TextView LoginText;
    protected Button Register;
    private EditText FullName;
    private TextView MissDetail;

    DataBaseHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mydb = new DataBaseHelper(this);
        Username = findViewById(R.id.RegUsernameText);
        Password = findViewById(R.id.RegPasswordText);
        CnfPassword = findViewById(R.id.RegConfirmpassText);
        LoginText = findViewById(R.id.RegLoginText);
        Register = findViewById(R.id.RegisterButton);
        FullName = findViewById(R.id.RegFullNameText);
        MissDetail = findViewById(R.id.missing_detail);

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
                String username = Username.getText().toString();
                String full = FullName.getText().toString();
                String pwd = Password.getText().toString();
                String cnf_pwd = CnfPassword.getText().toString();
                try{
                    if(!username.isEmpty() && !full.isEmpty() && !pwd.isEmpty() && !cnf_pwd.isEmpty()){
                        if(pwd.equals(cnf_pwd)){
                            User user = new User(username, full, pwd);
                            long val = mydb.addUser(user);

                            MissDetail.setText("");
                            if(val > 0){
                                Toast.makeText(Register.this,"You have registered",Toast.LENGTH_SHORT).show();
                                Intent moveToHome = new Intent(Register.this,Home_Page.class);
                                moveToHome.putExtra("Origin_Activity", "Register_Activity");
                                moveToHome.putExtra("profile_name", username);
                                startActivity(moveToHome);
                            }
                            else{
                                Toast.makeText(Register.this,"Registration Error",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            MissDetail.setText(R.string.password_not_match);
                        }
                    }
                    else{
                        if (full.isEmpty()){
                            MissDetail.setText(R.string.missing_name);
                        }

                        else if (username.isEmpty()){
                            MissDetail.setText(R.string.missing_username);
                        }

                        else {
                            MissDetail.setText(R.string.missing_password);
                        }
                    }
                }
                catch (Exception e){
                    Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
