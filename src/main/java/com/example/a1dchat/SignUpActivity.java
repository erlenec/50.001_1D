package com.example.a1dchat;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {

    EditText editTextUsername;
    EditText editTextPassword;

    Button buttonSignUp;
    Button buttonLogIn;

    DatabaseReference mRootDatabaseRef;
    DatabaseReference mNodeRefUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogIn = findViewById(R.id.buttonBackLogIn);
        buttonSignUp = findViewById(R.id.buttonSignUpNow);

        mRootDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mNodeRefUsers = mRootDatabaseRef.child(getString(R.string.user_node_key));

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String usernameInput = editTextUsername.getText().toString();
                final String passwordInput = editTextPassword.getText().toString();
                if (usernameInput.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, R.string.error_empty_username, Toast.LENGTH_SHORT).show();
                }
                else if (passwordInput.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, R.string.error_empty_password, Toast.LENGTH_SHORT).show();
                }
                else {
                    mNodeRefUsers.orderByChild(getString(R.string.user_username)).equalTo(usernameInput).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getChildrenCount() == 0) {
                                User.UserBuilder userBuilder = new User.UserBuilder().setPassword(passwordInput).setUsername(usernameInput);
                                User user = userBuilder.build();
                                mNodeRefUsers.push().setValue(user);
                                Toast.makeText(SignUpActivity.this, R.string.successful_sign_up, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignUpActivity.this, R.string.error_username_exists, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }


            }
        });



        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
