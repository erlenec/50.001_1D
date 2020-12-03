package com.example.a1dchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText username, password;
    Button login,signup;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef = db.collection("UserInfo").document("name&password");
    public Map<String,Object> doc;
    public interface CallBack {
        void onCallback(List<Map<String,String>> userList);

    }
    public void getUserInfo(final CallBack callback) {

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            doc = documentSnapshot.getData();
                            List<Map<String,String>> userInfo = new ArrayList<Map<String,String>>();
                            boolean isUser = false;
                            for(String key:doc.keySet()){
                                userInfo.add((Map<String, String>) doc.get(key));
                            }
                            callback.onCallback(userInfo);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        Log.d("TAG",e.toString());
                    }
                });

            }
        });
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        //chat = findViewById(R.id.ChatButton);
        /*
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        ChatActivity.class);
                startActivity(intent);
            }
        });*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.editTextUsername);
        password =  findViewById(R.id.editTextPassword);
        Button login = findViewById(R.id.buttonLogIn);
        Button signup = findViewById(R.id.buttonSignUp);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                if(username.getText().toString().isEmpty())
                    Toast.makeText(MainActivity.this, "Please Enter your Username", Toast.LENGTH_SHORT).show();
                else if(password.getText().toString().isEmpty())
                    Toast.makeText(MainActivity.this, "Please Enter your Password", Toast.LENGTH_SHORT).show();*/
                //else if password wrong toast wrong password

                // if password correct toast welcome
//                Intent intent = new Intent(MainActivity.this,HomeActivity.class);
//                startActivity(intent);

                getUserInfo(new CallBack() {
                    @Override
                    public void onCallback(List<Map<String,String>> userList) {
                        boolean isUser = false;
                        for(Map<String,String> user : userList){
                            if(username.getText().toString().equals(user.get(("name"))) && password.getText().toString().equals(user.get("password"))){
                                Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                                startActivity(intent);
                                Toast.makeText(MainActivity.this, "Log in successfully", Toast.LENGTH_SHORT).show();
                                isUser = true;
                                break;
                            }
                        }
                        if(isUser == false){
                            Toast.makeText(MainActivity.this, "UserName or PassWord is WRONG!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });


        //if password username not passed, toast it is wrong and stop here.


    }
}