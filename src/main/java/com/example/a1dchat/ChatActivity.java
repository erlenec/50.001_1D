package com.example.a1dchat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    public static StringBuilder chatp = new StringBuilder();
    EditText send_msg;
    TextView ui;
    String send_massage;
    Button send,back,refresh;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef = db.collection("ChatChannel").document("Anonymous");
    public Map<String,Object> dataNotInOrder;


    public interface CallBack {
        void onCallback(StringBuilder str);

    }

    public void gatData(final CallBack callback) {

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            dataNotInOrder = documentSnapshot.getData();
                            Map<String,Object> dataInOrder = new LinkedHashMap<String,Object>();
                            List<String> timeList = new ArrayList<String>();
                            Iterator<String> it =dataNotInOrder.keySet().iterator();
                            while(it.hasNext()){
                                timeList.add(it.next());
                            }
                            Collections.sort(timeList);
                            Iterator<String> it2 = timeList.iterator();
                            while(it2.hasNext()){
                                String key = it2.next();
                                dataInOrder.put(key,dataNotInOrder.get(key));
                            }
                            StringBuilder messages = new StringBuilder();
                            if(dataInOrder.keySet() != null){
                                for(String key : dataInOrder.keySet()){
                                    messages.append("\n").append(key).append("\n").append("\n").append(dataInOrder.get(key)).append("\n");
                                    //messages += key + "\n" + doc.get(key) + "\n";
                                }
                            }

                            Toast.makeText(ChatActivity.this,"Data loaded",Toast.LENGTH_SHORT).show();
                            callback.onCallback(messages);
                        }else{
                            Toast.makeText(ChatActivity.this,"No data exists",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChatActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        Log.d("TAG",e.toString());
                    }
                });

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        // to syn the chat string with the firebase here.

        gatData(new CallBack() {
            @Override
            public void onCallback(StringBuilder str) {
                chatp = str;
            }
        });

        ui=findViewById(R.id.ChatText);
        ui.setText(chatp);
        send = findViewById(R.id.SendButton);
        //back = findViewById(R.id.BackButton);
        refresh = findViewById(R.id.RefButton);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_msg = findViewById(R.id.editTextValue);
                send_massage = send_msg.getText().toString();
                if(send_massage!=null&&send_massage.length()!=0){
                    String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
                    chatp.append("\n").append(currentTime).append("\n").append("\n").append(send_massage).append("\n");

                    while(chatp.charAt(chatp.length()-1)=='\n'){
                        chatp.replace((chatp.length()-1),chatp.length(), "");
                    }
                    StringBuilder currentMessage = chatp;
                    docRef.update(currentTime,currentMessage.substring(currentMessage.lastIndexOf("\n")))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ChatActivity.this, "Message saved", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChatActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            Log.d("TAG",e.toString());
                        }
                    });
                    recreate();

                }
            }

        });




        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

    }

}