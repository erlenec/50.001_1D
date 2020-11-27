package com.example.a500011d;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText editTextUsername;
    EditText editTextPassword;

    Button buttonLogIn;
    Button buttonSignUp;

    DatabaseReference mRootDatabaseRef;
    DatabaseReference mNodeRefUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogIn = findViewById(R.id.buttonLogIn);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        mRootDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mNodeRefUsers = mRootDatabaseRef.child(getString(R.string.user_node_key));

        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameInput = editTextUsername.getText().toString();
                String passwordInput = editTextPassword.getText().toString();
                if (usernameInput.isEmpty()) {
                    Toast.makeText(LoginActivity.this, R.string.error_empty_username, Toast.LENGTH_SHORT).show();
                }
                else if (passwordInput.isEmpty()) {
                    Toast.makeText(LoginActivity.this, R.string.error_empty_password, Toast.LENGTH_SHORT).show();
                }
                else {
                    mNodeRefUsers.orderByChild(getString(R.string.user_username)).equalTo(usernameInput).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getChildrenCount() == 0) {
                                Toast.makeText(LoginActivity.this, R.string.error_user_not_found, Toast.LENGTH_SHORT).show();
                            } else {
                                DataSnapshot ds = snapshot.getChildren().iterator().next();
                                User compareUser = ds.getValue(User.class);
                                if (compareUser.validate(passwordInput)) {
                                    // TODO: set correct intent
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LoginActivity.this, R.string.error_wrong_password, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: set correct intent
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}