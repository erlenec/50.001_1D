package com.example.a500011d;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostingActivity extends AppCompatActivity {
    String[] status = {ItemEntry.Status.LOST.toString(), ItemEntry.Status.FOUND.toString()};
    ItemEntry.Status statusSelected;
    String username;
    String imagePath; //TODO: image upload
    EditText editTextItem;
    EditText editTextLocation;
    EditText editTextDescription;

    Button buttonPost;

    DatabaseReference mRootDatabaseRef;
    DatabaseReference mNodeRefItem;

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

        buttonPost = findViewById(R.id.buttonPost);

        mRootDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mNodeRefItem = mRootDatabaseRef.child(getString(R.string.item_node_key));

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = editTextItem.getText().toString();
                String location = editTextLocation.getText().toString();
                String description = editTextDescription.getText().toString();

                if (item.isEmpty() || description.isEmpty()) {
                    Toast.makeText(PostingActivity.this, R.string.error_empty_compulsory, Toast.LENGTH_LONG).show();
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
                }

            }

        });

    }
}