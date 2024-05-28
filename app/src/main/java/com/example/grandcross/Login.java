package com.example.grandcross;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://grandcroos-7ee23-default-rtdb.firebaseio.com");

    EditText loginEditText, passwordEditText;
    TextView RegBut;
    Button LoginBut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEditText = findViewById(R.id.LoginId);
        passwordEditText = findViewById(R.id.PasswordId);
        LoginBut = findViewById(R.id.LoginButtonId);
        RegBut = findViewById(R.id.RegButtonId);

        RegBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Registration.class);
                startActivity(intent);
            }
        });

        LoginBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login, password;
                login = loginEditText.getText().toString().trim();
                password = passwordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(login) || TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Введите Логин и Пароль", Toast.LENGTH_SHORT).show();
                } else {

                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            boolean loginFound = false;
                            String userId = null;

                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                String userLogin = userSnapshot.child("login").getValue(String.class);
                                String userPassword = userSnapshot.child("password").getValue(String.class);

                                if (userLogin.equals(login) && userPassword.equals(password)) {
                                    loginFound = true;
                                    userId = userSnapshot.getKey();
                                    break;
                                }
                            }

                            if (loginFound) {
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                intent.putExtra("userId", userId).putExtra("login",login);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Login.this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }
        });
    }
}