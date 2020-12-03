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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PostActivity extends AppCompatActivity {
    EditText item, date, information;
    Button Post, Back;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef = db.collection("Items").document("LostItem");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        item = findViewById(R.id.edititemText);
        date = findViewById(R.id.editDateText);
        information = findViewById(R.id.editInfoText);
        Post = findViewById(R.id.PostButton2);
        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ans = "Item: " + item.getText().toString() + "\nDate:" + date.getText().toString() + "\nExtra Infomation:"
                        + information.getText().toString() + "\n\n\n";
                Map<String,String> iteminfo = new HashMap<String,String>();
                iteminfo.put("item",item.getText().toString());
                iteminfo.put("date",date.getText().toString());
                iteminfo.put("information",information.getText().toString());
                docRef.update(item.getText().toString(),iteminfo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(PostActivity.this, "Post successfully!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        Log.d("TAG",e.toString());
                    }
                });
                Intent intent = new Intent(PostActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
/*
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

public class PostActivity extends AppCompatActivity {
    String[] status = {ItemEntry.Status.LOST.toString(), ItemEntry.Status.FOUND.toString()};
    ItemEntry.Status statusSelected;
    String username;
    String imagePath;

    EditText editTextItem;
    EditText editTextLocation;
    EditText editTextDescription;
    ImageView imageViewSelected;

    Button buttonSelectImage;
    Button buttonPost;

    DatabaseReference mRootDatabaseRef;
    DatabaseReference mNodeRefItem;

    final StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    final static int REQUEST_IMAGE_GET = 2000;
    final static String KEY_PATH = "Image";
    final static String KEY_NAME = "Name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);
        Intent intent = getIntent();
        username = intent.getStringExtra(getString(R.string.username_intent));

        Spinner spin = findViewById(R.id.spinnerStatus);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, status);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                statusSelected = ItemEntry.Status.valueOf(adapterView.getItemAtPosition(pos).toString().toUpperCase());
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        editTextItem = findViewById(R.id.editTextItem);
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextDescription = findViewById(R.id.editTextDescription);
        imageViewSelected = findViewById(R.id.imageViewSelected);

        buttonPost = findViewById(R.id.buttonPost);

        mRootDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mNodeRefItem = mRootDatabaseRef.child(getString(R.string.item_node_key));

        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_GET);
                }

            }
        });

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = editTextItem.getText().toString();
                String location = editTextLocation.getText().toString();
                String description = editTextDescription.getText().toString();

                if (item.isEmpty() || description.isEmpty()) {
                    Toast.makeText(PostActivity.this, R.string.error_empty_compulsory, Toast.LENGTH_LONG).show();
                } else {
                    ItemEntry.ItemEntryBuilder builder = new ItemEntry.ItemEntryBuilder();
                    builder.setDescription(description);
                    builder.setItem(item);
                    builder.setUsername(username);
                    if (!location.isEmpty()) builder.setLocation(location);
                    if (imagePath != null) builder.setImagePath(imagePath);
                    builder.setStatus(statusSelected);
                    ItemEntry itemEntry = builder.build();
                    Log.d("d", itemEntry.getItem());
                    Log.d("d", itemEntry.getStatus().toString());
                    if(!location.isEmpty()) Log.d("d", itemEntry.getLocation());
                    Log.d("d", itemEntry.getDescription());
                    Log.d("d", itemEntry.getDate_created());
                    mNodeRefItem.push().setValue(itemEntry);
                    //TODO: proper intent
                    Intent intent = new Intent(PostActivity.this,HomeActivity.class);
                    startActivity(intent);
                }


            }

        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            Uri fullPhotoUri = data.getData();
            try {
                String filename = "/backgrounds/" + FireBaseUtils.getFileName(PostActivity.this, fullPhotoUri);
                imagePath = filename;
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference imageRef = storageRef.child(filename);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fullPhotoUri);
                FireBaseUtils.uploadImageToStorage(PostActivity.this, imageRef, bitmap);
            } catch (IOException e) {
                Log.e("uploadimage", "IOException getting image");
                e.printStackTrace();
            }
            imageViewSelected.setImageURI(fullPhotoUri);
        }
        else {
            Toast.makeText(PostActivity.this, R.string.file_not_found, Toast.LENGTH_LONG);
        }
    }
}*/
