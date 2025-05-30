package com.example.plantely;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * Statistik
 */



public class StatsActivity extends AppCompatActivity {
    private TextView totalView;
    private TextView toWaterView;
    private Button clearAllBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarStats);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        totalView   = findViewById(R.id.textTotalPlants);
        toWaterView = findViewById(R.id.textToWater);
        clearAllBtn = findViewById(R.id.buttonClearAll);

        clearAllBtn.setOnClickListener(v -> {
            PlantRepository.getInstance().getPlants().clear();
            updateStats();
            Toast.makeText(this,
                    "Alle Einträge wurden gelöscht",
                    Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStats();
    }

    private void updateStats() {
        int total   = PlantRepository.getInstance().getPlants().size();
        int toWater = total; // Platzhalter
        totalView.setText("Gesamtpflanzen: " + total);
        toWaterView.setText("Pflanzen zum Gießen: " + toWater);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
