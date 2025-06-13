package com.example.plantely;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Ermöglicht das Anlegen einer neuen Pflanze mit Namen, Foto und Beschreibung.
 */

public class AddPlantActivity extends AppCompatActivity {
    private static final int REQUEST_PHOTO = 100;
    private ImageView imageView;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        // Views
        EditText nameEdit       = findViewById(R.id.editTextPlantName);
        Spinner catSpinner      = findViewById(R.id.spinnerCategory);
        imageView               = findViewById(R.id.imageViewPlantPhoto);
        Button selectPhotoBtn   = findViewById(R.id.buttonSelectPhoto);
        EditText descEdit       = findViewById(R.id.editTextPlantDescription);
        Button saveButton       = findViewById(R.id.buttonSavePlant);

        // Spinner mit Kategorien füllen
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.plant_categories,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catSpinner.setAdapter(adapter);

        // Foto auswählen
        selectPhotoBtn.setOnClickListener(v -> {
            Intent pick = new Intent(Intent.ACTION_PICK);
            pick.setType("image/*");
            startActivityForResult(pick, REQUEST_PHOTO);
        });

        // Speichern
        saveButton.setOnClickListener(v -> {
            String name     = nameEdit.getText().toString().trim();
            String desc     = descEdit.getText().toString().trim();
            String category = catSpinner.getSelectedItem().toString();

            // Ins Repository einfügen
            PlantRepository.getInstance(getApplicationContext()).addPlant(
                    new Plant(name, desc, imageUri, category)
            );

            // Zurück zur Liste
            setResult(Activity.RESULT_OK);
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PHOTO && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
}
