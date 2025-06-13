package com.example.plantely;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Speichert und lädt alle Pflanzen persistent via SharedPreferences (JSON).
 */

public class PlantRepository {
    private static final String PREFS_NAME     = "plantely_prefs";
    private static final String KEY_PLANT_LIST = "plant_list_json";

    private static PlantRepository instance;
    private final SharedPreferences prefs;
    private final Gson gson;
    private List<Plant> plants;

    private PlantRepository(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        loadPlants();
    }

    /**
     * Holt das Singleton und initialisiert es mit Context.
     * Beispiel: PlantRepository.getInstance(getApplicationContext())
     */
    public static PlantRepository getInstance(Context context) {
        if (instance == null) {
            instance = new PlantRepository(context);
        }
        return instance;
    }

    /** Gibt die aktuelle Liste aller Pflanzen zurück. */
    public List<Plant> getPlants() {
        return plants;
    }

    /**
     * Gibt eine Pflanze anhand ihres Namens zurück oder null,
     * falls nicht gefunden.
     */
    public Plant getPlantByName(String name) {
        for (Plant p : plants) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    /** Fügt eine neue Pflanze hinzu und speichert sofort. */
    public void addPlant(Plant plant) {
        plants.add(plant);
        savePlants();
    }

    /** Entfernt eine Pflanze und speichert sofort. */
    public void removePlant(Plant plant) {
        plants.remove(plant);
        savePlants();
    }

    /**
     * Speichert Änderungen an bestehenden Pflanzen (z.B. Intervalle, Reminder)
     * in SharedPreferences.
     */
    public void saveChanges() {
        savePlants();
    }

    /** Lädt die Pflanzenliste aus SharedPreferences (JSON) oder initialisiert neu. */
    private void loadPlants() {
        String json = prefs.getString(KEY_PLANT_LIST, null);
        if (json != null) {
            Type type = new TypeToken<List<Plant>>() {}.getType();
            plants = gson.fromJson(json, type);
        } else {
            plants = new ArrayList<>();
        }
    }

    /** Interne Methode: serialisiert und speichert die Liste. */
    private void savePlants() {
        String json = gson.toJson(plants);
        prefs.edit()
                .putString(KEY_PLANT_LIST, json)
                .apply();
    }
}
