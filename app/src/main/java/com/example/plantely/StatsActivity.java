package com.example.plantely;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Visualisiert Statistiken (Gesamtzahl, zu gießende Pflanzen, Pflanzen pro Kategorie) und bietet Mark-All/Reset-Funktionen.
 */

public class StatsActivity extends AppCompatActivity {
    private TextView totalView, toWaterView, categoryHeader;
    private LinearLayout containerToWater, containerCategoryStats;
    private Button markAllBtn, clearAllBtn;

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

        // Views finden
        totalView              = findViewById(R.id.textTotalPlants);
        toWaterView            = findViewById(R.id.textToWater);
        categoryHeader         = findViewById(R.id.textCategoryHeader);
        containerToWater       = findViewById(R.id.containerToWater);
        containerCategoryStats = findViewById(R.id.containerCategoryStats);
        markAllBtn             = findViewById(R.id.buttonMarkAll);
        clearAllBtn            = findViewById(R.id.buttonClearAll);

        // "Pflanzen pro Kategorie:" unterstreichen
        categoryHeader.setPaintFlags(
                categoryHeader.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG
        );

        // Alle als gegossen markieren
        markAllBtn.setOnClickListener(v -> {
            containerToWater.removeAllViews();
            updateWaterCount();
            Toast.makeText(this, "Alle als gegossen markiert", Toast.LENGTH_SHORT).show();
        });

        // Alle Einträge löschen
        clearAllBtn.setOnClickListener(v -> {
            PlantRepository repo = PlantRepository.getInstance(getApplicationContext());
            repo.getPlants().clear();
            repo.saveChanges();
            updateStats();
            Toast.makeText(this, "Alle Einträge wurden gelöscht", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStats();
    }

    /**
     * Aktualisiert alle Statistiken:
     * - Gesamtzahl
     * - Liste Pflanzen zum Gießen
     * - Pflanzen pro Kategorie
     */
    private void updateStats() {
        PlantRepository repo = PlantRepository.getInstance(getApplicationContext());
        List<Plant> all      = repo.getPlants();

        // Gesamtpflanzen
        totalView.setText("Gesamtpflanzen: " + all.size());

        // Pflanzen zum Gießen
        containerToWater.removeAllViews();
        for (Plant p : all) {
            View row = getLayoutInflater().inflate(
                    R.layout.item_stats_plant, containerToWater, false);
            TextView name = row.findViewById(R.id.textStatsPlantName);
            Button btn    = row.findViewById(R.id.buttonGegossen);

            name.setText(p.getName());
            btn.setOnClickListener(v -> {
                containerToWater.removeView(row);
                updateWaterCount();
                Toast.makeText(this,
                        p.getName() + " als gegossen markiert",
                        Toast.LENGTH_SHORT).show();
            });

            containerToWater.addView(row);
        }
        updateWaterCount();

        // Pflanzen pro Kategorie
        containerCategoryStats.removeAllViews();
        Map<String,Integer> counts = new HashMap<>();
        for (Plant p : all) {
            String cat = p.getCategory();
            counts.put(cat, counts.getOrDefault(cat, 0) + 1);
        }
        for (Map.Entry<String,Integer> e : counts.entrySet()) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            tv.setText(e.getKey() + ": " + e.getValue());
            tv.setTextSize(16f);
            tv.setPadding(0, 8, 0, 8);
            containerCategoryStats.addView(tv);
        }
    }

    /** Aktualisiert den Zähler rechts unten "Pflanzen zum Gießen: X" */
    private void updateWaterCount() {
        int count = containerToWater.getChildCount();
        toWaterView.setText("Pflanzen zum Gießen: " + count);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
