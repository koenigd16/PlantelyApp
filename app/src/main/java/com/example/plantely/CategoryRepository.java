package com.example.plantely;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Repository zur Verwaltung von Pflanzkategorien.
 * Persistiert in SharedPreferences als JSON-Liste.
 */
public class CategoryRepository {
    private static final String PREFS_NAME     = "plantely_prefs";
    private static final String KEY_CATEGORIES = "categories_json";

    private static CategoryRepository instance;
    private final SharedPreferences prefs;
    private final Gson gson;
    private List<String> categories;

    private CategoryRepository(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        loadCategories();
    }

    /**
     * Holt das Singleton-Objekt. Muss mit Context aufgerufen werden.
     */
    public static CategoryRepository getInstance(Context context) {
        if (instance == null) {
            instance = new CategoryRepository(context);
        }
        return instance;
    }

    /**
     * Lädt Kategorien aus SharedPreferences oder initialisiert Default-Werte.
     */
    private void loadCategories() {
        String json = prefs.getString(KEY_CATEGORIES, null);
        if (json != null) {
            Type type = new TypeToken<List<String>>(){}.getType();
            categories = gson.fromJson(json, type);
        } else {
            categories = new ArrayList<>(Arrays.asList(
                    "Alle", "Sukkulente", "Zimmerpflanze", "Kräuter", "Blühpflanze"
            ));
            saveCategories();
        }
    }

    /**
     * Speichert die aktuelle Kategorienliste in SharedPreferences.
     */
    private void saveCategories() {
        String json = gson.toJson(categories);
        prefs.edit()
                .putString(KEY_CATEGORIES, json)
                .apply();
    }

    /**
     * Gibt alle verfügbaren Kategorien zurück.
     */
    public List<String> getCategories() {
        return categories;
    }

    /**
     * Fügt eine neue Kategorie hinzu, falls sie nicht existiert.
     */
    public void addCategory(String category) {
        if (!categories.contains(category)) {
            categories.add(category);
            saveCategories();
        }
    }

    /**
     * Entfernt eine Kategorie, außer "Alle".
     */
    public void removeCategory(String category) {
        if (!"Alle".equals(category) && categories.contains(category)) {
            categories.remove(category);
            saveCategories();
        }
    }
}
