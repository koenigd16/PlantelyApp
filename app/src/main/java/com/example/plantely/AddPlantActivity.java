package com.example.plantely;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity um Pflanzen abzuspeichern
 */



public class AddPlantActivity extends AppCompatActivity {
    private static final int REQUEST_PHOTO = 100;
    private ImageView imageView;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        imageView = findViewById(R.id.imageViewPlantPhoto);
        Button selectPhoto = findViewById(R.id.buttonSelectPhoto);
        EditText nameEdit = findViewById(R.id.editTextPlantName);
        EditText descEdit = findViewById(R.id.editTextPlantDescription);
        Button saveButton = findViewById(R.id.buttonSavePlant);

        selectPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_PHOTO);
        });

        // Speichern-Button fügt neue Pflanze ins Repository ein
        saveButton.setOnClickListener(v -> {
            String name = nameEdit.getText().toString();
            String desc = descEdit.getText().toString();
            // imageUri wurde in onActivityResult gesetzt
            Plant plant = new Plant(name, desc, imageUri);
            PlantRepository.getInstance().addPlant(plant);
            // Zurück zur Übersicht
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PHOTO && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();  // hier speichern
            imageView.setImageURI(imageUri);
        }
    }
}
